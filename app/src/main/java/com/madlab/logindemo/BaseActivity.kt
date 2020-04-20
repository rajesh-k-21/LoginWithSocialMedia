package com.madlab.logindemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.madlab.logindemo.model.UserData

open class BaseActivity : AppCompatActivity() {

    companion object {
        const val REQ_CODE = 13
    }
    lateinit var callbackManager: CallbackManager
    lateinit var userData: UserData

    lateinit var client: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userData = UserData()
        callbackManager = CallbackManager.Factory.create()

        val gsi =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()

        client = GoogleSignIn.getClient(this, gsi)
    }
}
