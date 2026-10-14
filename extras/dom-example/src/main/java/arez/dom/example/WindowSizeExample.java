package arez.dom.example;

import arez.Arez;
import arez.Disposable;
import arez.Observer;
import arez.dom.Dimension;
import arez.dom.EventDrivenValue;
import arez.dom.WindowSize;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class WindowSizeExample {
    private final EventDrivenValue<WindowSize.Window, Dimension> _inner = WindowSize.inner(window());
    private final Observer _observer;

    private WindowSizeExample() {
        _observer = Arez.context().observer(() -> {
            final Dimension dimension = _inner.getValue();
            ExampleDom.element("#status")
                    .setTextContent("Screen size: " + dimension.getWidth() + " x " + dimension.getHeight());
        });
    }

    @JsMethod
    public static WindowSizeExample bootstrap() {
        return new WindowSizeExample();
    }

    @JsMethod
    public void dispose() {
        _observer.dispose();
        Disposable.dispose(_inner);
    }

    @JsMethod
    public boolean isDisposed() {
        return _observer.isDisposed() && Disposable.isDisposed(_inner);
    }

    @JsProperty(name = "window", namespace = JsPackage.GLOBAL)
    private static native WindowSize.Window window();
}
