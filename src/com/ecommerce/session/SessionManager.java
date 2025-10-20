package com.ecommerce.session;

import com.ecommerce.cart.Cart;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final String COOKIE_NAME = "sid";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final Map<String, Cart> sessions = new ConcurrentHashMap<>();

    public String ensureSessionId(String existing) {
        if (existing != null && sessions.containsKey(existing)) {
            return existing;
        }
        String sid = newSessionId();
        sessions.put(sid, new Cart());
        return sid;
    }

    public Cart getCart(String sessionId) {
        return sessions.computeIfAbsent(sessionId, k -> new Cart());
    }

    public static String cookieName() {
        return COOKIE_NAME;
    }

    private static String newSessionId() {
        byte[] buf = new byte[18];
        RANDOM.nextBytes(buf);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
    }
}
