@file:Suppress("DEPRECATION")

package com.escalantedanny.candesk.auth.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.escalantedanny.candesk.BuildConfig
import com.escalantedanny.candesk.MainActivity
import com.escalantedanny.candesk.R
import com.escalantedanny.candesk.auth.fragments.LoginFragment
import com.escalantedanny.candesk.auth.fragments.LoginFragmentDirections
import com.escalantedanny.candesk.auth.fragments.SignUpFragment
import com.escalantedanny.candesk.databinding.ActivityLoginBinding
import com.escalantedanny.candesk.auth.models.User
import com.escalantedanny.candesk.auth.viewmodels.AuthViewModels
import com.escalantedanny.candesk.dogs.api.ApiResponseStatus
import com.escalantedanny.candesk.utils.showErrorDialog
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity(),
    LoginFragment.LoginFragmentActions,
    SignUpFragment.SingUpFragmentActions {

    private val viewModel: AuthViewModels by viewModels()

    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        @Suppress("DEPRECATION")
        progressBar = ProgressDialog(this)
        auth = FirebaseAuth.getInstance()

        viewModel.status.observe(this) {
            when (it) {
                is ApiResponseStatus.Error -> {
                    binding.loginProgress.visibility = View.GONE
                    Log.wtf("status", "ApiResponseStatus.Error")
                    showErrorDialog(it.messageId, this)
                }
                is ApiResponseStatus.Loading -> binding.loginProgress.visibility = View.VISIBLE
                is ApiResponseStatus.Success -> {
                    binding.loginProgress.visibility = View.GONE
                }
            }
        }

        viewModel.user.observe(this) { user ->
            if (user != null) {
                Log.wtf("REGISTER", "updateUserInfoAndGoHome" + user.user.authenticationToken)
                val usu = User(user.user.id, user.user.email, user.user.authenticationToken)
                User.setLoggedInUser(this, usu)
                updateUserInfoAndGoHome()
            }
        }
    }

    @SuppressLint("ResourceType")
    override fun onRegisterButtonClick() {
        Log.wtf("REGISTER", "Entro a registro")
        findNavController(R.id.nav_host_fragment)
            .navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
    }

    override fun onLoginButtonClick(email: String, password: String) {

        if (BuildConfig.ACTIVE_AUTH_FIREBASE) {
            @Suppress("DEPRECATION")
            progressBar.setMessage("Ingresando usuario...")
            progressBar.show()
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    progressBar.dismiss()
                    val firebaseUser = auth.currentUser

                    val user = User(
                        //id = firebaseUser!!.uid,
                        id = 0L,
                        email = firebaseUser?.email,
                        authenticationToken = firebaseUser?.email + "" + firebaseUser?.uid
                    )

                    User.setLoggedInUser(this, user)

                    updateUserInfoAndGoHome()
                }.addOnFailureListener {
                    progressBar.dismiss()
                    showErrorDialog(R.string.error_authentication, this)
                }
        } else {
            viewModel.signIn(email, password)
        }
    }

    override fun onSignUpFieldsValidated(
        email: String,
        password: String,
        passwordConfirmation: String,
        firstName: String,
        secondName: String
    ) {


        if (BuildConfig.ACTIVE_AUTH_FIREBASE) {

            FirebaseApp.initializeApp(this)
            val userFirebase = auth.currentUser
            progressBar.setMessage("Creando usuario...")
            progressBar.show()

            Log.wtf("USER", userFirebase?.uid.toString())
            if (userFirebase != null) {

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {

                        val user: FirebaseUser? = userFirebase

                        if (user != null)
                            verifyEmail(user)

                        updateUserInfoAndGoHome()

                    }.addOnFailureListener {
                        Toast.makeText(
                            this, "Error en la autenticación.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                progressBar.dismiss()
                showErrorDialog(R.string.error_desconocido, this)
            }
        } else {
            Log.wtf("REGISTER", "viewModel.signUp")
            viewModel.signUp(email, password, passwordConfirmation)
        }

    }

    private fun updateUserInfoAndGoHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        progressBar.hide()
        finish()
    }

    private fun verifyEmail(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Email " + user.getEmail(),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Error al verificar el correo ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


}