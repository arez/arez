package arez.promise.example;

import arez.Arez;
import arez.Disposable;
import arez.Observer;
import arez.promise.ObservablePromise;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class ObservablePromiseExample {
    @JsType(isNative = true, name = "Response", namespace = JsPackage.GLOBAL)
    private static class Response {
        @JsProperty(name = "status")
        native int status();

        @JsProperty(name = "statusText")
        native String statusText();
    }

    @JsType(isNative = true, name = "Error", namespace = JsPackage.GLOBAL)
    private static class JsError {
        @JsProperty(name = "message")
        native String message();
    }

    @JsType(isNative = true, name = "Element", namespace = JsPackage.GLOBAL)
    private static class Element {
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

    private final ObservablePromise<Response, Object> _promise;
    private final Observer _observer;

    private ObservablePromiseExample() {
        _promise = ObservablePromise.create(fetch("index.html"));
        _observer = Arez.context().observer(() -> outputStatus(_promise));
    }

    @JsMethod
    public static ObservablePromiseExample bootstrap() {
        return new ObservablePromiseExample();
    }

    @JsMethod
    public void dispose() {
        _observer.dispose();
        Disposable.dispose(_promise);
    }

    @JsMethod
    public boolean isDisposed() {
        return _observer.isDisposed() && Disposable.isDisposed(_promise);
    }

    private static void outputStatus(final ObservablePromise<Response, Object> observablePromise) {
        final ObservablePromise.State state = observablePromise.getState();
        final Response response = ObservablePromise.State.FULFILLED == state ? observablePromise.getValue() : null;
        final JsError error = ObservablePromise.State.REJECTED == state ? (JsError) observablePromise.getError() : null;
        final String message = "Promise State: " + state
                + (null != response ? " - Response: " + response.status() + ": " + response.statusText() : "")
                + (null != error ? " - Error: " + error.message() : "");
        NativeConsole.log(message);
        final Element element = document().querySelector("#app");
        assert null != element;
        element.setTextContent(message);
    }

    @JsMethod(name = "fetch", namespace = JsPackage.GLOBAL)
    private static native ObservablePromise.Promise<Response> fetch(String url);

    @JsProperty(name = "document", namespace = JsPackage.GLOBAL)
    private static native Document document();
}
