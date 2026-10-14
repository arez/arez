package arez.dom.example;

import arez.Arez;
import arez.Disposable;
import arez.Observer;
import arez.dom.GeoPosition;
import arez.dom.Position;
import jsinterop.annotations.JsMethod;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class GeoPositionExample {
    private final GeoPosition _model = GeoPosition.create();
    private final Runnable _removeWatch;
    private @Nullable Observer _observer;

    private GeoPositionExample() {
        ExampleDom.element("#status").setTextContent("GeoPosition: " + GeoPosition.Status.INITIAL);
        _removeWatch = ExampleDom.listen("#watch", "click", e -> watch());
    }

    @JsMethod
    public static GeoPositionExample bootstrap() {
        return new GeoPositionExample();
    }

    private void watch() {
        if (null == _observer) {
            _observer = Arez.context().observer(this::render);
        }
    }

    private void render() {
        final int status = _model.getStatus();
        final String errorMessage = _model.getErrorMessage();
        final Position position = _model.getPosition();
        ExampleDom.element("#status")
                .setTextContent("GeoPosition: " + status
                        + (null != position
                                ? " Position: " + position.getLatitude() + ", " + position.getLongitude()
                                : "")
                        + (null != errorMessage ? " ErrorMessage: " + errorMessage : ""));
    }

    @JsMethod
    public void dispose() {
        _removeWatch.run();
        if (null != _observer) {
            _observer.dispose();
        }
        Disposable.dispose(_model);
    }

    @JsMethod
    public boolean isDisposed() {
        return (null == _observer || _observer.isDisposed()) && Disposable.isDisposed(_model);
    }
}
