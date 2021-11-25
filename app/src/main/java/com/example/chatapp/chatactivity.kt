package com.example.chatapp

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class chatactivity : AppCompatActivity() {

    private lateinit var chatrecyclerview: RecyclerView
    private lateinit var messagebox: EditText
    private lateinit var sendbutton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<message>
    private lateinit var mdbref: DatabaseReference
    private lateinit var nametopass: String
    private lateinit var ruidtopass: String


    private lateinit var imagebutton: Button
    private val pickImage = 100
    private var imageUri: Uri? = null

    var recevierroom: String? = null
    var senderroom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatactivity)

        val builder = NotificationCompat.Builder(this)


        val name = intent.getStringExtra("name")
        val reciveruid = intent.getStringExtra("uid")

        nametopass = name!!
        ruidtopass = reciveruid!!

        val senderuid = FirebaseAuth.getInstance().currentUser?.uid

        senderroom = reciveruid + senderuid
        recevierroom = senderuid + reciveruid


        supportActionBar?.title = name


        chatrecyclerview = findViewById(R.id.chatRecyclerView)
        messagebox = findViewById(R.id.messagebox)
        sendbutton = findViewById(R.id.sendbutton)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)
        mdbref = FirebaseDatabase.getInstance().getReference()
        imagebutton = findViewById(R.id.imagebutton)


        chatrecyclerview.layoutManager = LinearLayoutManager(this)
        chatrecyclerview.adapter = messageAdapter

        //(chatrecyclerview.layoutManager as LinearLayoutManager).stackFromEnd

        // add data to recyclerview
        mdbref.child("chats").child(senderroom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()
                    for (postsnapshot in snapshot.children) {
                        val message = postsnapshot.getValue(message::class.java)
                        messageList.add(message!!)
                        (chatrecyclerview.layoutManager as LinearLayoutManager).scrollToPosition(
                            messageList.size - 1
                        )
                    }
                    messageAdapter.notifyDataSetChanged()
//                    (chatrecyclerview.layoutManager as LinearLayoutManager).scrollToPosition(
//                        messageList.size - 1
//                    )
//                    mdbref.child("chats").child(senderroom!!).child("messages")
//                        .addChildEventListener(object : ChildEventListener {
//                            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
//                                builder.setSmallIcon(android.R.mipmap.sym_def_app_icon)
//                                builder.setContentTitle("Firebase Push Notification")
//                                builder.setContentText("Hello this is a test Firebase notification, a new database child has been added")
//                                val notificationManager =
//                                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//                                notificationManager.notify(1, builder.build())
//                            }
//
//                            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
//                            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
//                            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
//                            override fun onCancelled(databaseError: DatabaseError) {}
//                        })
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })


        sendbutton.setOnClickListener {

            val message = messagebox.text.toString()
            val messageobject = message(message, senderuid)

            mdbref.child("chats").child(senderroom!!).child("messages").push()
                .setValue(messageobject).addOnSuccessListener {
                    mdbref.child("chats").child(recevierroom!!).child("messages").push()
                        .setValue(messageobject)
                }
            messagebox.setText("")
        }

        imagebutton.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }



//        mdbref.addChildEventListener(object : ChildEventListener {
//            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
//                //builder.setSmallIcon(android.R.mipmap.ic_launcher)
//                builder.setContentTitle("Firebase Push Notification")
//                builder.setContentText("Hello this is a test Firebase notification, a new database child has been added")
//                val notificationManager =
//                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//                notificationManager.notify(1, builder.build())
//            }
//
//            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
//            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
//            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
//            override fun onCancelled(databaseError: DatabaseError) {}
//        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            val intent = Intent(this,ImageSend::class.java)

            intent.putExtra("imageUri",imageUri.toString())
            intent.putExtra("name",nametopass)
            intent.putExtra("receiveruid",ruidtopass)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.delete,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.delete_message){
            // return to login
            var uid = mdbref.child("chats").child(senderroom!!).child("messages").child("message").key
            Log.i(TAG,"$uid")
            DeleteMessageFromBothSide.showAlertdialog(this,"Confirm Deletion",senderroom,recevierroom)
            return true
        }
        return true
    }

}