package com.example.chatapp

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class UserAdapter(val context: Context, val userList: ArrayList<Users>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textname = itemView.findViewById<TextView>(R.id.txt_name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.userlayout, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentuser = userList[position]

        holder.textname.text = currentuser.name

        holder.itemView.setOnClickListener{
            val intent = Intent(context,chatactivity::class.java)

            intent.putExtra("name",currentuser.name)
            intent.putExtra("uid",currentuser.uid)

            context.startActivity(intent)
        }
        var recevierroom: String? = null
        var senderroom: String? = null

        var reciveruid = currentuser.uid
        var senderuid = FirebaseAuth.getInstance().currentUser?.uid
        senderroom = reciveruid + senderuid
        recevierroom = senderuid + reciveruid
        holder.itemView.setOnLongClickListener{
            DeleteMessageFromBothSide.showAlertdialog(context,"Delete Chat",senderroom,recevierroom)
            return@setOnLongClickListener true
        }

    }



    override fun getItemCount(): Int {
        return userList.size
    }
}