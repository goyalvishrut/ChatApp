package com.example.chatapp

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var userrecyclerview: RecyclerView
    private lateinit var userlist: ArrayList<Users>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mdbref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        mdbref = FirebaseDatabase.getInstance().getReference()

        userlist = ArrayList()
        adapter = UserAdapter(this@MainActivity,userlist)

        userrecyclerview = findViewById(R.id.userrecyclerview)
        userrecyclerview.layoutManager = LinearLayoutManager(this)
        userrecyclerview.adapter = adapter

        mdbref.child("user").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                userlist.clear()
                for (postSnapshot in snapshot.children){
                    val currentuser = postSnapshot.getValue(Users::class.java)

                    if(mAuth.currentUser?.uid != currentuser?.uid){
                        userlist.add(currentuser!!)
                    }

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_bar,menu)
        menuInflater.inflate(R.menu.menu,menu)
        menuInflater.inflate(R.menu.delete_user,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            // return to login
            mAuth.signOut()
            val intent = Intent(this,Login::class.java)
            finish()
            startActivity(intent)
            return true
        }
        if (item.itemId == R.id.delete_user){
            val user_uid = mAuth.currentUser?.uid!!
            val user = Firebase.auth.currentUser!!
            DeleteUser.showAlertdialog(this,"Confirm Deletion",user_uid,user)
        }
        if (item.itemId == R.id.search_bar){
        }
        return true
    }

}