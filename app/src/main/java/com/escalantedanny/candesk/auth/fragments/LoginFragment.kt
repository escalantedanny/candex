package com.escalantedanny.candesk.auth.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.escalantedanny.candesk.databinding.FragmentLoginBinding
import com.escalantedanny.candesk.utils.isValidEmail
import com.escalantedanny.candesk.utils.isValidPassword

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    interface LoginFragmentActions {
        fun onRegisterButtonClick()
        fun onLoginButtonClick(email: String, password: String)
    }

    private lateinit var loginFragmentActions: LoginFragmentActions

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginFragmentActions = try {
            context as LoginFragmentActions
        } catch (e: java.lang.ClassCastException){
            throw java.lang.ClassCastException("$context debe ser implementado")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        binding.loginRegisterButton.setOnClickListener {
            loginFragmentActions.onRegisterButtonClick()
        }
        binding.loginButton.setOnClickListener {
            ValidateFields()

        }
        return binding.root
    }

    private fun ValidateFields() {

        binding.emailInput.error = ""
        binding.passwordInput.error = ""
        val email = binding.emailEdit.text.toString()
        val password = binding.passwordEdit.text.toString()

        if (!isValidEmail(email)) {
            binding.emailInput.error = "Email no es valido"
            return
        }

        if (!isValidPassword(password)) {
            binding.passwordInput.error = "Contraseña menor a 5 caracteres"
            return
        }

        loginFragmentActions.onLoginButtonClick(
            binding.emailEdit.text.toString(),
            binding.passwordEdit.text.toString()
        )
    }

}