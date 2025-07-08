package com.eetrust.narwhal.demo;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eetrust.annotations.Builder;
import com.eetrust.annotations.Required;

/**
 * Desc:
 *
 * @author lijt
 * Created on 2025/7/5
 * Email: tao351992257@gmail.com
 */
@Builder
public class UserActivity extends AppCompatActivity {
    @Required
    String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserActivityBuilder.inject(this, savedInstanceState);
        Log.d("TAG", "onCreate: name = " + name);
    }
}
