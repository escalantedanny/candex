package com.escalantedanny.candesk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.escalantedanny.candesk.auth.activities.LoginActivity
import com.escalantedanny.candesk.databinding.ActivityMainBinding
import com.escalantedanny.candesk.dogs.activities.DogListActivity
import com.escalantedanny.candesk.auth.models.User
import com.escalantedanny.candesk.responses.ApiServiceInterceptor
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val user = User.getLoggedInUser(this)

        Log.wtf("USER_SHAREDPREFERENCE", user.toString())

        if (BuildConfig.ACTIVE_AUTH_FIREBASE) {
            if (user == null) {
                openLoginActivity()
                return
            } else {
                auth = FirebaseAuth.getInstance()
                auth.getAccessToken(true)
                    .addOnCompleteListener(OnCompleteListener<GetTokenResult?> { task ->
                        if (task.isSuccessful) {
                            val idToken: String? = task.result.token
                            ApiServiceInterceptor.setSessionToken(sessionToken = idToken!!)
                        } else {
                            task.exception
                        }
                    })
            }
        } else {
            if (user != null) {
                ApiServiceInterceptor.setSessionToken(sessionToken = user.authenticationToken)
            }
        }

        binding.tlMain.text = getString(R.string.saludo_main, "Hi")

        binding.ListDogs.setOnClickListener {
            openListAnimalsActivity()
        }

        binding.logout.setOnClickListener {

            if (BuildConfig.ACTIVE_AUTH_FIREBASE) {
                auth.signOut()
            } else {
                User.logout(this)
            }
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun openListAnimalsActivity() {
        startActivity(Intent(this, DogListActivity::class.java))
    }

    private fun openLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}