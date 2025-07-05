package com.eetrust.narwhal.runtime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Desc:
 *
 * @author lijt
 * Created on 2025/7/5
 * Email: tao351992257@gmail.com
 */
public class ActivityBuilder {
    public static final ActivityBuilder INSTANCE = new ActivityBuilder();

    public void startActivity(Context context, Intent intent) {
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

}
