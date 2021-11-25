package com.example.chatapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception

class SignUp : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var edtName: EditText

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mdbref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        edtName = findViewById(R.id.edt_name)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnSignup = findViewById(R.id.signup_button)

        btnSignup.setOnClickListener{
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            signup(name,email,password)
        }
    }

    private fun signup(name: String, email: String, password: String) {
        // Logic of creating user
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Return to home page

                    addUsertoDatabase(name,email,mAuth.currentUser?.uid!!)
                    val intent = Intent(this@SignUp,MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    // Show error
                    try {
                        throw task.exception!!
                    }catch (e: Exception) {
                        Log.e(TAG, e.message!!)
                        Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()
                    }
                    //Log.e("Signup Error", "onCancelled", task.exception);
                }
            }
    }

    private fun addUsertoDatabase(name: String, email: String, uid: String) {
        mdbref = FirebaseDatabase.getInstance().getReference()
        mdbref.child("user").child(uid).setValue(Users(name,email,uid))
    }

}