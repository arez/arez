package arez.dom.example;

import arez.Arez;
import arez.Disposable;
import arez.Observer;
import arez.dom.IdleStatus;
import jsinterop.annotations.JsMethod;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class IdleStatusExample {
    private final IdleStatus _model = IdleStatus.create();
    private final Observer _observer;

    private IdleStatusExample() {
        _observer = Arez.context().observer(() -> {
            final String message = "Interaction Status: " + (_model.isIdle() ? "Idle" : "Active");
            ExampleDom.element("#idle").setTextContent(message);
            ExampleDom.log(message);
        });
    }

    @JsMethod
    public static IdleStatusExample bootstrap() {
        return new IdleStatusExample();
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
