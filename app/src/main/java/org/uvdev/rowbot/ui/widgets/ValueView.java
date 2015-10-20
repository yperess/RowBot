package org.uvdev.rowbot.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.uvdev.rowbot.R;

public class ValueView extends RelativeLayout {

    private static final String TAG = "ValueView";
    private static final boolean DBG = false;

    /** The title containing {@link TextView}. */
    private TextView mTitle;

    /** The value containing {@link TextView}. */
    private TextView mValue;

    /** The value containing {@link TextView}. */
    private TextView mUnits;

    public ValueView(Context context) {
        super(context);
        init(null /* attributeSet */);
    }

    public ValueView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public ValueView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        init(attributeSet);
    }

    /**
     * Set the title of this value view. If the title is {@code null} or empty then the title view
     * will be hidden.
     *
     * @param title The new title of this view.
     */
    public void setTitle(String title) {
        setValue(mTitle, title);
    }

    /**
     * @return The title string of this view or {@code null} if the title is invisible.
     */
    public String getTitle() {
        return getValue(mTitle);
    }

    /**
     * Set the value of this value view. If the value is {@code null} or empty the value view will
     * be hidden.
     *
     * @param value The new value of this view.
     */
    public void setValue(String value) {
        setValue(mValue, value);
    }

    /**
     * @return The value string of this view or {@code null} if the value is invisible.
     */
    public String getValue() {
        return mValue.getText().toString();
    }

    /**
     * Set the units of this value view. If the units are {@code null} or empty the units view will
     * be hidden.
     *
     * @param units The new units of this view.
     */
    public void setUnits(String units) {
        setValue(mUnits, units);
    }

    /**
     * @return The units string of this view or {@code null} if the units are invisible.
     */
    public String getUnits() {
        return mUnits.getText().toString();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        if (DBG) Log.d(TAG, "Height = " + height);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, height / 6.0f);
        mValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, height / 2.5f);
        mUnits.setTextSize(TypedValue.COMPLEX_UNIT_PX, height / 6.0f);
    }

    private void init(AttributeSet attributeSet) {
        inflate(getContext(), R.layout.widget_value, this);
        mTitle = (TextView) findViewById(R.id.title);
        mValue = (TextView) findViewById(R.id.value);
        mUnits = (TextView) findViewById(R.id.units);
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
                attributeSet, R.styleable.ValueView, 0, 0);
        try {
            setTitle(typedArray.getString(R.styleable.ValueView_titleText));
            setValue(typedArray.getString(R.styleable.ValueView_valueText));
            setUnits(typedArray.getString(R.styleable.ValueView_unitsText));
        } finally {
            typedArray.recycle();
        }
    }

    private void setValue(TextView view, String str) {
        if (TextUtils.isEmpty(str)) {
            view.setVisibility(GONE);
        } else {
            view.setText(str);
            view.setVisibility(VISIBLE);
        }
    }

    private String getValue(TextView view) {
        if (view.getVisibility() != VISIBLE) {
            return null;
        }
        return view.getText().toString();
    }
}
