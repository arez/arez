package arez.dom.example;

import arez.Arez;
import arez.Disposable;
import arez.Observer;
import arez.dom.NetworkStatus;
import jsinterop.annotations.JsMethod;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class NetworkStatusExample {
    private final NetworkStatus _model = NetworkStatus.create();
    private final Observer _observer;

    private NetworkStatusExample() {
        _observer = Arez.context().observer(() -> {
            final String message = "Network Status: " + (_model.isOnLine() ? "Online" : "Offline");
            ExampleDom.element("#network").setTextContent(message);
        });
    }

    @JsMethod
    public static NetworkStatusExample bootstrap() {
        return new NetworkStatusExample();
    }

    @JsMethod
    public void dispose() {
        _observer.dispose();
        Disposable.dispose(_model);
    }

    @JsMethod
    public boolean isDisposed() {
        return _observer.isDisposed() && Disposable.isDisposed(_model);
    }
}
