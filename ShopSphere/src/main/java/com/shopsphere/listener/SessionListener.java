package com.shopsphere.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Session Lifecycle Event Listener.
 * Implements HttpSessionListener to track live concurrent sessions for analytics and viva defense.
 */
@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        ServletContext ctx = se.getSession().getServletContext();
        AtomicInteger counter = (AtomicInteger) ctx.getAttribute("activeSessionsCount");
        if (counter != null) {
            int current = counter.incrementAndGet();
            System.out.println("[SessionListener] Session created: " + se.getSession().getId()
                    + " | Active sessions: " + current);
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        ServletContext ctx = se.getSession().getServletContext();
        AtomicInteger counter = (AtomicInteger) ctx.getAttribute("activeSessionsCount");
        if (counter != null) {
            int current = counter.decrementAndGet();
            System.out.println("[SessionListener] Session destroyed: " + se.getSession().getId()
                    + " | Active sessions: " + current);
        }
    }
}
