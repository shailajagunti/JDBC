package com.ecommerce.http.handlers;

import com.ecommerce.http.RequestUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HomeHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String html = "" +
                "<!doctype html>" +
                "<html lang='en'>" +
                "<head>" +
                "  <meta charset='utf-8'/>" +
                "  <meta name='viewport' content='width=device-width, initial-scale=1'/>" +
                "  <title>E‑Commerce</title>" +
                "  <style>body{font-family:ui-sans-serif,system-ui,-apple-system,Segoe UI,Roboto;max-width:920px;margin:2rem auto;padding:0 1rem} a{color:#2563eb;text-decoration:none} a:hover{text-decoration:underline} .card{border:1px solid #e5e7eb;border-radius:12px;padding:1rem} .header{display:flex;align-items:center;justify-content:space-between;margin-bottom:1rem} .btn{display:inline-block;background:#111827;color:#fff;padding:.5rem 1rem;border-radius:.5rem}</style>" +
                "</head>" +
                "<body>" +
                "  <div class='header'>" +
                "    <h1>Welcome to the E‑Commerce Demo</h1>" +
                "    <a class='btn' href='/'>Home</a>" +
                "  </div>" +
                "  <div class='card'>" +
                "    <p>This is a minimal Java HTTP server. Product catalog and AI recommendations will appear here as we build them.</p>" +
                "  </div>" +
                "</body>" +
                "</html>";
        RequestUtils.sendHtml(exchange, html);
    }
}
