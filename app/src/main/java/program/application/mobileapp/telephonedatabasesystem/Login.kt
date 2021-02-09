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

        mLoginSystemConnection = FirebaseAuth.getInstance()
        mBtnRegister = findViewById(R.id.btn_register_login)
        mBtnLogin = findViewById(R.id.btn_login_login)
        mInputPassword = findViewById(R.id.input_password_login)
        mInputUsername = findViewById(R.id.input_email_login)
    }
    override fun onResume() {
        super.onResume()

        mBtnRegister.setOnClickListener { startActivity(Intent(applicationContext, Register::class.java)) }
        mBtnLogin.setOnClickListener {
            val textUsername = mInputUsername.text.toString().trim()
            val textPassword = mInputPassword.text.toString().trim()
            if (textUsername.isEmpty()){
                Toast.makeText(this,"กรุณาป้อน Email", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (textPassword.isEmpty()){
                Toast.makeText(this,"กรุณาป้อน Password",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            mLoginSystemConnection!!.signInWithEmailAndPassword( textUsername , textPassword ).addOnCompleteListener {
                if (!it.isSuccessful){
                    if (textPassword.length <= 8){
                        mInputPassword.error = ".ใส่รหัสผ่านมากกว่า 8 ตัวอักษร"
                    }else{
                        Toast.makeText(this,"Login ไม่สำเร็จ เนื่องจาก :"+it.exception!!.message, Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this,"Login สำเร็จแล้ว",Toast.LENGTH_LONG).show()
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}