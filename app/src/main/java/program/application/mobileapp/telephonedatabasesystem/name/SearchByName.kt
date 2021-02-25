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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import program.application.mobileapp.telephonedatabasesystem.R

class SearchByName : Fragment() {
    private var mLoginSystemConnection : FirebaseAuth? = null
    private val mFirebaseDatabaseConnection = FirebaseDatabase.getInstance()
    private lateinit var mBranch : String
    private lateinit var mUserData : String
    private lateinit var mInputName : EditText
    private lateinit var mBtnSearch : Button
    private lateinit var mShowPhoneNumber : TextView
    private lateinit var mRcv : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.search_name_fragment, container, false)

        mLoginSystemConnection = FirebaseAuth.getInstance()
        mBranch = resources.getString(R.string.branch)
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
            searchDataAndShow()
        }
    }

    private fun searchDataAndShow(){
        val textName = mInputName.text.toString().trim()
        if(textName == ""){
            showText(resources.getString(R.string.text_when_not_enough_data))
            return
        }
        mRcv.layoutManager = LinearLayoutManager(activity)
        mRcv.addItemDecoration(DividerItemDecoration(activity,0))

        val refSearchPhoneNumberByNameDB = mFirebaseDatabaseConnection.getReference( mBranch ).orderByChild("nameForSearch").equalTo(textName)
        refSearchPhoneNumberByNameDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val textPhoneNumber = snapshot.value.toString().substringAfter("phonenumberForName=").substringBefore(",").substringBefore("}")
                mShowPhoneNumber.text = if (snapshot.value.toString() == "null"){resources.getString(R.string.text_when_not_found_number)}else{textPhoneNumber}
                val refSearchPositionByPhoneNumberDB = mFirebaseDatabaseConnection.getReference( mBranch ).orderByChild("phonenumber").equalTo(textPhoneNumber)
                refSearchPositionByPhoneNumberDB.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val getData = snapshot.children
                        val arrayData = mutableListOf<KeepDataFromSnapshot>()
                        for ( data in getData){
                            val textFromData = data.toString()
                            val rowLocationE = transferDataToString(textFromData,"rowLocationE")
                            val columnLocationE =transferDataToString(textFromData,"columnLocationE")
                            val rowLocationH =transferDataToString(textFromData,"rowLocationH")
                            val columnLocationH =transferDataToString(textFromData,"columnLocationH")
                            val locationTC1 =transferDataToString(textFromData,"locationTC1")
                            val rowLocationTC1 =transferDataToString(textFromData,"rowLocationTC1")
                            val columnLocationTC1 =transferDataToString(textFromData,"columnLocationTC1")
                            val locationTC2 =transferDataToString(textFromData,"locationTC2")
                            val rowLocationTC2 =transferDataToString(textFromData,"rowLocationTC2")
                            val columnLocationTC2 =transferDataToString(textFromData,"columnLocationTC2")
                            arrayData.add(KeepDataFromSnapshot(rowLocationE, columnLocationE, rowLocationH, columnLocationH, locationTC1, rowLocationTC1, columnLocationTC1, locationTC2, rowLocationTC2, columnLocationTC2))
                        }
                        mRcv.adapter = AdapterForRCV(arrayData)
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

    class AdapterForRCV(private val arrayData: MutableList<KeepDataFromSnapshot>) : RecyclerView.Adapter<ViewHolderForRCV>() {
        override fun getItemCount(): Int = arrayData.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderForRCV {
            val rcvForNameFragment = LayoutInflater.from(parent.context).inflate(R.layout.rcv_for_name,parent,false)
            return ViewHolderForRCV(rcvForNameFragment)
        }

        override fun onBindViewHolder(holder: ViewHolderForRCV, position: Int) {
            holder.showRowE.text = arrayData[position].rowLocationE
            holder.showColumnE.text = arrayData[position].columnLocationE
            holder.showRowH.text = arrayData[position].rowLocationH
            holder.showColumnH.text = arrayData[position].columnLocationH
            holder.showLocationTC1.text = arrayData[position].locationTC1
            holder.showRowTC1.text = arrayData[position].rowLocationTC1
            holder.showColumnTC1.text = arrayData[position].columnLocationTC1
            holder.showLocationTC2.text = arrayData[position].locationTC2
            holder.showRowTC2.text = arrayData[position].rowLocationTC2
            holder.showColumnTC2.text = arrayData[position].columnLocationTC2
        }
    }
    class ViewHolderForRCV(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val showRowE:TextView = itemView.findViewById(R.id.show_row_rcv_name)
        val showColumnE:TextView = itemView.findViewById(R.id.show_column_rcv_name)
        val showRowH:TextView = itemView.findViewById(R.id.show_row_rcv_nameH)
        val showColumnH:TextView = itemView.findViewById(R.id.show_column_rcv_name2)
        val showLocationTC1:TextView = itemView.findViewById(R.id.show_location_rcv_name3)
        val showRowTC1:TextView = itemView.findViewById(R.id.show_row_rcv_nameTC1)
        val showColumnTC1:TextView = itemView.findViewById(R.id.show_column_rcv_nameTC1)
        val showLocationTC2:TextView = itemView.findViewById(R.id.show_location_rcv_nameTC2)
        val showRowTC2:TextView = itemView.findViewById(R.id.show_row_rcv_nameTC2)
        val showColumnTC2:TextView = itemView.findViewById(R.id.show_column_rcv_nameTC2)
    }
    class KeepDataFromSnapshot(val rowLocationE: String, val columnLocationE: String, val rowLocationH: String
                               ,val columnLocationH:String,val locationTC1:String,val rowLocationTC1:String,val columnLocationTC1:String,val locationTC2:String
                               ,val rowLocationTC2:String,val columnLocationTC2:String)
    private fun transferDataToString(data:String,text:String):String{
        return if(data != "null"){
            if (data.substringAfter("$text=").substringBefore(",").substringBefore("}").startsWith("DataS")){
                ""
            }else{
                data.substringAfter("$text=").substringBefore(",").substringBefore("}")
            }
        }else{
            resources.getString(R.string.text_when_not_found_data)
        }
    }
    private fun showTextWhenErrorFromDataSnapshot(message: String){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
    private fun showText(text: String){
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
    }
}