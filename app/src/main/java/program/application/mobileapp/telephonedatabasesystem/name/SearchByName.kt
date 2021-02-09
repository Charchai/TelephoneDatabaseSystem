package program.application.mobileapp.telephonedatabasesystem.name

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import program.application.mobileapp.telephonedatabasesystem.R
import program.application.mobileapp.telephonedatabasesystem.number.SearchByNumber

class SearchByName : Fragment() {

//    private lateinit var searchByNameViewModel: SearchByNameViewModel
    private var mLoginSystemConnection : FirebaseAuth? = null
    private val mFirebaseDatabaseConnection = FirebaseDatabase.getInstance()
    private lateinit var mUserData : String
    private lateinit var mInputName : EditText
    private lateinit var mBtnSearch : Button
    private lateinit var mShowPhoneNumber : TextView
    private lateinit var mRcv : RecyclerView



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        searchByNameViewModel = ViewModelProvider(this).get(SearchByNameViewModel::class.java)
        val root = inflater.inflate(R.layout.search_name_fragment, container, false)
//        val textView: TextView = root.findViewById(R.id.text_dashboard)
//        searchByNameViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        mLoginSystemConnection = FirebaseAuth.getInstance()
        mUserData = mLoginSystemConnection!!.currentUser?.uid.toString()

        mInputName = root.findViewById(R.id.input_name_name_fragment)
        mBtnSearch = root.findViewById(R.id.btn_search_name_fragment)
        mShowPhoneNumber = root.findViewById(R.id.show_number_name_fragment)
        mRcv = root.findViewById(R.id.rcv_name_name_fragment)
        return root
    }


    override fun onResume() {
        super.onResume()



        mBtnSearch.setOnClickListener {
            val textName = mInputName.text.toString().trim()
            if(textName == ""){
                Toast.makeText(activity, "โปรดกรอกข้อมูล", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mRcv.layoutManager = LinearLayoutManager(activity)
            mRcv.addItemDecoration(DividerItemDecoration(activity,0))
            val refSearchPhoneNumberByNamerDB = mFirebaseDatabaseConnection.getReference( mUserData ).child("searchPhoneNumberByName").orderByKey().equalTo(textName)
            refSearchPhoneNumberByNamerDB.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val textPhoneNumber = snapshot.value.toString().substringAfter(textName+"=").dropLast(1)
                    mShowPhoneNumber.setText(if (snapshot.value.toString() == "null"){"ไม่พบหมายเลข"}else{textPhoneNumber})
                    val refSearchPositionByPhoneNumberDB = mFirebaseDatabaseConnection.getReference( mUserData ).child("searchPositionByPhoneNumber").child(textPhoneNumber)
                    refSearchPositionByPhoneNumberDB.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val getData = snapshot.children
                            val arrayData = mutableListOf<KeepDataFromSnapshot>()
                            for ( data in getData){
                                val receiveLocation = if(data.toString() != "null") data.toString().substringAfter("Location : ").substringBefore(" |") else "ไม่พบข้อมูล"
                                val receiveRow =if(data.toString() != "null")  data.toString().substringAfter("Row : ").substringBefore(" |") else "ไม่พบข้อมูล"
                                val receiveColumn = if(data.toString() != "null") data.toString().substringAfter("Column : ").substringBefore(",") else "ไม่พบข้อมูล"
                                val receiveOtherData = if(data.toString() != "null") data.toString().substringAfter("OtherData : ").dropLast(1) else "ไม่พบข้อมูล"
                                arrayData.add(KeepDataFromSnapshot(receiveLocation, receiveRow, receiveColumn, receiveOtherData))
                            }
                            mRcv.adapter = AdapterForRCV(arrayData)
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


        }
    }
    class AdapterForRCV(private val arrayData: MutableList<KeepDataFromSnapshot>) : RecyclerView.Adapter<ViewHolderForRCV>() {
        override fun getItemCount(): Int = arrayData.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderForRCV {
            val rcvForNameFragment = LayoutInflater.from(parent.context).inflate(R.layout.rcv_for_namefragment,parent,false)
            return ViewHolderForRCV(rcvForNameFragment)
        }

        override fun onBindViewHolder(holder: ViewHolderForRCV, position: Int) {
            holder.showLocation.text = arrayData[position].location
            holder.showRow.text = arrayData[position].row
            holder.showColumn.text = arrayData[position].column
            holder.showOtherData.text = arrayData[position].otherData
        }
    }

    class ViewHolderForRCV(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val showLocation : TextView = itemView.findViewById(R.id.show_location_rcv_namefragment)
        val showRow : TextView = itemView.findViewById(R.id.show_row_rcv_namefragment)
        val showColumn : TextView = itemView.findViewById(R.id.show_column_rcv_namefragment)
        val showOtherData : TextView = itemView.findViewById(R.id.show_otherdata_rcv_namefragment)
    }

    class KeepDataFromSnapshot(val location: String, val row: String, val column: String, val otherData: String)
}