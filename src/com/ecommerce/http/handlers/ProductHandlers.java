package com.ecommerce.http.handlers;

import com.ecommerce.cart.Cart;
import com.ecommerce.data.ProductRepository;
import com.ecommerce.http.RequestUtils;
import com.ecommerce.model.Product;
import com.ecommerce.reco.Recommender;
import com.ecommerce.session.SessionManager;
import com.ecommerce.ui.HtmlRenderer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class ProductHandlers {
    public static class ListHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            StringBuilder body = new StringBuilder();
            body.append("<div class='header'><h1>Products</h1><a class='btn' href='/cart'>Cart</a></div>");
            body.append("<div class='grid'>");
            for (Product p : ProductRepository.findAll()) {
                body.append("<div class='card'>")
                    .append("<img alt='' src='").append(p.getImageUrl()).append("' style='width:100%;height:150px;object-fit:cover;border-radius:8px' />")
                    .append("<h3>").append(HtmlRenderer.escape(p.getName())).append("</h3>")
                    .append("<div class='muted'>$ ").append(String.format("%.2f", p.getPrice())).append("</div>")
                    .append("<p class='muted'>").append(HtmlRenderer.escape(p.getDescription())).append("</p>")
                    .append("<a class='btn' href='/product?id=").append(p.getId()).append("'>View</a>")
                    .append("</div>");
            }
            body.append("</div>");
            String html = HtmlRenderer.page("Products", body.toString());
            RequestUtils.sendHtml(exchange, html);
        }
    }

    public static class DetailHandler implements HttpHandler {
        private final Recommender recommender = new Recommender();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, List<String>> q = RequestUtils.parseQuery(exchange.getRequestURI());
            String id = q.getOrDefault("id", List.of("" )).get(0);
            Optional<Product> opt = ProductRepository.findById(id);
            if (opt.isEmpty()) {
                byte[] notFound = "Product not found".getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(404, notFound.length);
                exchange.getResponseBody().write(notFound);
                exchange.close();
                return;
            }
            Product p = opt.get();

            StringBuilder body = new StringBuilder();
            body.append("<div class='header'><a href='/'>← Back</a><a class='btn' href='/cart'>Cart</a></div>");
            body.append("<div class='card'>");
            body.append("<img alt='' src='").append(p.getImageUrl()).append("' style='width:100%;height:260px;object-fit:cover;border-radius:8px' />");
            body.append("<h1>").append(HtmlRenderer.escape(p.getName())).append("</h1>");
            body.append("<div class='muted'>$ ").append(String.format("%.2f", p.getPrice())).append("</div>");
            body.append("<p>").append(HtmlRenderer.escape(p.getDescription())).append("</p>");
            body.append("<form method='POST' action='/add-to-cart'>")
                .append("<input type='hidden' name='id' value='").append(p.getId()).append("'/>")
                .append("<button class='btn' type='submit'>Add to cart</button>")
                .append("</form>");
            body.append("</div>");

            List<Product> recs = recommender.recommendSimilar(p.getId(), 4);
            if (!recs.isEmpty()) {
                body.append("<h2>Recommended</h2><div class='grid'>");
                for (Product r : recs) {
                    body.append("<div class='card'>")
                        .append("<img alt='' src='").append(r.getImageUrl()).append("' style='width:100%;height:120px;object-fit:cover;border-radius:8px' />")
                        .append("<h3>").append(HtmlRenderer.escape(r.getName())).append("</h3>")
                        .append("<div class='muted'>$ ").append(String.format("%.2f", r.getPrice())).append("</div>")
                        .append("<a class='btn' href='/product?id=").append(r.getId()).append("'>View</a>")
                        .append("</div>");
                }
                body.append("</div>");
            }

            String html = HtmlRenderer.page(p.getName(), body.toString());
            RequestUtils.sendHtml(exchange, html);
        }
    }

    public static class CartHandler implements HttpHandler {
        private final SessionManager sessionManager;

        public CartHandler(SessionManager sessionManager) {
            this.sessionManager = sessionManager;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String sessionId = ensureSession(exchange);
            Cart cart = sessionManager.getCart(sessionId);
            Map<String, Integer> items = cart.snapshot();
            double total = 0.0;
            StringBuilder rows = new StringBuilder();
            for (Map.Entry<String, Integer> e : items.entrySet()) {
                Product p = ProductRepository.findById(e.getKey()).orElse(null);
                if (p == null) continue;
                double line = p.getPrice() * e.getValue();
                total += line;
                rows.append("<tr>")
                    .append("<td>").append(HtmlRenderer.escape(p.getName())).append("</td>")
                    .append("<td>").append(e.getValue()).append("</td>")
                    .append("<td>$ ").append(String.format("%.2f", line)).append("</td>")
                    .append("</tr>");
            }
            StringBuilder body = new StringBuilder();
            body.append("<div class='header'><a href='/'>← Continue shopping</a></div>")
                .append("<div class='card'>")
                .append("<h2>Cart</h2>")
                .append("<table style='width:100%;border-collapse:collapse'>")
                .append("<thead><tr><th align='left'>Product</th><th align='left'>Qty</th><th align='left'>Total</th></tr></thead>")
                .append("<tbody>").append(rows).append("</tbody>")
                .append("<tfoot><tr><td></td><td><strong>Grand Total</strong></td><td><strong>$ ")
                .append(String.format("%.2f", total)).append("</strong></td></tr></tfoot>")
                .append("</table>")
                .append("</div>");
            String html = HtmlRenderer.page("Cart", body.toString());
            RequestUtils.sendHtml(exchange, html);
        }
    }

    public static class AddToCartHandler implements HttpHandler {
        private final SessionManager sessionManager;

        public AddToCartHandler(SessionManager sessionManager) {
            this.sessionManager = sessionManager;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().set("Allow", "POST");
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> form = parseFormEncoded(body);
            String id = form.get("id");
            if (id != null) {
                String sessionId = ensureSession(exchange);
                Cart cart = sessionManager.getCart(sessionId);
                cart.add(id, 1);
            }
            // Redirect back to product page or home
            String location = id != null ? ("/product?id=" + id) : "/";
            exchange.getResponseHeaders().set("Location", location);
            exchange.sendResponseHeaders(302, -1);
            exchange.close();
        }

        private static Map<String, String> parseFormEncoded(String body) {
            return java.util.Arrays.stream(body.split("&"))
                    .map(s -> s.split("=", 2))
                    .filter(kv -> kv.length == 2)
                    .collect(Collectors.toMap(kv -> urlDecode(kv[0]), kv -> urlDecode(kv[1])));
        }

        private static String urlDecode(String s) {
            return java.net.URLDecoder.decode(s, java.nio.charset.StandardCharsets.UTF_8);
        }
    }

    private static String ensureSession(HttpExchange exchange) {
        String cookie = exchange.getRequestHeaders().getFirst("Cookie");
        String sid = null;
        if (cookie != null) {
            for (String part : cookie.split(";\\s*")) {
                String[] kv = part.split("=", 2);
                if (kv.length == 2 && SessionManager.cookieName().equals(kv[0])) {
                    sid = kv[1];
                    break;
                }
            }
        }
        SessionManager sm = SingletonHolder.SESSION;
        String ensured = sm.ensureSessionId(sid);
        if (sid == null || !sid.equals(ensured)) {
            exchange.getResponseHeaders().add("Set-Cookie", SessionManager.cookieName() + "=" + ensured + "; Path=/; HttpOnly; Max-Age=604800");
        }
        return ensured;
    }

    private static class SingletonHolder {
        private static final SessionManager SESSION = new SessionManager();
    }
}
