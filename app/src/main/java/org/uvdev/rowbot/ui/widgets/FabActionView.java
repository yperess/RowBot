package org.uvdev.rowbot.ui.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.uvdev.rowbot.R;
import org.uvdev.rowbot.common.utils.ParcelableUtils;
import org.uvdev.rowbot.concept2api.utils.Objects;
import org.uvdev.rowbot.concept2api.utils.Preconditions;
import org.uvdev.rowbot.ui.widgets.utils.AbstractAnimationListener;

public class FabActionView extends LinearLayout {

    private TextView mDescription;
    private ImageView mIcon;
    private TextView mIconText;

    private int mCurrentIconResId = 0;
    private FabDataItem mCurrentDataItem = null;

    public FabActionView(Context context) {
        super(context);
        init(null);
    }

    public FabActionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FabActionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View root = inflate(getContext(), R.layout.fab_action_view, this);
        mDescription = (TextView) root.findViewById(R.id.description);
        mIcon = (ImageView) root.findViewById(R.id.icon);
        mIconText = (TextView) root.findViewById(R.id.image_text);

        if (attrs == null) {
            setFullSize(true);
            return;
        }

        TypedArray attrValues = getContext().getTheme().obtainStyledAttributes(attrs,
                R.styleable.FabActionView, 0, 0);

        boolean isFullSize = attrValues.getBoolean(R.styleable.FabActionView_fullSize, true);
        setFullSize(isFullSize);

        FabDataItem.Builder builder = new FabDataItem.Builder()
                .setIconResId(attrValues.getResourceId(R.styleable.FabActionView_src, 0));

        if (attrValues.hasValue(R.styleable.FabActionView_text)) {
            String text = attrValues.getString(R.styleable.FabActionView_text);
            if (text != null) {
                builder.setTitle(text);
            } else {
                int resId = attrValues.getResourceId(R.styleable.FabActionView_text, 0);
                builder.setTitle(resId);
            }
        }

        if (attrValues.hasValue(R.styleable.FabActionView_imageText)) {
            String text = attrValues.getString(R.styleable.FabActionView_imageText);
            if (text != null) {
                builder.setIconText(text);
            } else {
                int resId = attrValues.getResourceId(R.styleable.FabActionView_imageText, 0);
                builder.setIconText(resId);
            }
        }

        attrValues.recycle();
        setDataItem(builder.build(), false /* animate */, 0);
    }

    public void setFullSize(boolean fullSize) {
        Resources res = getContext().getResources();
        int margin = res.getDimensionPixelSize(fullSize ? R.dimen.fab_full_size_margin
                : R.dimen.fab_small_size_margin);
        ((MarginLayoutParams) mIcon.getLayoutParams()).setMargins(margin, 0, margin, 0);

        int size = res.getDimensionPixelSize(fullSize ? R.dimen.fab_full_size
                : R.dimen.fab_small_size);
        mIcon.getLayoutParams().width = size;
        mIcon.getLayoutParams().height = size;

        int padding = res.getDimensionPixelSize(fullSize ? R.dimen.fab_full_size_padding
                : R.dimen.fab_small_size_padding);
        mIcon.setPadding(padding, padding, padding, padding);

        int fontSize = res.getDimensionPixelSize(fullSize
                ? R.dimen.fab_full_size_image_text_font_size
                : R.dimen.fab_small_size_image_text_font_size);
        mIconText.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
    }

    public void setDataItem(final FabDataItem dataItem, boolean animate, int delayMs) {
        if (Objects.equals(mCurrentDataItem, dataItem)) {
            return;
        }
        if (mCurrentDataItem == null || !animate) {
            updateViews(dataItem);
            return;
        }

        final Animation fadeIn = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
        fadeOut.setAnimationListener(new AbstractAnimationListener(null) {
            @Override
            public void onAnimationEnd(Animation animation) {
                updateViews(dataItem);
                startAnimation(fadeIn);
            }
        });
        fadeOut.setStartOffset(delayMs);
        startAnimation(fadeOut);
    }

    private void updateViews(FabDataItem dataItem) {
        mCurrentDataItem = Preconditions.assertNotNull(dataItem);
        if (dataItem.titleResId > 0) {
            mDescription.setText(dataItem.titleResId);
        } else {
            mDescription.setText(dataItem.title);
        }
        if (dataItem.iconTextResId > 0) {
            mIconText.setText(dataItem.iconTextResId);
        } else {
            mIconText.setText(dataItem.iconText);
        }
        if (dataItem.iconResId != 0) {
            mIcon.setImageResource(dataItem.iconResId);
        } else {
            mIcon.setImageResource(android.R.color.transparent);
        }
    }

    public static final class FabDataItem  implements Parcelable {
        public final String title;
        public final int titleResId;
        public final int iconResId;
        public final String iconText;
        public final int iconTextResId;

        private FabDataItem(String title, int titleResId, int iconResId, String iconText,
                int iconTextResId) {
            this.title = title;
            this.titleResId = titleResId;
            this.iconResId = iconResId;
            this.iconText = iconText;
            this.iconTextResId = iconTextResId;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof FabDataItem)) {
                return false;
            }
            if (o == this) {
                return true;
            }

            FabDataItem other = (FabDataItem) o;
            return Objects.equals(title, other.title)
                    && Objects.equals(titleResId, other.titleResId)
                    && Objects.equals(iconResId, other.iconResId)
                    && Objects.equals(iconText, other.iconText)
                    && Objects.equals(iconTextResId, other.iconTextResId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, titleResId, iconResId, iconText, iconTextResId);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            ParcelableUtils.writeString(dest, title);
            dest.writeInt(titleResId);
            dest.writeInt(iconResId);
            ParcelableUtils.writeString(dest, iconText);
            dest.writeInt(iconTextResId);
        }

        public static final Parcelable.Creator<FabDataItem> CREATOR = new Creator<FabDataItem>() {
            @Override
            public FabDataItem createFromParcel(Parcel source) {
                String title = ParcelableUtils.readString(source);
                int titleResId = source.readInt();
                int iconResId = source.readInt();
                String iconText = ParcelableUtils.readString(source);
                int iconTextResId = source.readInt();
                return new FabDataItem(title, titleResId, iconResId, iconText, iconTextResId);
            }

            @Override
            public FabDataItem[] newArray(int size) {
                return new FabDataItem[size];
            }
        };

        public static final class Builder {
            private String mTitle = null;
            private int mTitleResId = 0;
            private int mIconResId = 0;
            private String mIconText = null;
            private int mIconTextResId = 0;

            public Builder setTitle(String title) {
                mTitle = title;
                mTitleResId = 0;
                return this;
            }

            public Builder setTitle(int restId) {
                mTitleResId = restId;
                mTitle = null;
                return this;
            }

            public Builder setIconResId(int resId) {
                mIconResId = resId;
                return this;
            }

            public Builder setIconText(String text) {
                mIconText = text;
                mIconTextResId = 0;
                return this;
            }

            public Builder setIconText(int resId) {
                mIconTextResId = resId;
                mIconText = null;
                return this;
            }

            public FabDataItem build() {
                return new FabDataItem(mTitle, mTitleResId, mIconResId, mIconText, mIconTextResId);
            }
        }
    }
}
