package com.eetrust.narwhal.demo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.eetrust.annotations.Builder
import com.eetrust.annotations.Optional
import com.eetrust.narwhal.demo.databinding.ActivityMainBinding

/**
 * Desc:
 * @author lijt
 * Created on 2025/7/5
 * Email: tao351992257@gmail.com
 */
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnGoToUserActivity.setOnClickListener {
            UserActivityBuilder.startWithoutOptional(this, "jt")
        }

        binding.btnGoToParcelableActivity.setOnClickListener {
            startParcelableActivity(createTime = null, User(name = "cat", age = 1, sex = '母'))
        }
    }
}