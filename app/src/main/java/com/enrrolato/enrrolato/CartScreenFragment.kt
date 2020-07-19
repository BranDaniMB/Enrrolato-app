package com.enrrolato.enrrolato

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enrrolato.enrrolato.adapter.AdapterCart
import com.enrrolato.enrrolato.database.Enrrolato
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class CartScreenFragment : Fragment() {

    private var enrrolato = Enrrolato.instance
    //private var name = ""
    private var listToRecycler: ArrayList<String> = ArrayList()
    private var txtPrice = "Precio: "
    private var priceT = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // PASAR TODO ESTO COMO ATRIBUTOS PARA NO ESTAR LLAMANDOLOS EN METODOS
        // TAMBIEN EN LAS OTRAS COSAS

        var view = inflater.inflate(R.layout.fragment_cart_screen, container, false)
        var price = view.findViewById<View>(R.id.txtTotalPrice) as TextView
        var recyclerCart = view.findViewById<View>(R.id.rvCart) as RecyclerView
        var sendAll = view.findViewById<View>(R.id.btSendAll) as Button
        var name = view.findViewById<View>(R.id.showUsername) as TextView
        var noIcecream = view.findViewById<View>(R.id.txtNoIceCream) as TextView

        enrrolato.getUsername().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                name.text = d.child("username").value.toString()
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        myOrders(recyclerCart)

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

        return view
    }

    /*
    // ESTA CREO QUE NO SE USA
    private fun catchUsername() {
        enrrolato.getUsername().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                name = d.child("username").value.toString()
            }

            override fun onCancelled(d: DatabaseError) {
            }
        })
    }
     */

    // AQUI TIENE QUE MOSTRARSE TODAS LAS ÓRDENES MIAS (INCLUIDAS LAS DEL HISTORIAL)
    private fun myOrders(recycler: RecyclerView) {
        var list = enrrolato.getList()

        for(l2 in list) {
            if(l2.id.equals(enrrolato.getId())) {

              listToRecycler.add("Sabores: " + l2.flavor + "\nJarabe: " + l2.filling + "\nToppings: " + l2.topping + "\nEnvase: " + l2.container + "\nPrecio: " + l2.price)
                showCart(recycler, listToRecycler)
                priceT += l2.price
            }
        }
    }

    private fun showCart(recycler: RecyclerView, listToRecycler: ArrayList<String>) {
        recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        var af = AdapterCart(listToRecycler)
        recycler.adapter = af
    }

    private fun errorCart(msg: String) {
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

    /*
    // ESTO VA AL OTRO LADO
    private fun enterUsername() {
        var txt = getString(R.string.enter_name)
        val alertDialogBuilder = context?.let { AlertDialog.Builder(it, R.style.alert_dialog) }
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val popupUsername = layoutInflater.inflate(R.layout.popup_username, null)
        val msg: TextView = popupUsername.findViewById(R.id.txtMessage)
        msg.text = txt
        val bt_ok: Button = popupUsername.findViewById(R.id.btOkUsername);
        val bt_cancel: Button = popupUsername.findViewById(R.id.btCancelUsername);
        val u: TextView = popupUsername.findViewById(R.id.eTextUsername)
        alertDialogBuilder?.setView(popupUsername)
        val alertDialog: AlertDialog = alertDialogBuilder!!.create()
        alertDialog.window?.attributes!!.windowAnimations = R.style.alert_dialog
        alertDialog.show()

        bt_ok.setOnClickListener {
            name = u.text.toString().trim()
            enrrolato.setUsername(name)
//            enrrolato.createIceCream().setUsername(u.text.toString())
            enrrolato.createOrders()
            alertDialog.cancel()
        }

        bt_cancel.setOnClickListener {
            alertDialog.cancel()
        }
    }
     */

}
