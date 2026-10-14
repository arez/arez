package arez.dom;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Factory for getting observable models that sizing of windows.
 *
 * <p>See the <a href="https://github.com/arez/arez/blob/master/extras/dom-example/src/main/java/arez/dom/example/WindowSizeExample.java">compiled J2CL example</a> for a JsInterop bootstrap,
 * reactive updates and observer disposal.</p>
 */
public final class WindowSize {
    /**
     * Minimal facade for the browser window sizing API.
     */
    @JsType(isNative = true, name = "Window", namespace = JsPackage.GLOBAL)
    public static class Window extends EventDrivenValue.EventTarget {
        protected Window() {}

        @JsProperty(name = "innerHeight")
        native int innerHeight();

        @JsProperty(name = "innerWidth")
        native int innerWidth();

        @JsProperty(name = "outerHeight")
        native int outerHeight();

        @JsProperty(name = "outerWidth")
        native int outerWidth();
    }

    private WindowSize() {}

    /**
     * Create an event driven observable component for window.innerWidth and window.innerHeight wrapped in dimension object.
     *
     * @param window the window.
     * @return the event driven observable component.
     */
    public static EventDrivenValue<Window, Dimension> inner(final Window window) {
        return EventDrivenValue.create(window, "resize", w -> new Dimension(w.innerWidth(), w.innerHeight()));
    }

    /**
     * Create an event driven observable component for window.innerHeight.
     *
     * @param window the window.
     * @return the event driven observable component.
     */
    public static EventDrivenValue<Window, Integer> innerHeight(final Window window) {
        return EventDrivenValue.create(window, "resize", Window::innerHeight);
    }

    /**
     * Create an event driven observable component for window.innerWidth.
     *
     * @param window the window.
     * @return the event driven observable component.
     */
    public static EventDrivenValue<Window, Integer> innerWidth(final Window window) {
        return EventDrivenValue.create(window, "resize", Window::innerWidth);
    }

    /**
     * Create an event driven observable component for window.outerHeight.
     *
     * @param window the window.
     * @return the event driven observable component.
     */
    public static EventDrivenValue<Window, Integer> outerHeight(final Window window) {
        return EventDrivenValue.create(window, "resize", Window::outerHeight);
    }

    /**
     * Create an event driven observable component for window.outerWidth.
     *
     * @param window the window.
     * @return the event driven observable component.
     */
    public static EventDrivenValue<Window, Integer> outerWidth(final Window window) {
        return EventDrivenValue.create(window, "resize", Window::outerWidth);
    }
}
