package com.escalantedanny.candesk.auth.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.findNavController
import com.escalantedanny.candesk.R
import com.escalantedanny.candesk.auth.fragments.LoginFragment
import com.escalantedanny.candesk.auth.fragments.LoginFragmentDirections
import com.escalantedanny.candesk.auth.fragments.SignUpFragment
import com.escalantedanny.candesk.databinding.ActivityLoginBinding
import com.escalantedanny.candesk.dogs.activities.DogListActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity(), LoginFragment.LoginFragmentActions, SignUpFragment.SingUpsFragmentActions {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("ResourceType")
    override fun onRegisterButtonClick() {
        Log.wtf("REGISTER", "Entro a registro")
        findNavController(R.id.nav_host_fragment)
            .navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
    }

    override fun onSignUpFieldsValidated(
        email: String,
        password: String,
        passwordConfirmation: String
    ) {

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        databaseReference = database.reference.child("Users")

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){

                val user: FirebaseUser = auth.currentUser!!
                verifyEmail(user);
                val currentUserDb = databaseReference.child(user.uid)
                currentUserDb.child("firstName").setValue("firstName")
                currentUserDb.child("lastName").setValue("lastName")
                updateUserInfoAndGoHome()

            }.addOnFailureListener{
                Toast.makeText(this, "Error en la autenticación.",
                    Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserInfoAndGoHome() {
        val intent = Intent(this, DogListActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun verifyEmail(user: FirebaseUser) {
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