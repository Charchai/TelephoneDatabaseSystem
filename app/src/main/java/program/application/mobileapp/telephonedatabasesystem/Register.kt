package program.application.mobileapp.telephonedatabasesystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {
    private var  mLoginSystemConnection : FirebaseAuth? = null
    private lateinit var mBtnRegister : Button
    private lateinit var mInputUsername : TextInputEditText
    private lateinit var mInputPassword : TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mLoginSystemConnection = FirebaseAuth.getInstance() //สร้างการเชื่อมต่อกับ login-firebase-system
        createViewConnection()
    }
    override fun onResume() {
        super.onResume()

        mBtnRegister.setOnClickListener {
            if (!checkEmptyText()){
                return@setOnClickListener
            }
            registerAndCheck() //ถ้าสมัครสำเร็จ จะย้ายไปหน้า main
        }
    }
    private fun createViewConnection(){
        mBtnRegister = findViewById(R.id.btn_register_register)
        mInputUsername = findViewById(R.id.input_email_register)
        mInputPassword = findViewById(R.id.input_password_register)
    }
    private fun showText(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
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
    private fun moveToMainActivity(){
        startActivity(Intent(this,MainActivity::class.java))
    }
    private fun registerAndCheck(){
        val textUsername = mInputUsername.text.toString().trim()
        val textPassword = mInputPassword.text.toString().trim()
        val textWhenRegisterFailedBecauseNumberOfPassword = resources.getString(R.string.text_when_register_failed_because_number_of_password)
        val textWhenRegisterFailedBecauseOther = resources.getString(R.string.text_when_register_failed_because_other)
        val textWhenRegisterSuccess = resources.getString(R.string.text_when_register_success)
        mLoginSystemConnection!!.createUserWithEmailAndPassword(textUsername,textPassword).addOnCompleteListener{
            if (!it.isSuccessful){
                if (textPassword.length <= 8){
                    mInputPassword.error = textWhenRegisterFailedBecauseNumberOfPassword
                }else{
                    //แสดงสาเหตุข้อผิดพลาดจากระบบ
                    Toast.makeText(this,textWhenRegisterFailedBecauseOther+it.exception!!.message, Toast.LENGTH_LONG).show()
                }
                makeInputIsEmpty()
            }else{
                showText(textWhenRegisterSuccess)
                moveToMainActivity()
                finish()
            }
        }
    }
    private fun makeInputIsEmpty(){
        mInputUsername.setText("")
        mInputPassword.setText("")
    }
}