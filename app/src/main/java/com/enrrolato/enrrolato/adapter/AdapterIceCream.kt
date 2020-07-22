package com.enrrolato.enrrolato.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.enrrolato.enrrolato.R


class AdapterIceCream(): Adapter<AdapterIceCream.ViewHolder>() {

lateinit var list: ArrayList<String>
lateinit var listener: OnItemClickListener

    constructor(list: ArrayList<String>): this() {
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list_icecream, null,false)

        //view.setOnClickListener(this)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.asignData(list[position])
        holder.deleteData(position)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    /*
    fun setOnClickListener(listener: View.OnClickListener) {
        this.listener = listener
    }

    override fun onClick(v: View?) {
        if(listener != null) {
            listener.onClick(v)
        }
    }
     */

    interface OnItemClickListener {
        fun onDeleteClick(position: Int)
    }

    class ViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

        var listener = listener
        var data: TextView = itemView.findViewById(R.id.item)
        var delete: ImageButton = itemView.findViewById(R.id.imgDelete)

        fun deleteData(position: Int) {
            delete.setOnClickListener {
                if(position != NO_POSITION) {
                    listener.onDeleteClick(position)
                }
            }
        }

        fun asignData(eData: String) {
            data.setText(eData)
        }

    }

}

