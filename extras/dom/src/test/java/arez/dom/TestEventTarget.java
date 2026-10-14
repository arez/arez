package arez.dom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TestEventTarget extends EventDrivenValue.EventTarget {

    private final Map<String, ArrayList<EventDrivenValue.EventListener>> _listeners = new HashMap<>();

    @Override
    void addEventListener(final String type, final EventDrivenValue.EventListener listener) {
        getEventListenersByType(type).add(listener);
    }

    @Override
    void removeEventListener(final String type, final EventDrivenValue.EventListener listener) {
        getEventListenersByType(type).remove(listener);
    }

    List<EventDrivenValue.EventListener> getEventListenersByType(final String type) {
        return _listeners.computeIfAbsent(type, t -> new ArrayList<>());
    }
}
