package com.enrrolato.enrrolato.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.enrrolato.enrrolato.DetailFragment
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.database.Enrrolato
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class AdapterCart(): Adapter<AdapterCart.ViewHolder>() {

    lateinit var list: ArrayList<String>
    private lateinit var listener: OnItemClickListener

    constructor(list: ArrayList<String>): this() {
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list_cart, null,false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.asignData(list.get(position))
        holder.clickListener(position)
        holder.deleteData(position)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onDeleteClick(position: Int)
    }

    class ViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var enrrolato = Enrrolato.instance
        var listener = listener
        var context = itemView.context
        var data: TextView = itemView.findViewById(R.id.itemCart)
        var details: Button = itemView.findViewById(R.id.btDetails)
        var delete: Button = itemView.findViewById(R.id.btDelete)
        var favoriteDisabled: ImageButton = itemView.findViewById(R.id.favoriteDisabled)
        var favoriteEnabled: ImageButton = itemView.findViewById(R.id.favoriteEnabled)
        var id: Int = 0
        var uName = ""

        fun asignData(eData: String) {
            data.setText(eData)
        }

        fun clickListener(position: Int) {
            details.setOnClickListener(this)
            favoriteEnabled.setOnClickListener(this)
            favoriteDisabled.setOnClickListener(this)
            id = position

            enrrolato.getUsername().addValueEventListener(object : ValueEventListener {
                override fun onDataChange(d: DataSnapshot) {
                    uName = d.child("username").value.toString()
                }

                override fun onCancelled(p0: DatabaseError) {}
            })
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.btDetails -> {
                    goToDetail(id)
                }

                R.id.favoriteDisabled -> {
                    fav(id)
                }

                R.id.favoriteEnabled -> {
                    fav(id)
                }
            }
        }

        fun deleteData(p: Int) {
            delete.setOnClickListener {
                if (p != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(p)
                }
            }
        }

        private fun goToDetail(id: Int) {
            val fragment = DetailFragment()
            fragment.catchID(id)
            val fm = (context as AppCompatActivity).supportFragmentManager
            val transaction = fm.beginTransaction()
            transaction.replace(R.id.ly_cart, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        private fun fav(id: Int) {
            if(enrrolato.getFavorite(id)) {
                favoriteDisabled.visibility = View.VISIBLE
                favoriteEnabled.visibility = View.INVISIBLE
                enrrolato.addFavoriteIcecream(id)
                //enrrolato.setFavorite(id)
                // EN VEZ DEL SET_FAVORITE VA SE VA A CREAR UNA RAMA NUEVA CON LOS HELADOS SELECCIONADOS
            }
            else {
                favoriteDisabled.visibility = View.INVISIBLE
                favoriteEnabled.visibility = View.VISIBLE
                enrrolato.removeFavoriteIcecream(id)
                //enrrolato.setFavorite(id)
                // AQUI HAY QUE ELIMINAR DE LA BASE DE DATOS
            }
        }


    }
}