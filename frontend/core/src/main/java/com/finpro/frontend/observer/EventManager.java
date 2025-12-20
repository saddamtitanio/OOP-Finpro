package com.finpro.frontend.observer;

import com.finpro.frontend.observer.listener.EventListener;

import java.util.*;

/**
 * Event System Documentation
 * How to create a new event:
 * 1. Create a new class in the event package.
 * 2. Create a listener in the listener package implementing EventListener<T> for the new event type
 * 3. Subscribe the listener to the event:
 *      eventManager.subscribe(CustomEvent.class, new CustomEventListener());
 *
 * 4. Trigger the event when needed:
 *      CustomEvent event = new CustomEvent(new Vector2(100, 200));
 *      eventManager.notify(event);
 *
 * The EventManager stores listeners in a map where the key is the event's class:
 *      listeners = {
 *          FireEvent.class -> [ShootingListener, ...],
 *          CustomEvent.class -> [CustomEventEventListener, ...]
 *      }
 */

public class EventManager {
    Map<Class<?>, List<EventListener<?>>> listeners = new HashMap<>();

    public <T> void subscribe(Class<T> eventType, EventListener<T> listener) {
        // Get the list of listeners for this event type
        List<EventListener<?>> listenerList = listeners.get(eventType);

        // If no list exists yet, create a new one and put it in the map
        if (listenerList == null) {
            listenerList = new ArrayList<>();
            listeners.put(eventType, listenerList);
        }

        listenerList.add(listener);
    }
    
    @SuppressWarnings("unchecked")
    public <T> void notify(T event) {
        List<EventListener<?>> list = listeners.get(event.getClass());
        if (list == null) return;

        for (EventListener<?> rawListener : list) {
            EventListener<T> listener = (EventListener<T>) rawListener;
            listener.update(event);
        }
    }

}
