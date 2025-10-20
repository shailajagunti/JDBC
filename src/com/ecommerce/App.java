package com.ecommerce;

import com.ecommerce.http.WebServer;

public class App {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        String portEnv = System.getenv("PORT");
        if (portEnv != null) {
            try {
                port = Integer.parseInt(portEnv.trim());
            } catch (NumberFormatException ignored) { }
        }
        WebServer webServer = new WebServer(port);
        webServer.start();
        System.out.println("E-commerce server is running on http://localhost:" + port + "/");
    }
}
