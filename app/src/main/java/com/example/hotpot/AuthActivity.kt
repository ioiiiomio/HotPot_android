package com.example.hotpot


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hotpot.data.auth.login.LoginRepository
import com.example.hotpot.data.auth.login.LoginRequest
import com.example.hotpot.data.auth.login.LoginResult
import com.example.hotpot.data.auth.register.RegisterRepository
import com.example.hotpot.data.auth.register.RegisterRequest
import com.example.hotpot.data.auth.register.RegisterResult
import com.example.hotpot.fragments.LoginFragment
import com.example.hotpot.fragments.RegisterFragment
import com.example.hotpot.ui.activity.MainActivity
import com.prowheelxrassistv01.data.AppStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin


class AuthActivity : AppCompatActivity() {
    private val registerRepository: RegisterRepository by lazy { getKoin().get<RegisterRepository>() }
    private val loginRepository: LoginRepository by lazy { getKoin().get<LoginRepository>() }

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val appStorage: AppStorage by lazy { getKoin().get<AppStorage>()}

    val loginFragment = LoginFragment()
    val registerFragment = RegisterFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        setFragment(loginFragment)
    }

    fun setFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.auth_fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun onRegisterClicked(email: String, firstname: String, lastname: String, username: String, password: String){
        email.trim()
        firstname.trim()
        lastname.trim()
        username.trim()
        password.trim()
        if(email.isNotEmpty() && firstname.isNotEmpty() &&
            lastname.isNotEmpty() && username.isNotEmpty() &&
            password.isNotEmpty() && password.length>4 && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            viewModelScope.launch {
                val registerResult = registerRepository.register(RegisterRequest(email, username, firstname, lastname, password))
                if(registerResult is RegisterResult.Success){
                    setFragment(loginFragment)
                }else{
                    Toast.makeText(this@AuthActivity, "Error occurred while creating account", Toast.LENGTH_SHORT).show()
                }
            }
        }else {
            Toast.makeText(this, "Please, fill up the form correctly", Toast.LENGTH_SHORT).show()
        }
    }

    fun onLoginClicked(email: String, password: String){
        email.trim()
        password.trim()
        if(email.isNotEmpty() && password.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            viewModelScope.launch {
                val loginResult = loginRepository.login(LoginRequest(email, password))
                if(loginResult is LoginResult.Success){
                    redirectToMainActivity()
                    appStorage.savePassword(password)
                    appStorage.saveEmail(email)
                    appStorage.saveAccessToken(loginResult.accessToken)
                }else{
                    Toast.makeText(this@AuthActivity, "Error occurred while authorizing", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(this, "Please, fill up the form correctly", Toast.LENGTH_SHORT).show()
        }
    }
    fun redirectToMainActivity(){
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }

}