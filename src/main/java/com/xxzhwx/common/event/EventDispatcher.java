package com.xxzhwx.common.event;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventDispatcher {
    private static final EventDispatcher I = new EventDispatcher();

    private final ConcurrentMap<EventType, List<EventListener>> eventListeners = new ConcurrentHashMap<>();

    private EventDispatcher() {
        for (EventType type : EventType.values()) {
            eventListeners.put(type, new CopyOnWriteArrayList<>());
        }
    }

    public static EventDispatcher getInstance() {
        return I;
    }

    public void registerEventListener(EventType type, EventListener listener) {
        List<EventListener> listeners = eventListeners.get(type);
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void raise(EventBase event) {
        List<EventListener> listeners = eventListeners.get(event.type);
        listeners.forEach(listener -> listener.onRaised(event));
    }
}
