package com.worldline.nicolaldi.util;

import android.content.Context;

import com.worldline.nicolaldi.R;

/**
 * @author Nicola Verbeeck
 */
public class DatabaseKeyMapper {

    public static int getStringResourceForKey(String key) {
        switch (key) {
            case "apple":
                return R.string.item_apple;
            case "banana":
                return R.string.item_banana;
            case "berry":
                return R.string.item_berry;
            case "grape":
                return R.string.item_grape;
            case "grapefruit":
                return R.string.item_grapefruit;
            case "lemon":
                return R.string.item_lemon;
            case "orange":
                return R.string.item_orange;
            case "pear":
                return R.string.item_pear;
            case "strawberry":
                return R.string.item_strawberry;
            case "watermelon":
                return R.string.item_watermelon;
        }
        return 0;
    }

    public static int getImageResourceForKey(String key) {
        switch (key) {
            case "apple":
                return R.drawable.apple;
            case "banana":
                return R.drawable.banana;
            case "berry":
                return R.drawable.berry;
            case "grape":
                return R.drawable.grape;
            case "grapefruit":
                return R.drawable.grapefruit;
            case "lemon":
                return R.drawable.lemon;
            case "orange":
                return R.drawable.orange;
            case "pear":
                return R.drawable.pear;
            case "strawberry":
                return R.drawable.strawberry;
            case "watermelon":
                return R.drawable.watermelon;
        }
        return 0;
    }

    public static String loadStringForKey(Context context, String key) {
        return context.getString(getStringResourceForKey(key));
    }

}
