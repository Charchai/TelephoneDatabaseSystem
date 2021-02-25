package program.application.mobileapp.telephonedatabasesystem

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private var mLoginSystemConnection : FirebaseAuth? = null
    private lateinit var mBtnSearch : TextView
    private lateinit var mBtnAddData : TextView
    private lateinit var mBtnLogout : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkLoginStatus() //ถ้าไม่มีการ login จะย้ายไปหน้า login
        createViewConnection()
    }
    override fun onResume() {
        super.onResume()

        mBtnLogout.setOnClickListener { logout() }
        mBtnSearch.setOnClickListener { moveToSearchHost() }
        mBtnAddData.setOnClickListener { moveToAddData() }
    }
    private fun checkLoginStatus(){
        mLoginSystemConnection = FirebaseAuth.getInstance()
        if (mLoginSystemConnection!!.currentUser == null){ //ถ้าไม่มีข้อมูลการ login
            moveToLogin()
            finish()
        }
    }
    private fun createViewConnection(){
        mBtnSearch = findViewById(R.id.text_search_main)
        mBtnAddData = findViewById(R.id.text_adddata_main)
        mBtnLogout = findViewById(R.id.btn_logout_main)
    }
    private fun logout(){
        mLoginSystemConnection!!.signOut()
        showTextWhenLogout()
        moveToLogin()
        finish()
    }
    private fun moveToSearchHost(){
        startActivity(Intent(applicationContext, SearchHost::class.java))
    }
    private fun moveToAddData(){
        startActivity(Intent(applicationContext, AddData::class.java))
    }
    private fun moveToLogin(){
        startActivity(Intent(this, Login::class.java))
    }
    private fun showTextWhenLogout(){
        val textWhenLogout = resources.getString(R.string.text_when_logout)
        showText(textWhenLogout)
    }
    private fun showText(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}