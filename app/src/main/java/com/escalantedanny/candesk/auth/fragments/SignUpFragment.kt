package com.escalantedanny.candesk.auth.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.escalantedanny.candesk.utils.isValidEmail
import com.escalantedanny.candesk.utils.isValidPassword
import com.escalantedanny.candesk.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    interface SingUpsFragmentActions {
        fun onSignUpFieldsValidated(email: String, password: String, passwordConfirmation: String)
    }

    private lateinit var signUpFragmentActions: SingUpsFragmentActions

    override fun onAttach(context: Context) {
        super.onAttach(context)
        signUpFragmentActions = try {
            context as SingUpsFragmentActions
        } catch (e: java.lang.ClassCastException){
            throw java.lang.ClassCastException("$context debe ser implementado")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        setupSignUpButton()
        return binding.root
    }

    private fun setupSignUpButton() {
        binding.signUpButton.setOnClickListener {
            ValidateFields()

        }
    }

    private fun ValidateFields() {
        binding.emailInput.error = ""
        binding.passwordInput.error = ""
        binding.confirmPasswordInput.error = ""
        val email = binding.emailEdit.text.toString()
        val password = binding.passwordEdit.text.toString()
        val confirm = binding.confirmPasswordEdit.text.toString()

        if (!isValidEmail(email)) {
            binding.emailInput.error = "Email no es valido"
            return
        }

        if (!isValidPassword(password)) {
            binding.passwordInput.error = "Contraseña menor a 5 caracteres"
            return
        }

        if (!isValidPassword(confirm)) {
            binding.confirmPasswordInput.error = "Contraseña menor a 5 caracteres"
            return
        }

        if (password != confirm) {
            binding.passwordInput.error = "Contraseñas no son iguales"
            binding.confirmPasswordInput.error = "Contraseñas no son iguales"
            return
        }

        // llamar singUp
        signUpFragmentActions.onSignUpFieldsValidated(email, password, confirm)
    }

}