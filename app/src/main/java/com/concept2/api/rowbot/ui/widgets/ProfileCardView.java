package com.concept2.api.rowbot.ui.widgets;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import com.concept2.api.rowbot.R;

public class ProfileCardView extends CardView {

    public ProfileCardView(Context context) {
        super(context);
        init();
    }

    public ProfileCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProfileCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.profile_card, this);
    }
}
