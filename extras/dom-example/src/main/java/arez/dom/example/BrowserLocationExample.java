package arez.dom.example;

import arez.Arez;
import arez.ArezContext;
import arez.Disposable;
import arez.Observer;
import arez.dom.BrowserLocation;
import jsinterop.annotations.JsMethod;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class BrowserLocationExample {
    private final BrowserLocation _location = BrowserLocation.create();
    private final Observer _cleanObserver;
    private final Observer _printObserver;
    private final Runnable _removeBase;
    private final Runnable _removeSlash;
    private final Runnable _removeEvent;
    private final Runnable _removeOther;

    private BrowserLocationExample() {
        final ArezContext context = Arez.context();
        _cleanObserver = context.observer(
                () -> cleanLocation(_location), Observer.Flags.READ_WRITE | Observer.Flags.NESTED_ACTIONS_ALLOWED);
        _printObserver = context.observer(() -> printBrowserLocation(_location));
        _removeBase = ExampleDom.listen("#route_base", "click", e -> _location.changeLocation(""));
        _removeSlash = ExampleDom.listen("#route_slash", "click", e -> _location.changeLocation("/"));
        _removeEvent = ExampleDom.listen("#route_event", "click", e -> _location.changeLocation("/event"));
        _removeOther = ExampleDom.listen("#route_other", "click", e -> _location.changeLocation("/other"));
    }

    @JsMethod
    public static BrowserLocationExample bootstrap() {
        return new BrowserLocationExample();
    }

    @JsMethod
    public void dispose() {
        _removeBase.run();
        _removeSlash.run();
        _removeEvent.run();
        _removeOther.run();
        _cleanObserver.dispose();
        _printObserver.dispose();
        Disposable.dispose(_location);
    }

    @JsMethod
    public boolean isDisposed() {
        return _cleanObserver.isDisposed() && _printObserver.isDisposed() && Disposable.isDisposed(_location);
    }

    private static void cleanLocation(final BrowserLocation location) {
        final String browserLocation = location.getBrowserLocation();
        if (isValid(browserLocation)) {
            location.changeLocation(browserLocation);
        } else if (isValid(location.getLocation())) {
            location.resetBrowserLocation();
        } else {
            location.changeLocation("");
        }
    }

    private static boolean isValid(final String location) {
        return "/event".equals(location) || "/other".equals(location) || "/".equals(location) || "".equals(location);
    }

    private static void printBrowserLocation(final BrowserLocation location) {
        emitLocation("#browser_location", "Browser Location: " + location.getBrowserLocation());
        emitLocation("#app_location", "Application Location: " + location.getLocation());
    }

    private static void emitLocation(final String selector, final String message) {
        ExampleDom.element(selector).setTextContent(message);
        ExampleDom.log(message);
    }
}
