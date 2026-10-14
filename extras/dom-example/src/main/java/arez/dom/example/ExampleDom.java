package arez.dom.example;

import java.util.Objects;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class ExampleDom {
    @JsFunction
    interface EventListener {
        void handleEvent(Object event);
    }

    @JsType(isNative = true, name = "Element", namespace = JsPackage.GLOBAL)
    static class Element {
        @JsMethod
        native void addEventListener(String type, EventListener listener);

        @JsMethod
        native void removeEventListener(String type, EventListener listener);

        @JsProperty
        native void setTextContent(String text);
    }

    @JsType(isNative = true, name = "Document", namespace = JsPackage.GLOBAL)
    private static class Document {
        @JsMethod
        native @Nullable Element querySelector(String selector);
    }

    @JsType(isNative = true, name = "console", namespace = JsPackage.GLOBAL)
    private static final class NativeConsole {
        @JsMethod
        private static native void log(Object message);
    }

    private ExampleDom() {}

    static Element element(final String selector) {
        return Objects.requireNonNull(document().querySelector(selector), "Missing example element: " + selector);
    }

    static Runnable listen(final String selector, final String type, final EventListener listener) {
        final Element element = element(selector);
        element.addEventListener(type, listener);
        return () -> element.removeEventListener(type, listener);
    }

    static void log(final Object message) {
        NativeConsole.log(message);
    }

    @JsProperty(name = "document", namespace = JsPackage.GLOBAL)
    private static native Document document();
}
