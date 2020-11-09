package com.enrrolato.enrrolato.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.enrrolato.enrrolato.R

class AdapterMenu(): Adapter<AdapterMenu.ViewHolder>() {

    lateinit var list: ArrayList<String>
    private lateinit var listener: View.OnClickListener

    constructor(list: ArrayList<String>): this() {
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list_icecream, null,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.assignData(list[position])
    }

    fun setOnClickListener(listener: View.OnClickListener) {
        this.listener = listener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var data : TextView = itemView.findViewById(R.id.item)

        fun assignData(eData: String) {
            data.text = eData.capitalize();
        }
    }

}