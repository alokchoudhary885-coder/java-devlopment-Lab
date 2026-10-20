package com.shopsphere.edu.url;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Academic Demonstration: Java URL & HttpURLConnection Networking.
 * <p>
 * RTU Advanced Java Syllabus Traceability:
 * - URL and URI object construction and manipulation
 * - URLConnection & HttpURLConnection lifecycle
 * - HTTP Request Header injection (User-Agent, Accept)
 * - Response status code, response headers, and content stream consumption
 * - Exception handling for timeouts, invalid URLs, and network failures
 * </p>
 */
public class URLConnectionDemo {

    public static void inspectUrl(String targetUrl) {
        System.out.println("======================================================================");
        System.out.println(" URLConnection Header & Stream Inspection: " + targetUrl);
        System.out.println("======================================================================");

        HttpURLConnection conn = null;
        try {
            URI uri = URI.create(targetUrl);
            URL url = uri.toURL();

            // 1. Open Connection
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "ShopSphere-ECommerce-Edu-Client/1.0 (RTU Advanced Java)");
            conn.setRequestProperty("Accept", "text/html,application/json,*/*");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            // 2. Connect & Get Status
            conn.connect();
            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();

            System.out.println("[HTTP Response] : " + responseCode + " " + responseMessage);
            System.out.println("[Content-Type]  : " + conn.getContentType());
            System.out.println("[Content-Length]: " + conn.getContentLength() + " bytes");
            System.out.println("[Encoding]      : " + conn.getContentEncoding());

            // 3. Inspect Response Header Fields
            System.out.println("\n[HTTP Response Headers]:");
            Map<String, List<String>> headers = conn.getHeaderFields();
            int count = 0;
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                if (count++ >= 6) break; // Display top 6 headers for demonstration
                System.out.println("  " + (entry.getKey() != null ? entry.getKey() : "Status") + ": " + entry.getValue());
            }

            // 4. Read Preview of Response Stream
            System.out.println("\n[Response Body Preview (First 3 lines)]:");
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                int linesRead = 0;
                while ((line = reader.readLine()) != null && linesRead < 3) {
                    System.out.println("  > " + (line.length() > 80 ? line.substring(0, 80) + "..." : line));
                    linesRead++;
                }
            }

            System.out.println("\n[Result]: URLConnection inspection succeeded!");

        } catch (Exception e) {
            System.out.println("[Notice]: Network or endpoint unavailable (" + e.getMessage() + ").");
            System.out.println("[Academic Simulation]: Demonstrating programmatic protocol parsing:");
            try {
                URI sample = URI.create(targetUrl);
                System.out.println("  Scheme   : " + sample.getScheme());
                System.out.println("  Host     : " + sample.getHost());
                System.out.println("  Port     : " + (sample.getPort() == -1 ? 80 : sample.getPort()));
                System.out.println("  Path     : " + sample.getPath());
                System.out.println("[Result]: URL Parsing and Protocol components demonstrated successfully.");
            } catch (Exception ignored) {}
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static void main(String[] args) {
        // Inspect public educational endpoint
        String testUrl = "https://httpbin.org/get";
        inspectUrl(testUrl);
    }
}
