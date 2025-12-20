package com.finpro.frontend.observer.listener;

public interface EventListener<T> {
    void update(T event);
}
