package com.madlab.logindemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.facebook.login.LoginManager
import com.google.gson.Gson
import com.madlab.logindemo.model.UserData
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        userData = intent.getParcelableExtra("DATA")!!

        textViewEmail.text = userData.email
        textViewUsername.text = userData.name
        Glide.with(this).load(userData.profileUrl).into(imageViewProfile)

        buttonLogout.setOnClickListener {
            LoginManager.getInstance().logOut()
            client.signOut()
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }
}