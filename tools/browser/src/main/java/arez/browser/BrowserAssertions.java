package arez.browser;

import arez.Arez;
import jsinterop.annotations.JsMethod;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.realityforge.braincheck.BrainCheckConfig;

@NullMarked
public final class BrowserAssertions {
    private BrowserAssertions() {}

    @JsMethod
    public static void expectChecks(final boolean enabled) {
        if (Arez.shouldCheckInvariants() != enabled
                || Arez.shouldCheckApiInvariants() != enabled
                || Arez.areNamesEnabled() != enabled
                || BrainCheckConfig.checkInvariants() != enabled
                || BrainCheckConfig.checkApiInvariants() != enabled) {
            throw new AssertionError("Expected Arez and Braincheck development checks: " + enabled);
        }
    }

    @JsMethod
    public static void expectText(final String id, final String expected) {
        final String actual = text(id);
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected " + id + " to contain '" + expected + "', got '" + actual + "'");
        }
    }

    @JsMethod
    public static void expectContains(final String id, final String expected) {
        final String actual = text(id);
        if (null == actual || !actual.contains(expected)) {
            throw new AssertionError("Expected " + id + " to include '" + expected + "', got '" + actual + "'");
        }
    }

    @JsMethod
    private static native @Nullable String text(String id);
}
