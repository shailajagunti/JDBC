package com.ecommerce.http;

import com.ecommerce.http.handlers.HomeHandler;
import com.ecommerce.http.handlers.ProductHandlers;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {
    private final int port;
    private HttpServer server;
    private ExecutorService executorService;

    public WebServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        // Routes
        server.createContext("/", new HomeHandler());
        server.createContext("/products", new ProductHandlers.ListHandler());
        server.createContext("/product", new ProductHandlers.DetailHandler());
        server.createContext("/cart", new ProductHandlers.CartHandler(new com.ecommerce.session.SessionManager()));
        server.createContext("/add-to-cart", new ProductHandlers.AddToCartHandler(new com.ecommerce.session.SessionManager()));

        executorService = Executors.newFixedThreadPool(Math.max(4, Runtime.getRuntime().availableProcessors()));
        server.setExecutor(executorService);
        server.start();
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}
