package com.shopsphere;

import com.shopsphere.edu.applet.AppletLifecycleSimulator;
import com.shopsphere.edu.applet.AppletLifecycleSimulator.AppletState;
import com.shopsphere.network.SocketProtocol;
import com.shopsphere.rmi.RemoteInventoryService;
import com.shopsphere.rmi.RemoteInventoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit and contract tests for Advanced Java Syllabus modules:
 * Socket Protocol, RMI Distributed Computing, Applet Lifecycle Simulation, and i18n Resource Bundles.
 */
public class NetworkRMITest {

    // =========================================================================
    // 1. Socket Protocol Validation
    // =========================================================================

    @Test
    @DisplayName("Socket Protocol: Verifies port, host, commands, and status constants")
    public void testSocketProtocolConstants() {
        assertEquals(8085, SocketProtocol.DEFAULT_PORT);
        assertEquals("localhost", SocketProtocol.DEFAULT_HOST);

        assertEquals("PING", SocketProtocol.CMD_PING);
        assertEquals("GET_PRODUCT", SocketProtocol.CMD_GET_PRODUCT);
        assertEquals("GET_STOCK", SocketProtocol.CMD_GET_STOCK);
        assertEquals("UPDATE_STOCK", SocketProtocol.CMD_UPDATE_STOCK);
        assertEquals("SEARCH", SocketProtocol.CMD_SEARCH);
        assertEquals("QUIT", SocketProtocol.CMD_QUIT);

        assertEquals("OK: ", SocketProtocol.STATUS_OK);
        assertEquals("ERR: ", SocketProtocol.STATUS_ERR);
        assertEquals("BYE: Connection closed.", SocketProtocol.STATUS_BYE);
    }

    @Test
    @DisplayName("Socket Protocol: Simulates protocol message parser behavior")
    public void testSocketProtocolParsing() {
        String incomingMessage = "GET_STOCK 102";
        String[] parts = incomingMessage.trim().split("\\s+", 2);

        assertEquals("GET_STOCK", parts[0].toUpperCase());
        assertEquals("102", parts[1]);

        String responsePayload = SocketProtocol.STATUS_OK + "Stock for Product 102 = 45 units";
        assertTrue(responsePayload.startsWith(SocketProtocol.STATUS_OK));
    }

    // =========================================================================
    // 2. RMI Distributed Architecture Contract Validation
    // =========================================================================

    @Test
    @DisplayName("RMI: RemoteInventoryService extends java.rmi.Remote interface")
    public void testRMIInterfaceContract() {
        assertTrue(RemoteInventoryService.class.isInterface(),
                "RemoteInventoryService must be an interface");
        assertTrue(Remote.class.isAssignableFrom(RemoteInventoryService.class),
                "RemoteInventoryService must extend java.rmi.Remote");
    }

    @Test
    @DisplayName("RMI: All RemoteInventoryService methods declare RemoteException")
    public void testRMIMethodRemoteExceptions() {
        Method[] declaredMethods = RemoteInventoryService.class.getDeclaredMethods();
        assertTrue(declaredMethods.length > 0, "Remote interface must define distributed methods");

        for (Method method : declaredMethods) {
            boolean throwsRemoteException = Arrays.stream(method.getExceptionTypes())
                    .anyMatch(ex -> ex.equals(RemoteException.class));
            assertTrue(throwsRemoteException,
                    "Remote method " + method.getName() + " must declare 'throws RemoteException'");
        }
    }

    @Test
    @DisplayName("RMI: RemoteInventoryServiceImpl extends UnicastRemoteObject and implements RemoteInventoryService")
    public void testRMIImplementationContract() {
        assertTrue(UnicastRemoteObject.class.isAssignableFrom(RemoteInventoryServiceImpl.class),
                "RemoteInventoryServiceImpl must extend UnicastRemoteObject for point-to-point RMI communications");
        assertTrue(RemoteInventoryService.class.isAssignableFrom(RemoteInventoryServiceImpl.class),
                "RemoteInventoryServiceImpl must implement RemoteInventoryService");
    }

    // =========================================================================
    // 3. Applet Lifecycle State Machine Simulator
    // =========================================================================

    @Test
    @DisplayName("Applet Simulator: Validates transition sequence init -> start -> stop -> start -> destroy")
    public void testAppletLifecycleTransitions() {
        AppletLifecycleSimulator applet = new AppletLifecycleSimulator("TestApplet");
        assertEquals(AppletState.UNINITIALIZED, applet.getCurrentState());

        applet.init();
        assertEquals(AppletState.INITIALIZED, applet.getCurrentState());

        applet.start();
        assertEquals(AppletState.RUNNING, applet.getCurrentState());

        // Paint should not change running state
        applet.paint("MockGraphicsContext");
        assertEquals(AppletState.RUNNING, applet.getCurrentState());

        // User navigates away
        applet.stop();
        assertEquals(AppletState.STOPPED, applet.getCurrentState());

        // User navigates back
        applet.start();
        assertEquals(AppletState.RUNNING, applet.getCurrentState());

        // Browser closes
        applet.stop();
        applet.destroy();
        assertEquals(AppletState.DESTROYED, applet.getCurrentState());
    }

    // =========================================================================
    // 4. Internationalization (i18n) Resource Bundle Validation
    // =========================================================================

    @Test
    @DisplayName("i18n: Loads English and Hindi resource bundles with valid key mappings")
    public void testResourceBundlesEnglishAndHindi() {
        ResourceBundle enBundle = ResourceBundle.getBundle("messages", Locale.ENGLISH);
        assertNotNull(enBundle, "English resource bundle messages_en.properties must be loadable");
        assertEquals("ShopSphere — Enterprise E-Commerce System", enBundle.getString("app.title"));
        assertEquals("Home", enBundle.getString("nav.home"));
        assertEquals("Shopping Bag", enBundle.getString("nav.cart"));

        ResourceBundle hiBundle = ResourceBundle.getBundle("messages", Locale.forLanguageTag("hi"));
        assertNotNull(hiBundle, "Hindi resource bundle messages_hi.properties must be loadable");
        assertTrue(hiBundle.containsKey("app.title"), "Hindi bundle must contain 'app.title'");
        assertTrue(hiBundle.containsKey("nav.home"), "Hindi bundle must contain 'nav.home'");
        assertFalse(hiBundle.getString("app.title").isEmpty(), "Hindi title must not be empty");
    }
}
