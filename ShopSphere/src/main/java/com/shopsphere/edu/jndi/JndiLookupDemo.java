package com.shopsphere.edu.jndi;

import javax.naming.*;
import java.util.Hashtable;
import java.util.Properties;

/**
 * Academic Demonstration: Java Naming and Directory Interface (JNDI).
 * <p>
 * RTU Advanced Java Syllabus Traceability:
 * - javax.naming.Context and InitialContext architecture
 * - JNDI naming environment configuration
 * - Resource binding (bind / rebind), resolution (lookup), and deallocation (unbind)
 * - Enterprise application server DataSource injection simulation (java:comp/env/jdbc/ShopSphereDS)
 * </p>
 */
public class JndiLookupDemo {

    /**
     * Minimal in-memory Context implementation to demonstrate JNDI mechanics
     * without requiring an active heavyweight enterprise application server.
     */
    public static class SimpleInMemoryContext implements Context {
        private final Hashtable<String, Object> bindings = new Hashtable<>();
        private final Hashtable<Object, Object> environment;

        public SimpleInMemoryContext(Hashtable<?, ?> env) {
            this.environment = new Hashtable<>();
            if (env != null) {
                this.environment.putAll(env);
            }
        }

        @Override
        public Object lookup(String name) throws NamingException {
            if (!bindings.containsKey(name)) {
                throw new NameNotFoundException("JNDI Name not bound in context: " + name);
            }
            return bindings.get(name);
        }

        @Override
        public void bind(String name, Object obj) throws NamingException {
            if (bindings.containsKey(name)) {
                throw new NameAlreadyBoundException("JNDI Name already bound: " + name);
            }
            bindings.put(name, obj);
        }

        @Override
        public void rebind(String name, Object obj) throws NamingException {
            bindings.put(name, obj);
        }

        @Override
        public void unbind(String name) throws NamingException {
            bindings.remove(name);
        }

        @Override public Object lookup(Name name) throws NamingException { return lookup(name.toString()); }
        @Override public void bind(Name name, Object obj) throws NamingException { bind(name.toString(), obj); }
        @Override public void rebind(Name name, Object obj) throws NamingException { rebind(name.toString(), obj); }
        @Override public void unbind(Name name) throws NamingException { unbind(name.toString()); }
        @Override public void rename(Name oldName, Name newName) throws NamingException {}
        @Override public void rename(String oldName, String newName) throws NamingException {}
        @Override public NamingEnumeration<NameClassPair> list(Name name) throws NamingException { return null; }
        @Override public NamingEnumeration<NameClassPair> list(String name) throws NamingException { return null; }
        @Override public NamingEnumeration<Binding> listBindings(Name name) throws NamingException { return null; }
        @Override public NamingEnumeration<Binding> listBindings(String name) throws NamingException { return null; }
        @Override public void destroySubcontext(Name name) throws NamingException {}
        @Override public void destroySubcontext(String name) throws NamingException {}
        @Override public Context createSubcontext(Name name) throws NamingException { return this; }
        @Override public Context createSubcontext(String name) throws NamingException { return this; }
        @Override public Object lookupLink(Name name) throws NamingException { return lookup(name); }
        @Override public Object lookupLink(String name) throws NamingException { return lookup(name); }
        @Override public NameParser getNameParser(Name name) throws NamingException { return null; }
        @Override public NameParser getNameParser(String name) throws NamingException { return null; }
        @Override public Name composeName(Name name, Name prefix) throws NamingException { return null; }
        @Override public String composeName(String name, String prefix) throws NamingException { return prefix + "/" + name; }
        @Override public Object addToEnvironment(String propName, Object propVal) throws NamingException { return environment.put(propName, propVal); }
        @Override public Object removeFromEnvironment(String propName) throws NamingException { return environment.remove(propName); }
        @Override public Hashtable<?, ?> getEnvironment() throws NamingException { return environment; }
        @Override public void close() throws NamingException { bindings.clear(); }
        @Override public String getNameInNamespace() throws NamingException { return ""; }
    }

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println(" Java Naming and Directory Interface (JNDI) Demonstration");
        System.out.println("======================================================================");

        try {
            // 1. Configure JNDI Environment Properties
            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.shopsphere.edu.jndi.SimpleInMemoryContext");
            env.put(Context.PROVIDER_URL, "local://shopsphere");

            System.out.println("[Step 1] Initializing JNDI Context with Environment: " + env);
            Context ctx = new SimpleInMemoryContext(env);

            // 2. Bind Mock Enterprise DataSource & App Settings
            String dsJndiName = "java:comp/env/jdbc/ShopSphereDS";
            String mockDataSourceConfig = "HikariPool-1 [jdbc:mysql://localhost:3306/shopsphere, user=root, poolSize=20]";
            ctx.bind(dsJndiName, mockDataSourceConfig);
            System.out.println("[Step 2] Bound DataSource to: '" + dsJndiName + "'");

            String appVersionJndi = "java:comp/env/config/appVersion";
            ctx.bind(appVersionJndi, "1.0.0-SNAPSHOT (Jakarta EE 10 / Tomcat 11)");
            System.out.println("[Step 2] Bound App Version to: '" + appVersionJndi + "'");

            // 3. Perform JNDI Lookups (simulating Servlet / EJB injection)
            System.out.println("\n[Step 3] Performing JNDI Lookup Resolution:");
            Object resolvedDS = ctx.lookup(dsJndiName);
            System.out.println("  -> Lookup for '" + dsJndiName + "' returned: " + resolvedDS);

            Object resolvedVersion = ctx.lookup(appVersionJndi);
            System.out.println("  -> Lookup for '" + appVersionJndi + "' returned: " + resolvedVersion);

            // 4. Test NameNotFoundException handling
            System.out.println("\n[Step 4] Testing Lookup for Unbound Name:");
            try {
                ctx.lookup("java:comp/env/jdbc/NonExistentDS");
            } catch (NameNotFoundException e) {
                System.out.println("  -> Successfully caught expected exception: " + e.getMessage());
            }

            // 5. Cleanup Context
            ctx.close();
            System.out.println("\n[Result]: JNDI Context closed. All demonstration steps verified successfully!");

        } catch (Exception e) {
            System.err.println("JNDI Demonstration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
