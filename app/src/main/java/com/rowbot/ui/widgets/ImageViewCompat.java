package com.rowbot.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.rowbot.R;

public class ImageViewCompat extends ImageView {
    public ImageViewCompat(Context context) {
        super(context);
    }

    public ImageViewCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ImageViewCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray attrValues = getContext().getTheme().obtainStyledAttributes(attrs,
                R.styleable.ImageViewCompat, 0, 0);
        if (attrValues.hasValue(R.styleable.ImageViewCompat_tint)) {
            int tint = attrValues.getColor(R.styleable.ImageViewCompat_tint, Color.BLACK);
            Drawable src = getDrawable();
            if (src != null) {
                src = DrawableCompat.wrap(src);
                DrawableCompat.setTint(src, tint);
                DrawableCompat.setTintMode(src, PorterDuff.Mode.SRC_ATOP);
                setImageDrawable(src);
            }
        }
        attrValues.recycle();
    }
}
