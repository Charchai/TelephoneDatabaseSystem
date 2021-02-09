package program.application.mobileapp.telephonedatabasesystem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

        mLoginSystemConnection = FirebaseAuth.getInstance()
        if (mLoginSystemConnection!!.currentUser == null){
            startActivity(Intent(applicationContext, Login::class.java))
            finish()
        }

        mBtnSearch = findViewById(R.id.text_search_main)
        mBtnAddData = findViewById(R.id.text_adddata_main)
        mBtnLogout = findViewById(R.id.btn_logout_main)

    }
    override fun onResume() {
        super.onResume()

        mBtnLogout.setOnClickListener {
            mLoginSystemConnection!!.signOut()
            Toast.makeText(this,"Logout ออกจากระบบแล้ว", Toast.LENGTH_LONG).show()
            startActivity(Intent(this,Login::class.java))
            finish()
        }
        mBtnSearch.setOnClickListener { startActivity(Intent(applicationContext, SearchHost::class.java)) }
        mBtnAddData.setOnClickListener { startActivity(Intent(applicationContext, AddData::class.java)) }
    }
}