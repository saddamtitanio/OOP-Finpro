package com.finpro.frontend.pool;
import com.badlogic.gdx.utils.Array;

public abstract class ObjectPool<T> {
    private final Array<T> available = new Array<>();
    private final Array<T> inUse = new Array<>();

    protected abstract T createObject();
    protected abstract void resetObject(T object);

    public T obtain() {
        if (available.isEmpty()) {
            available.add(createObject());
        }
        T object = available.removeIndex(available.size - 1);
        inUse.add(object);
        return object;
    }

    public void release(T object) {
        if (inUse.removeValue(object, true)) {
            resetObject(object);
            available.add(object);
        }
    }

    public void releaseAll() {
        for (int i = inUse.size - 1; i >= 0; i--) {
            T obj = inUse.get(i);
            resetObject(obj);
            available.add(obj);
        }
        inUse.clear();
    }

    public Array<T> getInUse() {
        return inUse;
    }

    public int getActiveCount() {
        return inUse.size;
    }


}
