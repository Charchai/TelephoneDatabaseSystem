package program.application.mobileapp.telephonedatabasesystem.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import program.application.mobileapp.telephonedatabasesystem.R

class SearchByLocation : Fragment() {
    private var mLoginSystemConnection : FirebaseAuth? = null
    private val mFirebaseDatabaseConnection = FirebaseDatabase.getInstance()
    private lateinit var mBranch : String
    private lateinit var mUserData : String
    private lateinit var mSelectedLocation : Spinner
    private lateinit var mSelectedBox : Spinner
    private lateinit var mInputRow : EditText
    private lateinit var mInputColumn : EditText
    private lateinit var mBtnSearch : Button
    private lateinit var mShowPhoneNumber : TextView
    private lateinit var mShowName : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.search_location_fragment, container, false)
        mLoginSystemConnection = FirebaseAuth.getInstance()
        mBranch = resources.getString(R.string.branch)
        mUserData = mLoginSystemConnection!!.currentUser?.uid.toString()
        mSelectedLocation = root.findViewById(R.id.spinner_location_location_fragment)
        mSelectedBox = root.findViewById(R.id.spinner_Box_location_fragment)
        mInputRow = root.findViewById(R.id.input_row_location_fragment)
        mInputColumn = root.findViewById(R.id.input_column_location_fragment)
        mBtnSearch = root.findViewById(R.id.btn_search_position_location_fragment)
        mShowPhoneNumber = root.findViewById(R.id.show_number_location_fragment)
        mShowName = root.findViewById(R.id.show_name_location_fragment)


        val arrayListBox = mutableListOf<String>()
        arrayListBox.add(resources.getString(R.string.carbinet_1))
        arrayListBox.add(resources.getString(R.string.carbinet_2))
        arrayListBox.add(resources.getString(R.string.carbinet_3))
        arrayListBox.add(resources.getString(R.string.carbinet_4))
        mSelectedBox.adapter = ArrayAdapter(root.context,android.R.layout.simple_spinner_dropdown_item, arrayListBox)

        val refLocationDatabase = mFirebaseDatabaseConnection.getReference( mUserData ).child("setLocation")
        refLocationDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.children
                val arrayList = mutableListOf<String>()
                for (count in data){
                    arrayList.add(count.toString().substringBefore(",").drop(20))
                }
                mSelectedLocation.adapter = ArrayAdapter(root.context,android.R.layout.simple_spinner_dropdown_item, arrayList)
            }
            override fun onCancelled(error: DatabaseError) {
                showTextWhenErrorFromDataSnapshot(resources.getString(R.string.error_from_datasnapshot)+error.message)
            }
        })

        return root
    }

    override fun onResume() {
        super.onResume()

        mBtnSearch.setOnClickListener {
            val textLocationBox = mSelectedBox.selectedItem.toString().trim()
            val textLocation = mSelectedLocation.selectedItem.toString().trim()
            val textRow = mInputRow.text.toString().trim()
            val textColumn = mInputColumn.text.toString().trim()
            if(textLocationBox == ""){
                showText(resources.getString(R.string.text_when_not_enough_data))
                return@setOnClickListener
            }
            if(textLocation == ""){
                showText(resources.getString(R.string.text_when_not_enough_data))
                return@setOnClickListener
            }
            if(textRow == ""){
                showText(resources.getString(R.string.text_when_not_enough_data))
                return@setOnClickListener
            }
            if(textColumn == ""){
                showText(resources.getString(R.string.text_when_not_enough_data))
                return@setOnClickListener
            }
            when (textLocationBox) {
                "E" -> {
                    val refPhoneNumberByLocation = mFirebaseDatabaseConnection.getReference(mBranch).orderByChild("ide").equalTo("$textRow-$textColumn")
                    refPhoneNumberByLocation.addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val textPhoneNumber = snapshot.value.toString().substringAfter("phonenumber=").substringBefore(",").substringBefore("}").trim()
                            mShowPhoneNumber.text = if (snapshot.value.toString() == "null"){resources.getString(R.string.text_when_not_found_number)}else{textPhoneNumber}

                            val refSearchNameByPhoneNumberDB = mFirebaseDatabaseConnection.getReference( mBranch ).orderByChild("phonenumberForName").equalTo(textPhoneNumber)
                            refSearchNameByPhoneNumberDB.addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    mShowName.text = if (snapshot.value.toString() == "null"){resources.getString(R.string.text_when_not_found_user)}else{ snapshot.value.toString().substringAfter("nameForSearch=").substringBefore(",").substringBefore("}").trim()}
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    showTextWhenErrorFromDataSnapshot(resources.getString(R.string.error_from_datasnapshot)+error.message)
                                }
                            })
                        }

                        override fun onCancelled(error: DatabaseError) {
                            showTextWhenErrorFromDataSnapshot(resources.getString(R.string.error_from_datasnapshot)+error.message)
                        }
                    })
                }
                "H" -> {
                    val refPhoneNumberByLocation = mFirebaseDatabaseConnection.getReference(mBranch).orderByChild("idh").equalTo("$textRow-$textColumn")
                    refPhoneNumberByLocation.addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val textPhoneNumber = snapshot.value.toString().substringAfter("phonenumber=").substringBefore(",").substringBefore("}").trim()
                            mShowPhoneNumber.text = if (snapshot.value.toString() == "null"){resources.getString(R.string.text_when_not_found_number)}else{textPhoneNumber}

                            val refSearchNameByPhoneNumberDB = mFirebaseDatabaseConnection.getReference( mBranch ).orderByChild("phonenumberForName").equalTo(textPhoneNumber)
                            refSearchNameByPhoneNumberDB.addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    mShowName.text = if (snapshot.value.toString() == "null"){resources.getString(R.string.text_when_not_found_user)}else{ snapshot.value.toString().substringAfter("nameForSearch=").substringBefore(",").substringBefore("}").trim()}
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    showTextWhenErrorFromDataSnapshot(resources.getString(R.string.error_from_datasnapshot)+error.message)
                                }
                            })
                        }

                        override fun onCancelled(error: DatabaseError) {
                            showTextWhenErrorFromDataSnapshot(resources.getString(R.string.error_from_datasnapshot)+error.message)
                        }
                    })
                }
                "TC1" -> {
                    val refPhoneNumberByLocation = mFirebaseDatabaseConnection.getReference(mBranch).orderByChild("idtc1").equalTo("$textLocation-$textRow-$textColumn")
                    refPhoneNumberByLocation.addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val textPhoneNumber = snapshot.value.toString().substringAfter("phonenumber=").substringBefore(",").substringBefore("}").trim()
                            mShowPhoneNumber.text = if (snapshot.value.toString() == "null"){resources.getString(R.string.text_when_not_found_number)}else{textPhoneNumber}

                            val refSearchNameByPhoneNumberDB = mFirebaseDatabaseConnection.getReference( mBranch ).orderByChild("phonenumberForName").equalTo(textPhoneNumber)
                            refSearchNameByPhoneNumberDB.addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    mShowName.text = if (snapshot.value.toString() == "null"){resources.getString(R.string.text_when_not_found_user)}else{ snapshot.value.toString().substringAfter("nameForSearch=").substringBefore(",").substringBefore("}").trim()}
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    showTextWhenErrorFromDataSnapshot(resources.getString(R.string.error_from_datasnapshot)+error.message)
                                }
                            })
                        }

                        override fun onCancelled(error: DatabaseError) {
                            showTextWhenErrorFromDataSnapshot(resources.getString(R.string.error_from_datasnapshot)+error.message)
                        }
                    })
                }
                "TC2" -> {
                    val refPhoneNumberByLocation = mFirebaseDatabaseConnection.getReference(mBranch).orderByChild("idtc2").equalTo("$textLocation-$textRow-$textColumn")
                    refPhoneNumberByLocation.addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val textPhoneNumber = snapshot.value.toString().substringAfter("phonenumber=").substringBefore(",").substringBefore("}").trim()
                            mShowPhoneNumber.text = if (snapshot.value.toString() == "null"){resources.getString(R.string.text_when_not_found_number)}else{textPhoneNumber}

                            val refSearchNameByPhoneNumberDB = mFirebaseDatabaseConnection.getReference( mBranch ).orderByChild("phonenumberForName").equalTo(textPhoneNumber)
                            refSearchNameByPhoneNumberDB.addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    mShowName.text = if (snapshot.value.toString() == "null"){resources.getString(R.string.text_when_not_found_user)}else{ snapshot.value.toString().substringAfter("nameForSearch=").substringBefore(",").substringBefore("}").trim()}
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    showTextWhenErrorFromDataSnapshot(resources.getString(R.string.error_from_datasnapshot)+error.message)
                                }
                            })
                        }

                        override fun onCancelled(error: DatabaseError) {
                            showTextWhenErrorFromDataSnapshot(resources.getString(R.string.error_from_datasnapshot)+error.message)
                        }
                    })
                }
            }

        }
    }
    private fun showTextWhenErrorFromDataSnapshot(message: String){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
    private fun showText(text: String){
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
    }
}