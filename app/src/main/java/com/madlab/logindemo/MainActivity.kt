package com.madlab.logindemo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.facebook.*
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), View.OnClickListener {

    lateinit var signInButton: SignInButton
    lateinit var logoutButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FacebookSdk.sdkInitialize(this)

        logoutButton = findViewById(R.id.buttonLogoutGoogle)
        signInButton = findViewById(R.id.buttonLoginGoogle)
        signInButton.setSize(SignInButton.SIZE_STANDARD)

        buttonLoginWithFacebook.setOnClickListener(this)

        signInButton.setOnClickListener(this)

        logoutButton.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun getUserDetails(currentAccessToken: AccessToken?) {
        val graphRequest = GraphRequest.newMeRequest(
            currentAccessToken
        ) { `object`, _ ->
            val fname = `object`?.getString("first_name")
            val lname = `object`?.getString("last_name")
            val email = `object`?.getString("email")
            val id = `object`?.getString("id")
            val profileUrl =
                "https://graph.facebook.com/$id/picture?type=normal";

            userData.email = email
            userData.name = "$fname $lname"
            userData.profileUrl = profileUrl

            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("DATA", userData)
            startActivity(intent)
            finish()
        }
        val parameters = Bundle()
        parameters.putString("fields", "first_name,last_name,email,id")
        graphRequest.parameters = parameters
        graphRequest.executeAsync()
    }

    private fun signIn() {
        val signInIntent = client.signInIntent
        startActivityForResult(signInIntent, REQ_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE) {
            val data = GoogleSignIn.getSignedInAccountFromIntent(data)
            val result = data.getResult(ApiException::class.java)
            userData.email = result?.email.toString()
            userData.name = result?.displayName.toString()
            userData.profileUrl = result?.photoUrl.toString()

            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("DATA", userData)
            startActivity(intent)
            finish()

        }
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun setUpGoogleData(data: GoogleSignInAccount?) {
        userData.email = data?.email
        userData.name = data?.displayName
        userData.profileUrl = data?.photoUrl.toString()

        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("DATA", userData)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        val lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (lastSignedInAccount != null) {
            setUpGoogleData(lastSignedInAccount)
        }
        if (AccessToken.isCurrentAccessTokenActive()) {
            getUserDetails(AccessToken.getCurrentAccessToken())
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonLogoutGoogle -> {
                if (GoogleSignIn.getLastSignedInAccount(this) != null) {
                    val signOut = client.signOut()
                    signOut.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Logout Successful", Toast.LENGTH_LONG).show()
                        }

                    }
                }
            }
            R.id.buttonLoginGoogle -> {
                if (GoogleSignIn.getLastSignedInAccount(this) == null) {
                    signIn()
                } else {
                    Toast.makeText(this, "already login", Toast.LENGTH_LONG).show()
                }
            }
            R.id.buttonLoginWithFacebook -> {
                LoginManager.getInstance()
                    .logInWithReadPermissions(this, listOf("email", "public_profile"))

                buttonLoginWithFacebook.registerCallback(callbackManager,
                    object : FacebookCallback<LoginResult> {
                        override fun onSuccess(result: LoginResult?) {
                            getUserDetails(AccessToken.getCurrentAccessToken())
                        }

                        override fun onCancel() {
                        }

                        override fun onError(error: FacebookException?) {
                        }
                    })
            }
        }
    }
}