package program.application.mobileapp.telephonedatabasesystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddData : AppCompatActivity() {
    private var mLoginSystemConnection : FirebaseAuth? = null
    private val mFirebaseDatabaseConnection = FirebaseDatabase.getInstance()
    private lateinit var mInputLocation : EditText
    private lateinit var mBtnAddLocation : Button
    private lateinit var mInputPhone : EditText
    private lateinit var mInputName : EditText
    private lateinit var mBtnAddName : Button
    private lateinit var mSelectedLocation : Spinner
    private lateinit var mInputRow : EditText
    private lateinit var mInputColumn : EditText
    private lateinit var mInputPhonePosition : EditText
    private lateinit var mInputOtherData : EditText
    private lateinit var mBtnAddPosition : Button
    private lateinit var mUserData : String
    private lateinit var mBtnDeleteLocation : Button
    private lateinit var mBtnDeletePosition : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data)

        mLoginSystemConnection = FirebaseAuth.getInstance()
        mUserData = mLoginSystemConnection!!.currentUser?.uid.toString()

        mInputLocation = findViewById(R.id.input_location_adddata)
        mBtnAddLocation = findViewById(R.id.btn_save_location_adddata)
        mInputPhone = findViewById(R.id.input_phone_adddata)
        mInputName = findViewById(R.id.input_name_adddata)
        mBtnAddName = findViewById(R.id.btn_save_name_adddata)
        mSelectedLocation = findViewById(R.id.spinner_location_adddata)
        mInputRow = findViewById(R.id.input_row_adddata)
        mInputColumn = findViewById(R.id.input_colomn_adddata)
        mInputPhonePosition = findViewById(R.id.input_phone_position_adddata)
        mInputOtherData = findViewById(R.id.input_otherdata_adddata)
        mBtnAddPosition = findViewById(R.id.btn_save_position_adddata)
        mBtnDeleteLocation = findViewById(R.id.btn_deleteLocation)
        mBtnDeletePosition = findViewById(R.id.btn_deletePosition)

        val refLocationDatabase = mFirebaseDatabaseConnection.getReference( mUserData ).child("setLocation")
        refLocationDatabase.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val getDataPack = snapshot.children
                val arrayListOfSpiner = mutableListOf<String>()
                for (data in getDataPack){
                    arrayListOfSpiner.add(data.toString().substringBefore(",").drop(20))
                }
                mSelectedLocation.adapter = ArrayAdapter(baseContext,android.R.layout.simple_spinner_dropdown_item, arrayListOfSpiner)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "เกิดข้อผิดพลาด : "+error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    override fun onResume() {
        super.onResume()

        mBtnAddLocation.setOnClickListener {
            val textLocation = mInputLocation.text.toString().trim()
            if(textLocation == ""){
                Toast.makeText(applicationContext, "โปรดกรอกข้อมูล", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val refAddLocationDB = mFirebaseDatabaseConnection.getReference( mUserData ).child("setLocation")
            refAddLocationDB.child(textLocation).child("สถานะ").setValue("พร้อมใช้งาน")
            Toast.makeText(applicationContext, "บันทึกข้อมูลเรียบร้อย : $textLocation", Toast.LENGTH_SHORT).show()
        }

        mBtnAddName.setOnClickListener {
            val textPhone = mInputPhone.text.toString().trim()
            val textName = mInputName.text.toString().trim()
            if(textPhone == ""){
                Toast.makeText(applicationContext, "โปรดกรอกข้อมูล", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(textName == ""){
                Toast.makeText(applicationContext, "โปรดกรอกข้อมูล", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val textNameAfterRecheck: String
            textNameAfterRecheck = if (textName == "*") "Empty" else textName
            val refSearchPhoneNumberByNameDB = mFirebaseDatabaseConnection.getReference( mUserData ).child("searchPhoneNumberByName")
            refSearchPhoneNumberByNameDB.child(textNameAfterRecheck).setValue(textPhone)
            val refSearchNameByPhoneNumberDB = mFirebaseDatabaseConnection.getReference( mUserData).child("searchNameByPhoneNumber")
            refSearchNameByPhoneNumberDB.child(textPhone).setValue(textNameAfterRecheck)
            Toast.makeText(applicationContext, "บันทึกข้อมูลเรียบร้อย $textPhone : $textNameAfterRecheck", Toast.LENGTH_SHORT).show()
        }
        mBtnDeleteLocation.setOnClickListener {
            val textLocation = mSelectedLocation.selectedItem.toString().trim()
            if(textLocation == ""){
                Toast.makeText(applicationContext, "โปรดกรอกข้อมูล", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val refDeleteLocation = mFirebaseDatabaseConnection.getReference(mUserData).child("setLocation").child(textLocation)
            refDeleteLocation.child("สถานะ").removeValue()
            Toast.makeText(applicationContext, "ลบข้อมูลเรียบร้อย", Toast.LENGTH_SHORT).show()
        }

        mBtnAddPosition.setOnClickListener {
            val textLocation = mSelectedLocation.selectedItem.toString().trim()
            val textRow = mInputRow.text.toString().trim()
            val textColumn = mInputColumn.text.toString().trim()
            val textPhoneNumber = mInputPhonePosition.text.toString().trim()
            val textOtherData = mInputOtherData.text.toString().trim()
            if(textLocation == ""){
                Toast.makeText(applicationContext, "โปรดกรอกข้อมูล", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(textRow == ""){
                Toast.makeText(applicationContext, "โปรดกรอกข้อมูล", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(textColumn == ""){
                Toast.makeText(applicationContext, "โปรดกรอกข้อมูล", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(textPhoneNumber == ""){
                Toast.makeText(applicationContext, "โปรดกรอกข้อมูล", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val refSearchPositionByPhoneNumberDB = mFirebaseDatabaseConnection.getReference(mUserData).child("searchPositionByPhoneNumber").child(textPhoneNumber)
            refSearchPositionByPhoneNumberDB.child("Location : $textLocation | Row : $textRow | Column : $textColumn").setValue("OtherData : $textOtherData")

            val refSearchPhoneNumberByLocationDB = mFirebaseDatabaseConnection.getReference( mUserData ).child("searchPhoneNumberByLocation")
            refSearchPhoneNumberByLocationDB.child("Location : $textLocation | Row : $textRow | Column : $textColumn").setValue("PhoneNumber : $textPhoneNumber | OtherData : $textOtherData")

            Toast.makeText(applicationContext, "บันทึกข้อมูลเรียบร้อย : $textPhoneNumber", Toast.LENGTH_SHORT).show()
            var intColumn = textColumn.toInt()
            var intRow = textRow.toInt()
            if (intColumn == 10){
                intColumn = 0
                intRow += 1
            }
            intColumn += 1
            mInputColumn.setText(intColumn.toString())
            mInputRow.setText(intRow.toString())
        }
        mBtnDeletePosition.setOnClickListener {
            val textLocation = mSelectedLocation.selectedItem.toString().trim()
            val textRow = mInputRow.text.toString().trim()
            val textColumn = mInputColumn.text.toString().trim()
            val textPhoneNumber = mInputPhonePosition.text.toString().trim()
            if(textLocation == ""){
                Toast.makeText(applicationContext, "โปรดกรอกข้อมูล", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(textRow == ""){
                Toast.makeText(applicationContext, "โปรดกรอกข้อมูล", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(textColumn == ""){
                Toast.makeText(applicationContext, "โปรดกรอกข้อมูล", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(textPhoneNumber == ""){
                Toast.makeText(applicationContext, "โปรดกรอกข้อมูล", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val refSearchPositionByPhoneNumberDB = mFirebaseDatabaseConnection.getReference(mUserData).child("searchPositionByPhoneNumber").child(textPhoneNumber)
            refSearchPositionByPhoneNumberDB.child("Location : $textLocation | Row : $textRow | Column : $textColumn").removeValue()
            Toast.makeText(applicationContext, "ลบข้อมูลเรียบร้อย", Toast.LENGTH_SHORT).show()
        }
    }
}