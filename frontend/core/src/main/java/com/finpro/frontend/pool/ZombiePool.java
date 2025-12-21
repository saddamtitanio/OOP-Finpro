package com.finpro.frontend.pool;

import java.util.ArrayList;
import java.util.List;

public abstract class ZombiePool<T> {
    private final List<T> available = new ArrayList<>();
    private final List<T> inUse = new ArrayList<>();

    protected abstract T createZombie();
    protected abstract void resetZombie(T zombie);

    public T obtain() {
        T object;
        if (available.isEmpty()) {
            object = createZombie();
        } else {
            object = available.remove(available.size() - 1);
        }
        inUse.add(object);
        return object;
    }

    public void release(T object) {
        if (inUse.remove(object)) {
            resetZombie(object);
            available.add(object);
        }
    }

    public void releaseAll() {
        for (T object : inUse) {
            resetZombie(object);
            available.add(object);
        }
        inUse.clear();
    }

    public List<T> getInUse() {
        return new ArrayList<>(inUse);
    }

    public int getActiveCount() {
        return inUse.size();
    }
}
