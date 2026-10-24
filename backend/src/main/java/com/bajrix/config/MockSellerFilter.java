package com.bajrix.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Simple mock authentication filter.
 * Expects a request header {@code X-Mock-Seller-Id} containing the seller's numeric ID.
 * The ID is stored as a request attribute "sellerId" for downstream controllers/services.
 * If the header is missing, the request is treated as a buyer (no sellerId).
 */
@Component
public class MockSellerFilter extends OncePerRequestFilter {
    public static final String HEADER = "X-Mock-Seller-Id";
    public static final String ATTR = "sellerId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HEADER);
        if (header != null && !header.isBlank()) {
            try {
                Long sellerId = Long.valueOf(header.trim());
                request.setAttribute(ATTR, sellerId);
            } catch (NumberFormatException ignored) {
                // invalid header – ignore, treat as buyer
            }
        }
        filterChain.doFilter(request, response);
    }
}
