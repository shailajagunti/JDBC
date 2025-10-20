package com.ecommerce.ui;

public final class HtmlRenderer {
    private HtmlRenderer() {}

    public static String page(String title, String bodyContent) {
        String styles = "body{font-family:ui-sans-serif,system-ui,-apple-system,Segoe UI,Roboto;max-width:980px;margin:2rem auto;padding:0 1rem} a{color:#2563eb;text-decoration:none} a:hover{text-decoration:underline} .grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(220px,1fr));gap:1rem} .card{border:1px solid #e5e7eb;border-radius:12px;padding:1rem} .muted{color:#6b7280} .header{display:flex;align-items:center;justify-content:space-between;margin-bottom:1rem} .btn{display:inline-block;background:#111827;color:#fff;padding:.5rem 1rem;border-radius:.5rem}";
        return "<!doctype html><html lang='en'><head><meta charset='utf-8'/>" +
                "<meta name='viewport' content='width=device-width,initial-scale=1'/>" +
                "<title>" + escape(title) + "</title>" +
                "<style>" + styles + "</style>" +
                "</head><body>" + bodyContent + "</body></html>";
    }

    public static String escape(String input) {
        if (input == null) return "";
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
