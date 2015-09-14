package com.rowbot.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.concept2.api.rowbot.profile.Profile;
import com.rowbot.R;

import java.util.ArrayList;
import java.util.Arrays;

public final class StockImageUtils {
    public static final int[] STOCK_AVATAR_IMAGE_RES_IDS = new int[] { R.drawable.stock_avatar_0,
            R.drawable.stock_avatar_1, R.drawable.stock_avatar_2, R.drawable.stock_avatar_3,
            R.drawable.stock_avatar_4, R.drawable.stock_avatar_5, R.drawable.stock_avatar_6,
            R.drawable.stock_avatar_7, R.drawable.stock_avatar_8, R.drawable.stock_avatar_9,
            R.drawable.stock_avatar_10, R.drawable.stock_avatar_11,};

    public static int getStockImageResId(Profile profile) {
        return STOCK_AVATAR_IMAGE_RES_IDS[profile.getImageId()];
    }

    // Keeping this here because I might need this later.
//    public static Uri resourceToUri (Context context,int resID) {
//        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
//                context.getResources().getResourcePackageName(resID) + '/' +
//                context.getResources().getResourceTypeName(resID) + '/' +
//                context.getResources().getResourceEntryName(resID) );
//    }
}
