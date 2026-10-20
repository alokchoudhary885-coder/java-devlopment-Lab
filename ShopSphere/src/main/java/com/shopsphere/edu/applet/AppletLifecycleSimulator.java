package com.shopsphere.edu.applet;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Academic Demonstration & Lifecycle Simulator: Java Applet Architecture.
 * <p>
 * RTU Advanced Java Syllabus Traceability:
 * - Applet lifecycle methods: init(), start(), paint(), stop(), destroy()
 * - Browser execution sandbox model vs Standalone Application execution
 * - Critical Modern Java Context: Explains why Applets were deprecated in JDK 9 (JEP 289)
 *   and permanently removed in JDK 17 (JEP 398) due to NPAPI deprecation in modern browsers
 *   and shift towards HTML5 / Web Standards / Jakarta EE.
 * </p>
 */
public class AppletLifecycleSimulator {

    public enum AppletState {
        UNINITIALIZED,
        INITIALIZED,
        RUNNING,
        STOPPED,
        DESTROYED
    }

    private AppletState currentState = AppletState.UNINITIALIZED;
    private final String appletName;

    public AppletLifecycleSimulator(String name) {
        this.appletName = name;
        log("Applet object instantiated in browser heap memory.");
    }

    /**
     * 1. init(): Invoked once when the browser loads the HTML containing the &lt;applet&gt; tag.
     * Used for component initialization, parameter loading, and image preloading.
     */
    public void init() {
        currentState = AppletState.INITIALIZED;
        log("1. init() invoked: Loading configuration parameters, fonts, and UI buttons.");
    }

    /**
     * 2. start(): Invoked after init(), and each time the user navigates back to the applet page.
     * Used to start threads, audio loops, or animation tickers.
     */
    public void start() {
        if (currentState != AppletState.INITIALIZED && currentState != AppletState.STOPPED) {
            log("Cannot start applet from state: " + currentState);
            return;
        }
        currentState = AppletState.RUNNING;
        log("2. start() invoked: Starting background threads and animation timers. Applet is ACTIVE.");
    }

    /**
     * 3. paint(Graphics g): Invoked whenever the browser window requires visual re-rendering.
     */
    public void paint(String graphicsContext) {
        if (currentState != AppletState.RUNNING) {
            log("Cannot paint while applet is not running.");
            return;
        }
        log("3. paint() invoked: Rendering ShopSphere visual banner on (" + graphicsContext + ").");
    }

    /**
     * 4. stop(): Invoked when the user navigates away from the browser tab or minimizes browser.
     * Used to suspend CPU-heavy animations and background audio.
     */
    public void stop() {
        if (currentState != AppletState.RUNNING) {
            return;
        }
        currentState = AppletState.STOPPED;
        log("4. stop() invoked: Pausing background threads. Applet is IDLE.");
    }

    /**
     * 5. destroy(): Invoked once when the browser window is closed or browser cache is flushed.
     * Used for final resource deallocation and socket cleanup.
     */
    public void destroy() {
        currentState = AppletState.DESTROYED;
        log("5. destroy() invoked: Releasing all resources and sockets. Garbage collection ready.");
    }

    public AppletState getCurrentState() {
        return currentState;
    }

    private void log(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        System.out.println("[" + timestamp + "] [" + appletName + "] [" + currentState + "] -> " + message);
    }

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println(" RTU Advanced Java — Applet Lifecycle Simulation & Viva Guide");
        System.out.println("======================================================================");

        AppletLifecycleSimulator simulator = new AppletLifecycleSimulator("ShopSphereBannerApplet");

        // Simulate Full Browser Lifecycle Flow
        System.out.println("\n--- [Phase 1: Browser Loads Page] ---");
        simulator.init();
        simulator.start();
        simulator.paint("Browser Canvas: Width=800, Height=200");

        System.out.println("\n--- [Phase 2: User Switches Browser Tab] ---");
        simulator.stop();

        System.out.println("\n--- [Phase 3: User Returns to Browser Tab] ---");
        simulator.start();
        simulator.paint("Browser Canvas: Repainting Active Layout");

        System.out.println("\n--- [Phase 4: User Closes Browser] ---");
        simulator.stop();
        simulator.destroy();

        System.out.println("\n======================================================================");
        System.out.println(" Academic Viva Voce Defense Summary for Applets:");
        System.out.println(" 1. Lifecycle order: init() -> start() -> paint() -> stop() -> destroy()");
        System.out.println(" 2. Key Differences: Applets do not have a main() method; execution is");
        System.out.println("    controlled entirely by the browser's Java Plug-in container.");
        System.out.println(" 3. JDK Status: Deprecated in Java 9 (JEP 289), completely removed in");
        System.out.println("    JDK 17 (JEP 398). Modern web applications use HTML5/JS frontend and");
        System.out.println("    Jakarta Servlets/JSP backend as demonstrated in ShopSphere.");
        System.out.println("======================================================================");
    }
}
