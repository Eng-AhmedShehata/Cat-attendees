package com.ashehata.catattendees.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ashehata.catattendees.R
import com.ashehata.catattendees.home.HomeActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO add auth
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}