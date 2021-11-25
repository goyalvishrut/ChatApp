package com.example.chatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, val messageList: ArrayList<message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType == 1){
            // receive
            val view: View = LayoutInflater.from(context).inflate(R.layout.recevie, parent, false)
            return recevieviewholder(view)
        }else{
            //sent
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            return sentviewholder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messageList[position]

        if(holder.javaClass == sentviewholder::class.java){
            // sentview

            val viewHolder = holder as sentviewholder
            holder.sentMessage.text = currentMessage.message
        }
        else{
            // receiveview
            val viewHolder = holder as recevieviewholder
            holder.receivemessage.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentmessage = messageList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentmessage.senderid)){
            return ITEM_SENT
        }else{
            return ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class sentviewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.text_sent_message)
    }

    class recevieviewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val receivemessage = itemView.findViewById<TextView>(R.id.text_recevie_message)
    }

}