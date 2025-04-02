package com.example.hotpot.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.hotpot.AuthActivity
import com.example.hotpot.R
import com.google.android.material.button.MaterialButton

class RegisterFragment : Fragment(R.layout.fragment_register) {
    private lateinit var email : TextView
    private lateinit var firstname : TextView
    private lateinit var lastname : TextView
    private lateinit var username : TextView
    private lateinit var password : TextView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email = view.findViewById(R.id.login_email)
        firstname = view.findViewById(R.id.login_firstname)
        lastname = view.findViewById(R.id.login_lastname)
        username = view.findViewById(R.id.login_username)
        password = view.findViewById(R.id.login_password)

        val registerButton = view.findViewById<MaterialButton>(R.id.register_button)
        registerButton.setOnClickListener{
            (activity as AuthActivity).onRegisterClicked(
                email.text.toString(),
                firstname.text.toString(),
                lastname.text.toString(),
                username.text.toString(),
                password.text.toString())
        }

        val switchToLogin = view.findViewById<TextView>(R.id.switch_to_login)
        switchToLogin.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.auth_fragment_container, LoginFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}