package com.example.hotpot.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.hotpot.AuthActivity
import com.example.hotpot.R
import com.google.android.material.button.MaterialButton

class LoginFragment : Fragment(R.layout.fragment_login) {
    lateinit var email : TextView
    lateinit var password : TextView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = view.findViewById(R.id.login_email)
        password = view.findViewById(R.id.login_password)

        val loginButton = view.findViewById<MaterialButton>(R.id.login_button)
        loginButton.setOnClickListener{
            (activity as AuthActivity).onLoginClicked(email.text.toString(), password.text.toString())
        }

        val switchToRegister = view.findViewById<TextView>(R.id.switch_to_register)
        switchToRegister.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.auth_fragment_container, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}