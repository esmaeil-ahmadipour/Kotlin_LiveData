package ir.ea2.kotlin_livedata.ui

import ir.ea2.kotlin_livedata.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import ir.ea2.kotlin_livedata.AppConstants
import ir.ea2.kotlin_livedata.NetworkUtil
import ir.ea2.kotlin_livedata.data.repository.AppRepository
import ir.ea2.kotlin_livedata.util.AppStatus
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        signupButton.setOnClickListener(View.OnClickListener {
            registerRequest(emailEditText.text.toString(),passwordEditText.text.toString())
        })
    }
    private fun registerRequest(email:String , password:String){
        if(NetworkUtil.isInternetAvailable(this)){
            AppRepository.getInstance().registerUser(email, password).observe(this, Observer { when(it.status){
                AppStatus.ERROR ->{
                    Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
                }
                AppStatus.SUCCESS->{
                    Toast.makeText(this,AppConstants.SUCCESSFUL_MESSAGE,Toast.LENGTH_LONG).show()
                }
            } })

        }else{
            Toast.makeText(this,AppConstants.DISCONNECT_INTERNET_MESSAGE,Toast.LENGTH_LONG).show()
        }
    }
}
