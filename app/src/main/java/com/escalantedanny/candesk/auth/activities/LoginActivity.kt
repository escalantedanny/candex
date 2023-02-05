package com.escalantedanny.candesk.auth.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.findNavController
import com.escalantedanny.candesk.MainActivity
import com.escalantedanny.candesk.R
import com.escalantedanny.candesk.auth.fragments.LoginFragment
import com.escalantedanny.candesk.auth.fragments.LoginFragmentDirections
import com.escalantedanny.candesk.auth.fragments.SignUpFragment
import com.escalantedanny.candesk.databinding.ActivityLoginBinding
import com.escalantedanny.candesk.dogs.activities.DogListActivity
import com.escalantedanny.candesk.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import kotlinx.coroutines.awaitAll

class LoginActivity : AppCompatActivity(), LoginFragment.LoginFragmentActions, SignUpFragment.SingUpsFragmentActions {

    private lateinit var auth: FirebaseAuth
    private lateinit var  progressBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressBar = ProgressDialog(this)
        auth = FirebaseAuth.getInstance()
    }

    @SuppressLint("ResourceType")
    override fun onRegisterButtonClick() {
        Log.wtf("REGISTER", "Entro a registro")
        findNavController(R.id.nav_host_fragment)
            .navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
    }

    override fun onLoginButtonClick(email: String, password: String) {

        progressBar.setMessage("Ingresando usuario...")
        progressBar.show()
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressBar.dismiss()
                val firebaseUser = auth.currentUser

                val user = User(
                    id = firebaseUser!!.uid,
                    email = firebaseUser.email,
                    authenticationToken = firebaseUser.email+""+firebaseUser.uid)

                User.setLoggedInUser(this, user)

                updateUserInfoAndGoHome()
            }.addOnFailureListener{
                Toast.makeText(this, "Error en la autenticación.",
                    Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSignUpFieldsValidated(
        email: String,
        password: String,
        passwordConfirmation: String,
        firstName: String,
        secondName: String
    ) {
        FirebaseApp.initializeApp(this)

        val userFirebase = auth.currentUser
        progressBar.setMessage("Creando usuario...")
        progressBar.show()

        Log.wtf("USER", userFirebase?.uid.toString())

        if (userFirebase != null){

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this)  {

                    val user: FirebaseUser? = userFirebase

                    if (user != null) {
                        verifyEmail(user)
                    };



                    //val usuario = User(id = user!!.uid, email = user.email  )

                    //User.setLoggedInUser(this, )
                    updateUserInfoAndGoHome()

                }.addOnFailureListener{
                    Toast.makeText(this, "Error en la autenticación.",
                        Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "null.",
                Toast.LENGTH_SHORT).show()
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
            .addOnCompleteListener(this) {
                    task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,
                        "Email " + user.getEmail(),
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this,
                        "Error al verificar el correo ",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}