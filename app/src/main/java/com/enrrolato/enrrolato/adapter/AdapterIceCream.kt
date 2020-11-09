package com.enrrolato.enrrolato.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.manager.Enrrolato


class AdapterIceCream() : Adapter<AdapterIceCream.ViewHolder>() {
    lateinit var list: ArrayList<String>
    lateinit var listener: OnItemClickListener

    constructor(list: ArrayList<String>) : this() {
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.selected_item_detail, null, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.assignData(list[position])
        holder.deleteData(position)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onDeleteClick(position: Int)
    }

    class ViewHolder(itemView: View, var listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        var itemName: TextView = itemView.findViewById(R.id.itemName)
        var delete: ImageButton = itemView.findViewById(R.id.imgDelete)

        fun deleteData(position: Int) {
            delete.setOnClickListener {
                if (position != NO_POSITION) {
                    listener.onDeleteClick(position)
                }
            }
        }

        fun assignData(name: String) {
            itemName.text = name
        }

    }

}

