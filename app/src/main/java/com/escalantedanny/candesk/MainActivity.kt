package com.escalantedanny.candesk

import android.content.Intent
import android.os.Bundle
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
        auth = FirebaseAuth.getInstance()
        val user = User.getLoggedInUser(this)
        if (user == null) {
            openLoginActivity()
            return
        }else {
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

        binding.tlMain.text = getString(R.string.saludo_main, user.email)

        binding.ListDogs.setOnClickListener {
            openListAnimalsActivity()
        }

        binding.logout.setOnClickListener {
            User.logout(this)
            auth.signOut()
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