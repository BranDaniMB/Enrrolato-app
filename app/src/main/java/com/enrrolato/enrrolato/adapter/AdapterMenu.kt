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
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list_icecream, null,false)
        var trash = view.findViewById<View>(R.id.imgDelete) as ImageButton
        //view.setOnClickListener(this)
        trash.visibility = View.INVISIBLE
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.asignData(list.get(position))
    }

    fun setOnClickListener(listener: View.OnClickListener) {
        this.listener = listener
    }

    /*
    override fun onClick(v: View?) {
        if(listener != null) {
            listener.onClick(v)
        }
    }
     */

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var data : TextView = itemView.findViewById(R.id.item)

        fun asignData(eData: String) {
            data.setText(eData)
        }
    }

}