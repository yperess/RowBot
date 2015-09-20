package com.rowbot.ui.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rowbot.R;

public class ImageRailView extends HorizontalScrollView implements View.OnClickListener {

    private LinearLayout mContainer;
    private int mSelectedImage = -1;
    private boolean mInTouchEvent = false;

    private int mImageSize;
    private int mMargins;
    private int mGrowSize;
    private int mSecondaryGrowSize;
    private float[] mAlpha = new float[3];

    // Inferred values.
    private int mSecondaryPadding;
    private int mImageWidth;

    public ImageRailView(Context context) {
        super(context);
        init(null);
    }

    public ImageRailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ImageRailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray attrValues = getContext().getTheme().obtainStyledAttributes(attrs,
                R.styleable.ImageRailView, 0, 0);
        int imagesArrayResId = attrValues.getResourceId(R.styleable.ImageRailView_images, 0);
        attrValues.recycle();

        View root = inflate(getContext(), R.layout.widget_image_rail, this);
        mContainer = (LinearLayout) root.findViewById(R.id.container);

        mImageSize = getResources().getDimensionPixelSize(R.dimen.profile_edit_image_size);
        mMargins = getResources().getDimensionPixelSize(R.dimen.profile_edit_image_margin);
        mGrowSize = getResources().getDimensionPixelSize(R.dimen.profile_edit_image_grow_size);
        mSecondaryGrowSize = getResources().getDimensionPixelSize(
                R.dimen.profile_edit_image_secondary_grow_size);

        mSecondaryPadding = mGrowSize - mSecondaryGrowSize;
        mImageWidth = mImageSize + ((mGrowSize + mMargins) * 2);

        TypedValue value = new TypedValue();
        getResources().getValue(R.dimen.stock_image_primary_alpha, value, false);
        mAlpha[0] = value.getFloat();
        getResources().getValue(R.dimen.stock_image_secondary_alpha, value, false);
        mAlpha[1] = value.getFloat();
        getResources().getValue(R.dimen.stock_image_base_alpha, value, false);
        mAlpha[2] = value.getFloat();

        if (imagesArrayResId != 0) {
            int[] images = getResources().getIntArray(imagesArrayResId);
            if (images != null) {
                setImages(images);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        final int padding = (getWidth() - mImageWidth) / 2;
        if (mContainer.getPaddingLeft() != padding) {
            mContainer.setPadding(padding /* left */, 0 /* top */, padding /* right */,
                    0 /* bottom */);
        }
        scrollToSelectedImage();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int scrollX = getScrollX();
        int imageId = (scrollX + (mImageWidth / 2)) / mImageWidth;
        if (event.getAction() == MotionEvent.ACTION_UP
                || event.getAction() == MotionEvent.ACTION_CANCEL) {
            mInTouchEvent = false;
            setSelectedImage(imageId, true /* scroll */);
            return true;
        } else {
            mInTouchEvent = true;
        }
        setSelectedImage(imageId, false /* scroll */);
        return super.onTouchEvent(event);
    }

    public void setImages(int... resIds) {
        Resources res = getResources();
        mContainer.removeAllViews();
        for (int i = 0; i < resIds.length; ++i) {
            ImageView view = new ImageView(getContext());
            modifyStockAvatarImage(view, mImageSize, mGrowSize, mAlpha[2]);
            view.setImageResource(resIds[i]);
            view.setOnClickListener(this);
            view.setTag(i);
            mContainer.addView(view);
        }
    }

    public void setSelectedImage(int index) {
        setSelectedImage(index, true /* scroll */);
    }

    public int getSelectedImage() {
        return mSelectedImage;
    }

    private void setSelectedImage(int index, boolean scroll) {
        if (mSelectedImage != -1 && mSelectedImage != index) {
            // Clear the old selected image.
            modifyStockAvatarImage(mSelectedImage, mImageSize, mGrowSize /* padding */, mAlpha[2]);
            modifyStockAvatarImage(mSelectedImage - 1, mImageSize, mGrowSize /* padding */,
                    mAlpha[2]);
            modifyStockAvatarImage(mSelectedImage + 1, mImageSize, mGrowSize /* padding */,
                    mAlpha[2]);
        }

        modifyStockAvatarImage(index, mImageSize + (mGrowSize * 2), 0 /* padding */, mAlpha[0]);
        modifyStockAvatarImage(index - 1, mImageSize + (mSecondaryGrowSize * 2), mSecondaryPadding,
                mAlpha[1]);
        modifyStockAvatarImage(index + 1, mImageSize + (mSecondaryGrowSize * 2), mSecondaryPadding,
                mAlpha[1]);
        mSelectedImage = index;

        if (scroll) {
            scrollToSelectedImage();
        }
    }

    private void scrollToSelectedImage() {
        if (mSelectedImage == -1 || mInTouchEvent) {
            return;
        }
        View view = mContainer.getChildAt(mSelectedImage);
        int center = (view.getLeft()+ view.getRight()) / 2;
        final int scrollTo = center - mContainer.getPaddingLeft() - (mImageWidth / 2);
        post(new Runnable() {
            @Override
            public void run() {
                smoothScrollTo(scrollTo, 0);
            }
        });
    }

    private void modifyStockAvatarImage(int index, int size, int padding, float alpha) {
        if (index < 0 || index >= mContainer.getChildCount()) {
            return;
        }
        modifyStockAvatarImage((ImageView) mContainer.getChildAt(index), size, padding, alpha);
    }

    private void modifyStockAvatarImage(ImageView view, int size, int padding, float alpha) {
        MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
        if (params == null) {
            params = new MarginLayoutParams(size, size);
        } else {
            params.height = params.width = size;
        }
        params.setMargins(mMargins * 2, mMargins, mMargins * 2, mMargins);
        view.setLayoutParams(params);
        view.setPadding(padding, padding, padding, padding);
        view.setAlpha(alpha);
    }

    @Override
    public void onClick(View v) {
        setSelectedImage((int) v.getTag(), true /* scroll */);
    }
}
