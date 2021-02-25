package program.application.mobileapp.telephonedatabasesystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private var mLoginSystemConnection : FirebaseAuth? = null
    private lateinit var mBtnRegister : Button
    private lateinit var mBtnLogin : Button
    private lateinit var mInputPassword : TextInputEditText
    private lateinit var mInputUsername : TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mLoginSystemConnection = FirebaseAuth.getInstance() //สร้างการเชื่อมต่อกับ login-firebase-system
        createViewConnection()
    }
    override fun onResume() {
        super.onResume()

        mBtnRegister.setOnClickListener { moveToRegister() }
        mBtnLogin.setOnClickListener {
            if (!checkEmptyText()){
                return@setOnClickListener
            }
            loginAndShowResult() //ถ้า login สำเร็จ จะย้ายไปหน้า main
        }
    }
    private fun createViewConnection(){
        mBtnRegister = findViewById(R.id.btn_register_login)
        mBtnLogin = findViewById(R.id.btn_login_login)
        mInputPassword = findViewById(R.id.input_password_login)
        mInputUsername = findViewById(R.id.input_email_login)
    }
    private fun moveToRegister(){
        startActivity(Intent(applicationContext, Register::class.java))
    }
    private fun checkEmptyText(): Boolean{
        val textUsername = mInputUsername.text.toString().trim()
        val textPassword = mInputPassword.text.toString().trim()
        val textWhenNotFoundEmail = resources.getString(R.string.text_when_not_found_email)
        val textWhenNotFoundPassword = resources.getString(R.string.text_when_not_found_password)
        if (textUsername.isEmpty()){
            showText(textWhenNotFoundEmail)
            return false
        }
        if (textPassword.isEmpty()){
            showText(textWhenNotFoundPassword)
            return false
        }
        return true
    }
    private fun loginAndShowResult(){
        val textUsername = mInputUsername.text.toString().trim()
        val textPassword = mInputPassword.text.toString().trim()
        val textWhenLoginFailedBecauseNumberOfPassword = resources.getString(R.string.text_when_login_failed_because_number_of_password)
        val textWhenLoginFailedBecauseOther = resources.getString(R.string.text_when_login_failed_because_other)
        val textWhenLoginSuccess = resources.getString(R.string.text_when_login_success)
        mLoginSystemConnection!!.signInWithEmailAndPassword( textUsername , textPassword ).addOnCompleteListener {
            if (!it.isSuccessful){
                if (textPassword.length <= 8){
                    mInputPassword.error = textWhenLoginFailedBecauseNumberOfPassword
                }else{
                    //แสดงสาเหตุข้อผิดพลาดจากระบบ
                    Toast.makeText(this,textWhenLoginFailedBecauseOther+it.exception!!.message, Toast.LENGTH_LONG).show()
                }
            }else{
                showText(textWhenLoginSuccess)
                moveToMainActivity()
                finish()
            }
        }
    }
    private fun showText(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
    private fun moveToMainActivity(){
        startActivity(Intent(this,MainActivity::class.java))
    }
}