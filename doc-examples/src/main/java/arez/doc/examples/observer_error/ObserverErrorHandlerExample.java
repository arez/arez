package arez.doc.examples.observer_error;

import arez.Arez;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings("CodeBlock2Expr")
public class ObserverErrorHandlerExample {
    // DOC ELIDE START
    @NullMarked
    @JsType(isNative = true, name = "globalThis.console", namespace = JsPackage.GLOBAL)
    private static final class NativeConsole {
        @JsMethod
        private static native void error(@Nullable Object message, @Nullable Object error);
    }
    // DOC ELIDE END

    public static void main(String @Nullable [] args) {
        Arez.context().addObserverErrorHandler(((observer, error, throwable) -> {
            NativeConsole.error(error + ": Error occurred", throwable);
        }));
    }
}
