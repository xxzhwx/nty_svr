package com.xxzhwx.common.event;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class EventSupport implements EventListener {
    private final Map<EventType, Object[]> eventHandlers = new HashMap<>(4);

    public EventSupport(Object handler) {
        for (Method method : handler.getClass().getDeclaredMethods()) {
            HandleEvent ann = method.getAnnotation(HandleEvent.class);
            if (ann != null ) {
                EventType[] types = ann.value();
                for (EventType type : types) {
                    EventDispatcher.getInstance().registerEventListener(type, this);
                    eventHandlers.put(type, new Object[]{method, handler});
                }
            }
        }
    }

    @Override
    public void onRaised(EventBase event) {
        Object[] handlers = eventHandlers.get(event.type);
        if (handlers != null) {
            Method method = (Method)handlers[0];
            try {
                method.invoke(handlers[1], event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
