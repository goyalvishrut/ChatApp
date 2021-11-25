package com.example.chatapp

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object DeleteMessageFromBothSide {
    private lateinit var mdbref: DatabaseReference


    fun showAlertdialog(context: Context, title: String, senderroomid: String?, recevierroomid: String?) {
        mdbref = FirebaseDatabase.getInstance().getReference()
        AlertDialog.Builder(context)
            .setTitle(title)
            .setNegativeButton("cancel",null)
            .setPositiveButton("Confirm Deletion"){_,_->
                DeleteMessageFromBothSide(senderroomid,recevierroomid)
            }.show()

    }

    fun DeleteMessageFromBothSide(senderroom: String?, recevierroom: String?,) {
        mdbref = FirebaseDatabase.getInstance().getReference()
        mdbref.child("chats").child(senderroom!!).child("messages").removeValue()
        mdbref.child("chats").child(recevierroom!!).child("messages").removeValue()
    }
}