package program.application.mobileapp.telephonedatabasesystem.spare

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

class Spare : Fragment() {
//
//    companion object {
//        fun newInstance() = Spare()
//    }

//    private lateinit var viewModel: SpareViewModel
    private var mLoginSystemConnection : FirebaseAuth? = null
    private val mFirebaseDatabaseConnection = FirebaseDatabase.getInstance()
    private lateinit var mUserData : String
    private lateinit var mRcv : RecyclerView


    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.spare_fragment, container, false)

        mLoginSystemConnection = FirebaseAuth.getInstance()
        mUserData = mLoginSystemConnection!!.currentUser?.uid.toString()
        mRcv = root.findViewById(R.id.rcv_empty)
        mRcv.layoutManager = LinearLayoutManager(activity)
        mRcv.addItemDecoration(DividerItemDecoration(activity,0))
        val refSearchEmptyNumberDB = mFirebaseDatabaseConnection.getReference(mUserData).child("searchNameByPhoneNumber").orderByValue().equalTo("Empty")
        refSearchEmptyNumberDB.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val getData = snapshot.children
                val arrayData2 = mutableListOf<KeepDataFromSnapshot2>()
                for ( data in getData){
                    val receiveEmpty = data.toString().substringBefore(",").substringAfter("key =").trim()
                    arrayData2.add(KeepDataFromSnapshot2(receiveEmpty))
                }
                mRcv.adapter = AdapterForRCV(arrayData2)


            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
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
    override fun onResume() {
        super.onResume()

    }
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
////        val viewModel = ViewModelProvider(this).get(SpareViewModel::class.java)
//        // TODO: Use the ViewModel
//    }


}