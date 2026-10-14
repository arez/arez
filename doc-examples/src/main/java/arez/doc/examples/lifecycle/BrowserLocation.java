package arez.doc.examples.lifecycle;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.PostConstruct;
import arez.annotations.PreDispose;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class BrowserLocation {
    // DOC ELIDE START
    @NullMarked
    @JsFunction
    private interface HashChangeEventListener {
        void handleEvent(@Nullable Object event);
    }

    private final @Nullable HashChangeEventListener _listener = this::onHashChangeEvent;
    // DOC ELIDE END

    @PostConstruct
    void postConstruct() {
        addEventListener("hashchange", _listener, false);
        // DOC ELIDE START
        // DOC ELIDE END
    }

    @PreDispose
    void preDispose() {
        removeEventListener("hashchange", _listener, false);
    }

    // DOC ELIDE START
    @Action
    void onHashChangeEvent(final @Nullable Object e) {}

    @JsMethod(name = "addEventListener", namespace = JsPackage.GLOBAL)
    private static native void addEventListener(
            @Nullable String type, @Nullable HashChangeEventListener listener, boolean capture);

    @JsMethod(name = "removeEventListener", namespace = JsPackage.GLOBAL)
    private static native void removeEventListener(
            @Nullable String type, @Nullable HashChangeEventListener listener, boolean capture);
    // DOC ELIDE END
}
