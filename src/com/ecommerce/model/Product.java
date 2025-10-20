package com.ecommerce.model;

import java.util.List;
import java.util.Objects;

public class Product {
    private final String id;
    private final String name;
    private final String description;
    private final List<String> tags;
    private final String imageUrl;
    private final double price;

    public Product(String id, String name, String description, List<String> tags, String imageUrl, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tags = List.copyOf(tags);
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<String> getTags() { return tags; }
    public String getImageUrl() { return imageUrl; }
    public double getPrice() { return price; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
