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
    private lateinit var mSelectedTC : Spinner
    private lateinit var mInputRow : EditText
    private lateinit var mInputColumn : EditText
    private lateinit var mInputPhonePosition : EditText
    private lateinit var mBtnAddPosition : Button
    private var mBRANCH : String = ""
    private lateinit var mUserData : String
    private lateinit var mBtnDeleteLocation : Button
    private lateinit var mBtnDeletePosition : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data)

        mLoginSystemConnection = FirebaseAuth.getInstance() //สร้างการเชื่อมต่อกับ login-firebase-system
        createViewConnection()
        createListItemIntoCarbinetSpinner()
        updateLocationName()
    }
    private fun createListItemIntoCarbinetSpinner(){
        val arrayListOfSpinner = mutableListOf<String>()
        arrayListOfSpinner.add(resources.getString(R.string.carbinet_1))
        arrayListOfSpinner.add(resources.getString(R.string.carbinet_2))
        arrayListOfSpinner.add(resources.getString(R.string.carbinet_3))
        arrayListOfSpinner.add(resources.getString(R.string.carbinet_4))
        mSelectedTC.adapter = ArrayAdapter(baseContext,android.R.layout.simple_spinner_dropdown_item, arrayListOfSpinner)
    }

    override fun onResume() {
        super.onResume()

        mBtnAddLocation.setOnClickListener {
            addLocationName()
        }
        mBtnAddName.setOnClickListener {
            addName()
            }
        mBtnDeleteLocation.setOnClickListener {
            deleteLocationName()
        }
        mBtnAddPosition.setOnClickListener {
            addData()
        }
        mBtnDeletePosition.setOnClickListener {
            deleteData()
        }
    }
    private fun createViewConnection(){
        mInputLocation = findViewById(R.id.input_location_adddata)
        mBtnAddLocation = findViewById(R.id.btn_save_location_adddata)
        mInputPhone = findViewById(R.id.input_phone_adddata)
        mInputName = findViewById(R.id.input_name_adddata)
        mBtnAddName = findViewById(R.id.btn_save_name_adddata)
        mSelectedLocation = findViewById(R.id.spinner_location_adddata)
        mInputRow = findViewById(R.id.input_row_adddata)
        mInputColumn = findViewById(R.id.input_colomn_adddata)
        mInputPhonePosition = findViewById(R.id.input_phone_position_adddata)
        mSelectedTC = findViewById(R.id.spinner_location_addbox)
        mBtnAddPosition = findViewById(R.id.btn_save_position_adddata)
        mBtnDeleteLocation = findViewById(R.id.btn_deleteLocation)
        mBtnDeletePosition = findViewById(R.id.btn_deletePosition)
        mBRANCH  = resources.getString(R.string.branch)
    }
    private fun updateLocationName(){
        mUserData = mLoginSystemConnection!!.currentUser?.uid.toString()
        val refLocationDatabase = mFirebaseDatabaseConnection.getReference( mUserData ).child("setLocation")
        refLocationDatabase.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val getDataPack = snapshot.children
                val arrayListOfSpinner = mutableListOf<String>()
                for (data in getDataPack){
                    arrayListOfSpinner.add(data.toString().substringBefore(",").drop(20))
                }
                mSelectedLocation.adapter = ArrayAdapter(baseContext,android.R.layout.simple_spinner_dropdown_item, arrayListOfSpinner)
            }
            override fun onCancelled(error: DatabaseError) {
                showTextWhenErrorFromDataSnapshot(resources.getString(R.string.error_from_datasnapshot)+error.message)
            }
        })
    }
    private fun showTextWhenErrorFromDataSnapshot(message: String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
    private fun addLocationName(){
        val textLocation = mInputLocation.text.toString().trim()
        if(textLocation == ""){
            showText(resources.getString(R.string.text_when_not_enough_data))
            return
        }
        val refAddLocationDB = mFirebaseDatabaseConnection.getReference( mUserData ).child("setLocation").child(textLocation)
        refAddLocationDB.child("สถานะ").setValue("พร้อมใช้งาน")
        showText(resources.getString(R.string.text_when_saved)+textLocation)
    }
    private fun showText(text: String){
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
    }
    private fun addName(){
        val textPhone = mInputPhone.text.toString().trim()
        val textName = mInputName.text.toString().trim()
        if(textPhone == ""){
            showText(resources.getString(R.string.text_when_not_enough_data))
            return
        }
        if(textName == ""){
            showText(resources.getString(R.string.text_when_not_enough_data))
            return
        }
        val textNameAfterRecheck: String = if (textName == "*") "Empty" else textName
        val refSearchPhoneNumberByNameDB = mFirebaseDatabaseConnection.getReference( mBRANCH ).child("NameOF--$textPhone")
        refSearchPhoneNumberByNameDB.child("phonenumberForName").setValue(textPhone)
        refSearchPhoneNumberByNameDB.child("nameForSearch").setValue(textNameAfterRecheck)
        showText("บันทึกข้อมูลเรียบร้อย $textPhone : $textNameAfterRecheck")
    }
    private fun deleteLocationName(){
        val textLocation = mSelectedLocation.selectedItem.toString().trim()
        if(textLocation == ""){
            showText(resources.getString(R.string.text_when_not_enough_data))
            return
        }
        val refDeleteLocation = mFirebaseDatabaseConnection.getReference(mUserData).child("setLocation").child(textLocation)
        refDeleteLocation.child("สถานะ").removeValue()
        showText(resources.getString(R.string.text_when_deleted_data))
    }
    private fun addData(){
        val textLocationBox = mSelectedTC.selectedItem.toString().trim()
        val textLocation = mSelectedLocation.selectedItem.toString().trim()
        val textRow = mInputRow.text.toString().trim()
        val textColumn = mInputColumn.text.toString().trim()
        val textPhoneNumber = mInputPhonePosition.text.toString().trim()
        if(textLocationBox == ""){
            showText(resources.getString(R.string.text_when_not_enough_data))
            return
        }
        if(textLocation == ""){
            showText(resources.getString(R.string.text_when_not_enough_data))
            return
        }
        if(textRow == ""){
            showText(resources.getString(R.string.text_when_not_enough_data))
            return
        }
        if(textColumn == ""){
            showText(resources.getString(R.string.text_when_not_enough_data))
            return
        }
        if(textPhoneNumber == ""){
            showText(resources.getString(R.string.text_when_not_enough_data))
            return
        }


        val refSearchPositionByPhoneNumberDB = mFirebaseDatabaseConnection.getReference(mBRANCH).child(textPhoneNumber)
        when (textLocationBox) {
            "E" -> {
                refSearchPositionByPhoneNumberDB.child("rowLocationE").setValue(textRow)
                refSearchPositionByPhoneNumberDB.child("columnLocationE").setValue(textColumn)
                refSearchPositionByPhoneNumberDB.child("phonenumber").setValue(textPhoneNumber)
                refSearchPositionByPhoneNumberDB.child("ide").setValue(textRow+"-"+textColumn)
            }
            "H" -> {
                refSearchPositionByPhoneNumberDB.child("rowLocationH").setValue(textRow)
                refSearchPositionByPhoneNumberDB.child("columnLocationH").setValue(textColumn)
                refSearchPositionByPhoneNumberDB.child("phonenumber").setValue(textPhoneNumber)
                refSearchPositionByPhoneNumberDB.child("idh").setValue(textRow+"-"+textColumn)
            }
            "TC1" -> {
                refSearchPositionByPhoneNumberDB.child("rowLocationTC1").setValue(textRow)
                refSearchPositionByPhoneNumberDB.child("columnLocationTC1").setValue(textColumn)
                refSearchPositionByPhoneNumberDB.child("phonenumber").setValue(textPhoneNumber)
                refSearchPositionByPhoneNumberDB.child("locationTC1").setValue(textLocation)
                refSearchPositionByPhoneNumberDB.child("idtc1").setValue(textLocation+"-"+textRow+"-"+textColumn)
            }
            "TC2" -> {
                refSearchPositionByPhoneNumberDB.child("rowLocationTC2").setValue(textRow)
                refSearchPositionByPhoneNumberDB.child("columnLocationTC2").setValue(textColumn)
                refSearchPositionByPhoneNumberDB.child("phonenumber").setValue(textPhoneNumber)
                refSearchPositionByPhoneNumberDB.child("locationTC2").setValue(textLocation)
                refSearchPositionByPhoneNumberDB.child("idtc2").setValue(textLocation+"-"+textRow+"-"+textColumn)
            }
        }
        val refSearchNameByNumberDB = mFirebaseDatabaseConnection.getReference( mBRANCH ).orderByChild("phonenumberForName").equalTo(textPhoneNumber)
        refSearchNameByNumberDB.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = if (snapshot.value.toString() == "null"){""}else{ snapshot.value.toString().substringAfter("nameForSearch=").substringBefore(",").substringBefore("}").trim()}
                refSearchPositionByPhoneNumberDB.child("user").setValue(name)
            }
            override fun onCancelled(error: DatabaseError) {
                showTextWhenErrorFromDataSnapshot(resources.getString(R.string.error_from_datasnapshot)+error.message)
            }
        })
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
    private fun deleteData(){
        val textTC = mSelectedTC.selectedItem.toString().trim()
        val textLocation = mSelectedLocation.selectedItem.toString().trim()
        val textRow = mInputRow.text.toString().trim()
        val textColumn = mInputColumn.text.toString().trim()
        val textPhoneNumber = mInputPhonePosition.text.toString().trim()
        if(textTC == ""){
            showText(resources.getString(R.string.text_when_not_enough_data))
            return
        }
        if(textLocation == ""){
            showText(resources.getString(R.string.text_when_not_enough_data))
            return
        }
        if(textRow == ""){
            showText(resources.getString(R.string.text_when_not_enough_data))
            return
        }
        if(textColumn == ""){
            showText(resources.getString(R.string.text_when_not_enough_data))
            return
        }
        if(textPhoneNumber == ""){
            showText(resources.getString(R.string.text_when_not_enough_data))
            return
        }

        val refSearchPositionByPhoneNumberDB = mFirebaseDatabaseConnection.getReference(mBRANCH).child(textPhoneNumber)
        when (textTC) {
            "E" -> {
                refSearchPositionByPhoneNumberDB.child("rowLocationE").removeValue()
                refSearchPositionByPhoneNumberDB.child("columnLocationE").removeValue()
                refSearchPositionByPhoneNumberDB.child("ide").removeValue()
            }
            "H" -> {
                refSearchPositionByPhoneNumberDB.child("rowLocationH").removeValue()
                refSearchPositionByPhoneNumberDB.child("columnLocationH").removeValue()
                refSearchPositionByPhoneNumberDB.child("idh").removeValue()
            }
            "TC1" -> {
                refSearchPositionByPhoneNumberDB.child("rowLocationTC1").removeValue()
                refSearchPositionByPhoneNumberDB.child("columnLocationTC1").removeValue()
                refSearchPositionByPhoneNumberDB.child("locationTC1").removeValue()
                refSearchPositionByPhoneNumberDB.child("idtc1").removeValue()
            }
            "TC2" -> {
                refSearchPositionByPhoneNumberDB.child("rowLocationTC2").removeValue()
                refSearchPositionByPhoneNumberDB.child("columnLocationTC2").removeValue()
                refSearchPositionByPhoneNumberDB.child("locationTC2").removeValue()
                refSearchPositionByPhoneNumberDB.child("idtc2").removeValue()
            }
        }
        showText(resources.getString(R.string.text_when_deleted_data))
    }
}