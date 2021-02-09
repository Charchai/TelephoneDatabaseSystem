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
    private lateinit var mUserData : String
    private lateinit var mSelectedLocation : Spinner
    private lateinit var mInputRow : EditText
    private lateinit var mInputColumn : EditText
    private lateinit var mBtnSearch : Button
    private lateinit var mShowPhoneNumber : TextView
    private lateinit var mShowName : TextView
    private lateinit var mShowOtherData : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.search_location_fragment, container, false)
        mLoginSystemConnection = FirebaseAuth.getInstance()
        mUserData = mLoginSystemConnection!!.currentUser?.uid.toString()
        mSelectedLocation = root.findViewById(R.id.spinner_location_location_fragment)
        mInputRow = root.findViewById(R.id.input_row_location_fragment)
        mInputColumn = root.findViewById(R.id.input_column_location_fragment)
        mBtnSearch = root.findViewById(R.id.btn_search_position_location_fragment)
        mShowPhoneNumber = root.findViewById(R.id.show_number_location_fragment)
        mShowName = root.findViewById(R.id.show_name_location_fragment)
        mShowOtherData = root.findViewById(R.id.show_otherdata_location_fragment)

        val refLocationDatabase = mFirebaseDatabaseConnection.getReference( mUserData ).child("setLocation")
        refLocationDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.children
                val arrayList = mutableListOf<String>()
                for (count in data){
                    arrayList.add(count.toString().substringBefore(",").drop(20))
                }
                val adapter = ArrayAdapter(root.context,android.R.layout.simple_spinner_dropdown_item, arrayList)
                mSelectedLocation.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        return root
    }

    override fun onResume() {
        super.onResume()

        mBtnSearch.setOnClickListener {
            val textLocation = mSelectedLocation.selectedItem.toString().trim()
            val textRow = mInputRow.text.toString().trim()
            val textColumn = mInputColumn.text.toString().trim()
            if(textLocation == ""){
                Toast.makeText(activity, "โปรดกรอกข้อมูล", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(textRow == ""){
                Toast.makeText(activity, "โปรดกรอกข้อมูล", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(textColumn == ""){
                Toast.makeText(activity, "โปรดกรอกข้อมูล", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val refPhoneNumberByLocation = mFirebaseDatabaseConnection.getReference(mUserData).child("searchPhoneNumberByLocation").child("Location : $textLocation | Row : $textRow | Column : $textColumn")
            refPhoneNumberByLocation.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val textPhoneNumber = snapshot.value.toString().substringAfter("PhoneNumber :").substringBefore("|").trim()
                    mShowPhoneNumber.setText(if (snapshot.value.toString() == "null"){"ไม่พบข้อมูล"}else{textPhoneNumber})
                    mShowOtherData.setText(if (snapshot.value.toString() == "null"){"ไม่พบข้อมูล"}else{snapshot.value.toString().substringAfter("OtherData : ").trim()})

                    val refSearchNameByPhoneNumberDB = mFirebaseDatabaseConnection.getReference( mUserData ).child("searchNameByPhoneNumber").orderByKey().equalTo(textPhoneNumber)
                    refSearchNameByPhoneNumberDB.addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            mShowName.setText(if (snapshot.value.toString() == "null"){"ไม่พบข้อมูล"}else{ snapshot.value.toString().substringAfter(textPhoneNumber+"=").dropLast(1)})
                        }
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}