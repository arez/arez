package arez.dom;

import arez.Disposable;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Exposes {@code document.visibilityState} as an observable property for specified documents.
 *
 * <p>See the <a href="https://github.com/arez/arez/blob/master/extras/dom-example/src/main/java/arez/dom/example/DocumentVisibilityExample.java">compiled J2CL example</a> for a JsInterop bootstrap,
 * reactive updates and observer disposal.</p>
 */
public final class DocumentVisibility implements Disposable {
    /**
     * Minimal facade for the browser document visibility API.
     */
    @JsType(isNative = true, name = "Document", namespace = JsPackage.GLOBAL)
    public static class Document extends EventDrivenValue.EventTarget {
        protected Document() {}

        @JsProperty(name = "visibilityState")
        native String visibilityState();
    }

    /**
     * The visibility state of the document.
     */
    public enum Visibility {
        /**
         * The page content may be at least partially visible. In practice this means that the page is the foreground tab of a non-minimized window.
         */
        VISIBLE,
        /**
         * The page content is not visible to the user. In practice this means that the document is either a background tab or part of a minimized window, or the OS screen lock is active.
         */
        HIDDEN,
        /**
         * The page content is being prerendered and is not visible to the user (considered hidden for purposes of document.hidden). The document may start in this state, but will never transition to it from another value. Note: browser support is optional.
         */
        PRERENDER
    }

    /**
     * The underlying component performing the monitoring.
     */
    private final EventDrivenValue<Document, String> _value;

    /**
     * Create component monitoring the default document.
     *
     * @return the new component.
     */
    public static DocumentVisibility create() {
        return create(document());
    }

    /**
     * Create component monitoring specific document.
     *
     * @param document the document.
     * @return the new component.
     */
    public static DocumentVisibility create(final Document document) {
        return new DocumentVisibility(document);
    }

    private DocumentVisibility(final Document document) {
        _value = EventDrivenValue.create(document, "visibilitychange", Document::visibilityState);
    }

    /**
     * Return the document that monitoring visibility state.
     *
     * @return the document.
     */
    public Document getDocument() {
        return _value.getSource();
    }

    /**
     * Change the document that is having visibility state monitored.
     *
     * @param document the new document.
     */
    public void setDocument(final Document document) {
        _value.setSource(document);
    }

    /**
     * Return the visibility state of the document as an enum.
     *
     * @return the visibility state as an enum.
     */
    public Visibility getVisibility() {
        return asVisibility(getVisibilityState());
    }

    /**
     * Return the visibility state of the document as a string.
     *
     * @return the visibility state as a string.
     */
    public String getVisibilityState() {
        return _value.getValue();
    }

    /**
     * Return true if visibility state is "visible".
     *
     * @return true if visibility state is "visible".
     */
    public boolean isVisible() {
        return "visible".equals(getVisibilityState());
    }

    /**
     * Return true if visibility state is "hidden".
     *
     * @return true if visibility state is "hidden".
     */
    public boolean isHidden() {
        return "hidden".equals(getVisibilityState());
    }

    @Override
    public void dispose() {
        Disposable.dispose(_value);
    }

    @Override
    public boolean isDisposed() {
        return Disposable.isDisposed(_value);
    }

    /**
     * Convert the visibility state as an enum.
     *
     * @param state the state.
     * @return the visibility enum.
     */
    private Visibility asVisibility(final String state) {
        if ("visible".equals(state)) {
            return Visibility.VISIBLE;
        } else if ("hidden".equals(state)) {
            return Visibility.HIDDEN;
        } else {
            assert "prerender".equals(state);
            return Visibility.PRERENDER;
        }
    }

    @JsProperty(name = "document", namespace = JsPackage.GLOBAL)
    private static native Document document();
}
