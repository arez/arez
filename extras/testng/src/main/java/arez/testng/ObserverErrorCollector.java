package arez.testng;

import arez.Observer;
import arez.ObserverError;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;

public final class ObserverErrorCollector {

    private final List<String> _observerErrors = new ArrayList<>();

    private final boolean _printExceptionStackTrace;

    public ObserverErrorCollector() {
        this(false);
    }

    public ObserverErrorCollector(final boolean printExceptionStackTrace) {
        _printExceptionStackTrace = printExceptionStackTrace;
    }

    public void clear() {
        _observerErrors.clear();
    }

    public List<String> getObserverErrors() {
        return _observerErrors;
    }

    void onObserverError(final Observer observer, final ObserverError error, @Nullable final Throwable throwable) {
        _observerErrors.add("Observer: " + observer.getName() + " Error: " + error + " " + throwable);
        if (_printExceptionStackTrace && null != throwable) {
            throwable.printStackTrace();
        }
    }
}
