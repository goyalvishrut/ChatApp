package com.example.chatapp

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.ContextCompat.startForegroundService
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

object DeleteUser {
    private lateinit var mdbref: DatabaseReference

    fun showAlertdialog(context:Context,title: String, userUid: String, user: FirebaseUser) {
        mdbref = FirebaseDatabase.getInstance().getReference()
        AlertDialog.Builder(context)
            .setTitle(title)
            .setNegativeButton("cancel",null)
            .setPositiveButton("Confirm Deletion"){_,_->
                DeleteUser(context,userUid,user)
            }.show()
    }

    fun DeleteUser(context: Context, userUid: String, user: FirebaseUser) {
        mdbref = FirebaseDatabase.getInstance().getReference()
        //delete user from db
        mdbref.child("user").child(userUid).removeValue()
        // delete user from authentication
        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(ContentValues.TAG, "User account deleted.")
                    Toast.makeText(context,"User account deleted",Toast.LENGTH_SHORT).show()
                    val intent = Intent(context,Login::class.java)
                    context.startActivity(intent)
                }
                else{
                    try {
                        throw task.exception!!
                    }catch (e: Exception) {
                        Log.e(ContentValues.TAG, e.message!!)
                        Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}