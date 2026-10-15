package arez;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jspecify.annotations.Nullable;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public final class ArezConfigTest {
    private static final Map<String, String> FLAGS = Map.ofEntries(
            Map.entry("enable_names", "areNamesEnabled"),
            Map.entry("enable_verify", "isVerifyEnabled"),
            Map.entry("enable_property_introspection", "arePropertyIntrospectorsEnabled"),
            Map.entry("enable_registries", "areRegistriesEnabled"),
            Map.entry("enforce_transaction_type", "enforceTransactionType"),
            Map.entry("purge_tasks_when_runaway_detected", "purgeTasksWhenRunawayDetected"),
            Map.entry("enable_references", "areReferencesEnabled"),
            Map.entry("enable_spies", "areSpiesEnabled"),
            Map.entry("enable_zones", "areZonesEnabled"),
            Map.entry("collections_properties_unmodifiable", "areCollectionsPropertiesUnmodifiable"),
            Map.entry("enable_native_components", "areNativeComponentsEnabled"),
            Map.entry("enable_observer_error_handlers", "areObserverErrorHandlersEnabled"),
            Map.entry("enable_task_interceptor", "isTaskInterceptorEnabled"),
            Map.entry("check_invariants", "checkInvariants"),
            Map.entry("check_expensive_invariants", "checkExpensiveInvariants"),
            Map.entry("check_api_invariants", "checkApiInvariants"));

    @DataProvider
    public Object[][] configurations() {
        return new Object[][] {
            {null, null}, {"production", null}, {"development", null},
            {"production", "true"}, {"development", "false"}, {"development", "TRUE"}
        };
    }

    @Test(dataProvider = "configurations")
    public void configurationDefaultsAndOverrides(final @Nullable String environment, final @Nullable String override)
            throws Exception {
        final Map<String, @Nullable String> original = new LinkedHashMap<>();
        original.put("arez.environment", System.getProperty("arez.environment"));
        original.put("arez.logger", System.getProperty("arez.logger"));
        FLAGS.keySet().forEach(key -> original.put("arez." + key, System.getProperty("arez." + key)));
        try {
            setProperty("arez.environment", environment);
            System.clearProperty("arez.logger");
            FLAGS.keySet().forEach(key -> setProperty("arez." + key, override));
            try (final var loader = isolatedRuntime()) {
                final Class<?> config = loader.loadClass("arez.ArezConfig");
                final boolean production = environment == null || environment.equals("production");
                assertEquals(invoke(config, "isProductionEnvironment"), production);
                assertEquals(invoke(config, "isDevelopmentEnvironment"), !production);
                assertEquals(invoke(config, "loggerType"), production ? "basic" : "proxy");
                for (final var flag : FLAGS.entrySet()) {
                    final boolean defaultValue =
                            switch (flag.getKey()) {
                                case "enable_zones", "enable_verify" -> false;
                                case "enable_references",
                                        "purge_tasks_when_runaway_detected",
                                        "enable_observer_error_handlers",
                                        "enable_task_interceptor" -> true;
                                default -> !production;
                            };
                    assertEquals(
                            invoke(config, flag.getValue()),
                            override == null ? defaultValue : override.equals("true"),
                            flag.getKey());
                }
            }
        } finally {
            original.forEach(ArezConfigTest::setProperty);
        }
    }

    @DataProvider
    public Object[][] loggers() {
        return new Object[][] {{"console", "ConsoleLogger"}, {"none", "NoopLogger"}, {"proxy", "ProxyLogger"}};
    }

    @Test(dataProvider = "loggers")
    public void loggerSelectionAndOutput(final String logger, final String expectedType) throws Exception {
        final String original = System.getProperty("arez.logger");
        final PrintStream originalOut = System.out;
        final var output = new ByteArrayOutputStream();
        try (final var capture = new PrintStream(output, true, StandardCharsets.UTF_8);
                final var loader = isolatedRuntime()) {
            System.setProperty("arez.logger", logger);
            System.setOut(capture);
            final Class<?> type = loader.loadClass("arez.ArezLogger");
            assertEquals(invoke(type, "getLogger").getClass().getSimpleName(), expectedType);
            final var log = type.getDeclaredMethod("log", String.class, Throwable.class);
            log.setAccessible(true);
            log.invoke(null, "first message", null);
            log.invoke(null, "second message", new IllegalStateException("logger test"));
            final String text = output.toString(StandardCharsets.UTF_8);
            if (logger.equals("console")) {
                assertTrue(text.startsWith("first message" + System.lineSeparator() + "second message"));
                assertTrue(text.contains("java.lang.IllegalStateException: logger test"));
            } else {
                assertEquals(text, "");
            }
        } finally {
            System.setOut(originalOut);
            setProperty("arez.logger", original);
        }
    }

    private static URLClassLoader isolatedRuntime() {
        return new URLClassLoader(
                new URL[] {Arez.class.getProtectionDomain().getCodeSource().getLocation()},
                Arez.class.getClassLoader()) {
            @Override
            protected synchronized Class<?> loadClass(final String name, final boolean resolve)
                    throws ClassNotFoundException {
                if (!name.startsWith("arez.")) {
                    return super.loadClass(name, resolve);
                }
                Class<?> type = findLoadedClass(name);
                if (type == null) {
                    type = findClass(name);
                }
                if (resolve) {
                    resolveClass(type);
                }
                return type;
            }
        };
    }

    private static Object invoke(final Class<?> type, final String method) throws Exception {
        final var target = type.getDeclaredMethod(method);
        target.setAccessible(true);
        final Object result = target.invoke(null);
        assertNotNull(result);
        assert null != result;
        return result;
    }

    private static void setProperty(final String name, final @Nullable String value) {
        if (value == null) {
            System.clearProperty(name);
        } else {
            System.setProperty(name, value);
        }
    }
}
