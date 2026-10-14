package arez.dom.example;

import arez.Arez;
import arez.Disposable;
import arez.Observer;
import arez.dom.DocumentVisibility;
import jsinterop.annotations.JsMethod;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DocumentVisibilityExample {
    private final DocumentVisibility _model = DocumentVisibility.create();
    private final Observer _observer;

    private DocumentVisibilityExample() {
        _observer = Arez.context().observer(() -> {
            final String message = "Document Visibility: " + _model.getVisibility();
            ExampleDom.element("#visibility").setTextContent(message);
            ExampleDom.log(message);
        });
    }

    @JsMethod
    public static DocumentVisibilityExample bootstrap() {
        return new DocumentVisibilityExample();
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
