package com.example.chatapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView

class ImageSend : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var sendimagebutton: ImageView
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_send)

        imageView = findViewById(R.id.imageView)
        sendimagebutton = findViewById(R.id.sendimagebutton)

        imageUri = Uri.parse(intent.getStringExtra("imageUri"))
        val name = intent.getStringExtra("name")
        val receiveruid = intent.getStringExtra("receiveruid")

        Log.i(TAG,"$imageUri")
        Log.i(TAG,"$name")
        Log.i(TAG,"$receiveruid")
        imageView.setImageURI(imageUri)


        sendimagebutton.setOnClickListener{
            val intent = Intent(this,chatactivity::class.java)
            intent.putExtra("name",name)
            intent.putExtra("uid",receiveruid)
            startActivity(intent)
        }
    }

}