package arez.dom.example;

import arez.Arez;
import arez.ArezContext;
import arez.Disposable;
import arez.Observer;
import arez.dom.MediaQuery;
import jsinterop.annotations.JsMethod;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MediaQueryExample {
    private final MediaQuery _query = MediaQuery.create("(max-width: 600px)");
    private final Observer _statusObserver;
    private final Observer _queryObserver;
    private final Runnable _removeButton;

    private MediaQueryExample() {
        final ExampleDom.Element statusElement = ExampleDom.element("#status");
        final ExampleDom.Element queryElement = ExampleDom.element("#query");
        final ArezContext context = Arez.context();
        _statusObserver = context.observer(
                () -> statusElement.setTextContent("Screen size: " + (_query.matches() ? "Narrow" : "Wide")));
        _queryObserver = context.observer(() -> queryElement.setTextContent(_query.getQuery()));
        _removeButton = ExampleDom.listen(
                "#change-query",
                "click",
                e -> context.safeAction(() -> {
                    if (_query.getQuery().equals("(max-width: 600px)")) {
                        _query.setQuery("(max-width: 1200px)");
                    } else {
                        _query.setQuery("(max-width: 600px)");
                    }
                }));
    }

    @JsMethod
    public static MediaQueryExample bootstrap() {
        return new MediaQueryExample();
    }

    @JsMethod
    public void dispose() {
        _removeButton.run();
        _statusObserver.dispose();
        _queryObserver.dispose();
        Disposable.dispose(_query);
    }

    @JsMethod
    public boolean isDisposed() {
        return _statusObserver.isDisposed() && _queryObserver.isDisposed() && Disposable.isDisposed(_query);
    }
}
