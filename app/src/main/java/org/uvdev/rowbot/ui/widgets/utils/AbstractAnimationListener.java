package org.uvdev.rowbot.ui.widgets.utils;

import android.view.animation.Animation;

public class AbstractAnimationListener<T> implements Animation.AnimationListener {

    protected final T mData;

    public AbstractAnimationListener(T data) {
        mData = data;
    }

    @Override
    public void onAnimationStart(Animation animation) {}

    @Override
    public void onAnimationEnd(Animation animation) {}

    @Override
    public void onAnimationRepeat(Animation animation) {}
}
