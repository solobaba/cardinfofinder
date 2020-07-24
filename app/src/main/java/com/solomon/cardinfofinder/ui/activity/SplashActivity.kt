package com.solomon.cardinfofinder.ui.activity

import android.content.Intent
import android.os.Bundle
import com.solomon.cardinfofinder.ui.base.BaseActivity

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, MainActivity::class.java))
    }
}