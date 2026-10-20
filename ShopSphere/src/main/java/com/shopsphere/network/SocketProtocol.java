package com.shopsphere.network;

/**
 * Application-layer Communication Protocol for ShopSphere TCP Inventory Sockets.
 * <p>
 * Defines command keywords, separators, and standard status responses for
 * multi-threaded client-server communication as required by the RTU Advanced Java
 * networking syllabus.
 * </p>
 */
public final class SocketProtocol {

    private SocketProtocol() {}

    public static final int DEFAULT_PORT = 8085;
    public static final String DEFAULT_HOST = "localhost";

    // Client -> Server Command Keywords
    public static final String CMD_PING = "PING";
    public static final String CMD_GET_PRODUCT = "GET_PRODUCT";
    public static final String CMD_GET_STOCK = "GET_STOCK";
    public static final String CMD_UPDATE_STOCK = "UPDATE_STOCK";
    public static final String CMD_SEARCH = "SEARCH";
    public static final String CMD_QUIT = "QUIT";

    // Server -> Client Status Prefixes
    public static final String STATUS_OK = "OK: ";
    public static final String STATUS_ERR = "ERR: ";
    public static final String STATUS_BYE = "BYE: Connection closed.";
}
