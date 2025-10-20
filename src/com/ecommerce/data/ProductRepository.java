package com.ecommerce.data;

import com.ecommerce.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProductRepository {
    private static final List<Product> products = new ArrayList<>();
    private static final Map<String, Product> idToProduct = new HashMap<>();

    static {
        seed();
    }

    private static void seed() {
        add(new Product(
                "p1",
                "Wireless Headphones",
                "Over-ear wireless headphones with noise cancellation and 30-hour battery life.",
                List.of("audio","headphones","wireless","noise-cancelling","bluetooth"),
                "https://picsum.photos/seed/headphones/400/300",
                129.99
        ));
        add(new Product(
                "p2",
                "Smartwatch Series X",
                "Water-resistant smartwatch with heart-rate monitor, GPS, and sleep tracking.",
                List.of("wearable","smartwatch","fitness","gps","heart-rate"),
                "https://picsum.photos/seed/smartwatch/400/300",
                199.00
        ));
        add(new Product(
                "p3",
                "Mechanical Keyboard",
                "Compact mechanical keyboard with hot-swappable switches and RGB backlight.",
                List.of("keyboard","mechanical","rgb","gaming","typing"),
                "https://picsum.photos/seed/keyboard/400/300",
                89.50
        ));
        add(new Product(
                "p4",
                "4K Action Camera",
                "Rugged 4K action camera with stabilization and wide-angle lens.",
                List.of("camera","4k","action","stabilization","outdoor"),
                "https://picsum.photos/seed/camera/400/300",
                239.99
        ));
        add(new Product(
                "p5",
                "Portable SSD 1TB",
                "High-speed portable SSD with USB-C connectivity and durable design.",
                List.of("storage","ssd","usb-c","portable","backup"),
                "https://picsum.photos/seed/ssd/400/300",
                119.95
        ));
        add(new Product(
                "p6",
                "Noise-Cancelling Earbuds",
                "True wireless earbuds with adaptive noise cancellation and wireless charging.",
                List.of("audio","earbuds","wireless","noise-cancelling","bluetooth"),
                "https://picsum.photos/seed/earbuds/400/300",
                149.00
        ));
        add(new Product(
                "p7",
                "Ergonomic Office Chair",
                "Breathable mesh chair with lumbar support and adjustable armrests.",
                List.of("office","chair","ergonomic","comfort","work"),
                "https://picsum.photos/seed/chair/400/300",
                259.00
        ));
        add(new Product(
                "p8",
                "Gaming Mouse Pro",
                "High-precision gaming mouse with 8 programmable buttons and RGB.",
                List.of("mouse","gaming","rgb","precision","dpi"),
                "https://picsum.photos/seed/mouse/400/300",
                59.99
        ));
    }

    private static void add(Product p) {
        products.add(p);
        idToProduct.put(p.getId(), p);
    }

    public static List<Product> findAll() {
        return Collections.unmodifiableList(products);
    }

    public static Optional<Product> findById(String id) {
        return Optional.ofNullable(idToProduct.get(id));
    }
}
