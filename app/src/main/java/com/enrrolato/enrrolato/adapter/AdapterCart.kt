package com.enrrolato.enrrolato.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.enrrolato.enrrolato.R


class AdapterCart(): Adapter<AdapterCart.ViewHolder>() {

    lateinit var list: ArrayList<String>

    constructor(list: ArrayList<String>): this() {
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list_cart, null,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.asignData(list.get(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var data : TextView = itemView.findViewById(R.id.itemCart)
        var sendOne: Button = itemView.findViewById(R.id.btSend)
        var details: Button = itemView.findViewById(R.id.btDetails)

        fun asignData(eData: String) {
            data.setText(eData)
        }

        fun sendOne(p: Int) {
            sendOne.setOnClickListener {

            }
        }

        fun details(p: Int) {
            details.setOnClickListener {

            }
        }

    }

}