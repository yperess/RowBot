package com.rowbot.ui.widgets;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;

import com.rowbot.R;

public class PrSummaryCardView extends CardView {

    public PrSummaryCardView(Context context) {
        super(context);
        init();
    }

    public PrSummaryCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PrSummaryCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View root = inflate(getContext(), R.layout.pr_summary_card, this);
    }
}
