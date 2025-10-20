package com.ecommerce.reco;

import com.ecommerce.data.ProductRepository;
import com.ecommerce.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Recommender {
    private final List<Product> products;
    private final List<Map<String, Double>> tfidfVectors; // parallel to products
    private final Map<String, Double> idf;

    public Recommender() {
        this.products = ProductRepository.findAll();
        this.idf = computeIdf(products);
        this.tfidfVectors = products.stream()
                .map(p -> computeTfidfVector(p, idf))
                .collect(Collectors.toList());
    }

    public List<Product> recommendSimilar(String productId, int k) {
        int idx = indexOf(productId);
        if (idx < 0) return Collections.emptyList();
        Map<String, Double> target = tfidfVectors.get(idx);
        List<Scored<Product>> scored = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            if (i == idx) continue;
            double score = cosine(target, tfidfVectors.get(i));
            scored.add(new Scored<>(products.get(i), score));
        }
        return topK(scored, k);
    }

    public List<Product> recommendForProductIds(List<String> productIds, int k) {
        if (productIds.isEmpty()) return Collections.emptyList();
        Map<String, Double> agg = new HashMap<>();
        for (String pid : productIds) {
            int idx = indexOf(pid);
            if (idx >= 0) addInPlace(agg, tfidfVectors.get(idx), 1.0);
        }
        if (agg.isEmpty()) return Collections.emptyList();
        List<Scored<Product>> scored = new ArrayList<>();
        Set<String> exclude = new HashSet<>(productIds);
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            if (exclude.contains(p.getId())) continue;
            double score = cosine(agg, tfidfVectors.get(i));
            scored.add(new Scored<>(p, score));
        }
        return topK(scored, k);
    }

    private int indexOf(String productId) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(productId)) return i;
        }
        return -1;
    }

    private static void addInPlace(Map<String, Double> base, Map<String, Double> add, double weight) {
        for (Map.Entry<String, Double> e : add.entrySet()) {
            base.merge(e.getKey(), e.getValue() * weight, Double::sum);
        }
    }

    private static List<Product> topK(List<Scored<Product>> scored, int k) {
        return scored.stream()
                .sorted(Comparator.comparingDouble((Scored<Product> s) -> s.score).reversed())
                .limit(k)
                .map(s -> s.item)
                .collect(Collectors.toList());
    }

    private static record Scored<T>(T item, double score) {}

    private static Map<String, Double> computeIdf(List<Product> products) {
        Map<String, Integer> df = new HashMap<>();
        int n = products.size();
        for (Product p : products) {
            Set<String> uniq = new HashSet<>(tokenize(p));
            for (String t : uniq) {
                df.merge(t, 1, Integer::sum);
            }
        }
        Map<String, Double> idf = new HashMap<>();
        for (Map.Entry<String, Integer> e : df.entrySet()) {
            double val = Math.log((1.0 + n) / (1.0 + e.getValue())) + 1.0; // smoothed idf
            idf.put(e.getKey(), val);
        }
        return idf;
    }

    private static Map<String, Double> computeTfidfVector(Product p, Map<String, Double> idf) {
        List<String> tokens = tokenize(p);
        Map<String, Integer> tfCounts = new HashMap<>();
        for (String t : tokens) tfCounts.merge(t, 1, Integer::sum);
        Map<String, Double> vec = new HashMap<>();
        int maxFreq = tfCounts.values().stream().mapToInt(i -> i).max().orElse(1);
        for (Map.Entry<String, Integer> e : tfCounts.entrySet()) {
            double tf = 0.5 + 0.5 * (e.getValue() / (double) maxFreq); // augmented tf
            double idfVal = idf.getOrDefault(e.getKey(), 1.0);
            vec.put(e.getKey(), tf * idfVal);
        }
        return vec;
    }

    private static List<String> tokenize(Product p) {
        String text = (p.getName() + " " + p.getDescription() + " " + String.join(" ", p.getTags()))
                .toLowerCase(Locale.ROOT);
        String[] parts = text.split("[^a-z0-9+]+");
        List<String> tokens = new ArrayList<>();
        for (String part : parts) {
            if (part.isBlank()) continue;
            if (STOPWORDS.contains(part)) continue;
            tokens.add(part);
        }
        return tokens;
    }

    private static final Set<String> STOPWORDS = Set.of(
            "the","and","a","an","with","for","of","to","in","on","by","is","this","that","plus","series","pro"
    );

    private static double cosine(Map<String, Double> a, Map<String, Double> b) {
        double dot = 0.0;
        double na = 0.0;
        double nb = 0.0;
        for (double v : a.values()) na += v*v;
        for (double v : b.values()) nb += v*v;
        if (na == 0 || nb == 0) return 0.0;
        // iterate over smaller map for efficiency
        Map<String, Double> small = a.size() <= b.size() ? a : b;
        Map<String, Double> large = small == a ? b : a;
        for (Map.Entry<String, Double> e : small.entrySet()) {
            double ov = large.getOrDefault(e.getKey(), 0.0);
            if (ov != 0.0) dot += e.getValue() * ov;
        }
        return dot / (Math.sqrt(na) * Math.sqrt(nb));
    }
}
