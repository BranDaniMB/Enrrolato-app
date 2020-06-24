package com.enrrolato.enrrolato.createIcecream

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enrrolato.enrrolato.R
import kotlinx.android.synthetic.main.item_list.view.*

class AdapterFlavor(): RecyclerView.Adapter<AdapterFlavor.ViewHolder>() {

    lateinit var list: ArrayList<String>

    constructor(list: ArrayList<String>): this() {
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list, null,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.asignData(list.get(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var data : TextView = itemView.findViewById(R.id.item)

        fun asignData(eData: String) {
            data.setText(eData)

        }

    }

}