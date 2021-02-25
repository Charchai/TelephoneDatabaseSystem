package program.application.mobileapp.telephonedatabasesystem.spare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class Spare : Fragment() {
    private var mLoginSystemConnection : FirebaseAuth? = null
    private val mFirebaseDatabaseConnection = FirebaseDatabase.getInstance()
    private lateinit var mBarnch : String
    private lateinit var mRcv : RecyclerView

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.spare_fragment, container, false)

        mLoginSystemConnection = FirebaseAuth.getInstance()
        mBarnch = resources.getString(R.string.branch)
        mRcv = root.findViewById(R.id.rcv_empty)
        mRcv.layoutManager = LinearLayoutManager(activity)
        mRcv.addItemDecoration(DividerItemDecoration(activity,0))
        val refSearchEmptyNumberDB = mFirebaseDatabaseConnection.getReference(mBarnch).orderByChild("nameForSearch").equalTo("Empty")
        refSearchEmptyNumberDB.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val getData = snapshot.children
                val arrayData2 = mutableListOf<KeepDataFromSnapshot2>()
                for ( data in getData){
                    val receiveEmpty = data.toString().substringAfter("phonenumberForName=").substringBefore(",").substringBefore("}").trim()
                    arrayData2.add(KeepDataFromSnapshot2(receiveEmpty))
                }
                mRcv.adapter = AdapterForRCV(arrayData2)


            }
            override fun onCancelled(error: DatabaseError) {
                showTextWhenErrorFromDataSnapshot(resources.getString(R.string.error_from_datasnapshot)+error.message)
            }
        })

        return root
    }
    class AdapterForRCV(private val arrayData: MutableList<KeepDataFromSnapshot2>) : RecyclerView.Adapter<ViewHolderForRCV2>() {
        override fun getItemCount(): Int = arrayData.size
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderForRCV2 {
            val rcvForNameFragment = LayoutInflater.from(parent.context).inflate(R.layout.rcv_for_empty,parent,false)
            return ViewHolderForRCV2(rcvForNameFragment)
        }
        override fun onBindViewHolder(holder: ViewHolderForRCV2, position: Int) {
            holder.showEmpty.text = arrayData[position].empty
        }
    }
    class ViewHolderForRCV2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val showEmpty : TextView = itemView.findViewById(R.id.show_empty)
    }
    class KeepDataFromSnapshot2(val empty: String)

    private fun showTextWhenErrorFromDataSnapshot(message: String){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }


}