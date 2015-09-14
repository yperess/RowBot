package com.rowbot.utils;

import com.concept2.api.utils.Objects;

import java.util.Comparator;
import java.util.Observer;

public class Observable<T> extends java.util.Observable {

    private T mValue;
    private Comparator<T> mComparator = null;

    public synchronized Observable<T> setValue(T value) {
        boolean isSame = mComparator == null ? Objects.equals(mValue, value)
                : mComparator.compare(mValue, value) == 0;
        if (!isSame) {
            mValue = value;
            setChanged();
        }
        return this;
    }

    public synchronized T getValue() {
        return mValue;
    }

    public synchronized Observable<T> setComparator(Comparator<T> comparator) {
        mComparator = comparator;
        return this;
    }

    @Override
    public void addObserver(Observer observer) {
        super.addObserver(observer);
        synchronized (this) {
            if (mValue != null) {
                observer.update(this, mValue);
            }
        }
    }

    @Override
    public void notifyObservers() {
        notifyObservers(mValue);
    }
}
