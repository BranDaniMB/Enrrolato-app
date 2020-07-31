package com.enrrolato.enrrolato

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enrrolato.enrrolato.adapter.AdapterCart
import com.enrrolato.enrrolato.database.Enrrolato
import com.enrrolato.enrrolato.database.Icecream
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class CartScreenFragment : Fragment() {

    private var enrrolato = Enrrolato.instance
    private var listToRecycler: ArrayList<String> = ArrayList()
    private var txtPrice = "Precio: "
    private var priceT = 0
    private lateinit var noIcecream: TextView
    private lateinit var name: TextView
    private lateinit var sendAll: Button
    private lateinit var recyclerCart: RecyclerView
    private lateinit var price: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_cart_screen, container, false)
        price = view.findViewById(R.id.txtTotalPrice)
        recyclerCart = view.findViewById(R.id.rvCart)
        sendAll = view.findViewById(R.id.btSendAll)
        name = view.findViewById(R.id.showUsername)
        noIcecream = view.findViewById(R.id.txtNoIceCream)

        enrrolato.getUsername().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                name.text = d.child("username").value.toString()
            }

            override fun onCancelled(p0: DatabaseError) {}
        })

        myOrders()

        if (recyclerCart == null || recyclerCart.adapter == null) {
            noIcecream.visibility = View.VISIBLE
            sendAll.visibility = View.INVISIBLE
            price.visibility = View.INVISIBLE
        }
        else {
            noIcecream.visibility = View.INVISIBLE
            sendAll.visibility = View.VISIBLE
            price.visibility = View.VISIBLE
        }

        price.text = txtPrice + priceT

        sendAll.setOnClickListener {
            sendAllOrders()
        }

        return view
    }

    // ENVIAR TODOS LOS PEDIDOS
    private fun sendAllOrders() {
        enrrolato.sendAllOrders(name.text.toString())
        message("Se ha enviado su orden\n Tiempo estimado 20 minutos\n (Esto puede variar según la cantidad de pedidos en el local)")
        sendAll.isEnabled = false
    }

    // AQUI TIENE QUE MOSTRARSE TODAS LAS ÓRDENES MIAS (INCLUIDAS LAS DEL HISTORIAL(FAVORITAS))
    private fun myOrders() {
        var list = enrrolato.getList()

        for(l2 in list) {
            if(l2.id.equals(enrrolato.getId())) {
              listToRecycler.add("Sabores: " + l2.flavor.substring(0, l2.flavor.length - 1) + "\nJarabe: " + l2.filling + "\nToppings: " + l2.topping.substring(0, l2.topping.length - 1) +
                      "\nEnvase: " + l2.container + "\nPrecio: " + l2.price)
                showCart(listToRecycler)
                priceT += l2.price
            }
        }

        // AQUI TIENE QUE CAPTURARSE LAS ÓRDENES QUE YA SE HAYAN HECHO
        // PERO SIMBOLIZAR DE ALGUNA MANERA QUE SOLO ESTAN PARA INFORMACION

    }

    private fun showCart(listToRecycler: ArrayList<String>) {
        recyclerCart.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        var af = AdapterCart(listToRecycler)
        recyclerCart.adapter = af

        // BORRAR UNO DE LA LISTA DE PEDIDOS
        af.setOnItemClickListener(object: AdapterCart.OnItemClickListener {
            override fun onDeleteClick(position: Int) {
                //Toast.makeText(context, "Se eliminó $position", Toast.LENGTH_SHORT).show()

                if(listToRecycler.size == 1 || listToRecycler.size == 0) {
                    enrrolato.deleteFromList(position)
                    listToRecycler.removeAt(position)
                    af.notifyItemRemoved(position)
                }
                else {
                    enrrolato.deleteFromList(position)
                    listToRecycler.removeAt(position)
                    af.notifyItemRemoved(position)
                }

                if (listToRecycler.isEmpty()) {
                    noIcecream.visibility = View.VISIBLE
                    sendAll.visibility = View.INVISIBLE
                    price.visibility = View.INVISIBLE
                }

            }
        })
    }

    private fun message(msg: String) {
        val alertDialogBuilder = context?.let { AlertDialog.Builder(it, R.style.alert_dialog) }
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val popup = layoutInflater.inflate(R.layout.popup_alert_message, null)
        val message = popup.findViewById<View>(R.id.txtMessage) as TextView
        message.text = msg
        val bt_ok: Button = popup.findViewById(R.id.btOk);
        alertDialogBuilder?.setView(popup)
        val alertDialog: AlertDialog = alertDialogBuilder!!.create()
        alertDialog.window?.attributes!!.windowAnimations = R.style.alert_dialog
        alertDialog.show()

        bt_ok.setOnClickListener {
            alertDialog.cancel()
        }
    }

}
