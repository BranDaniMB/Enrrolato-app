package com.enrrolato.enrrolato.createIcecream

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.database.Enrrolato

class SelectContainerFragment : Fragment() {

    private lateinit var username:String
    private var enrrolato = Enrrolato.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View =  inflater.inflate(R.layout.fragment_select_container, container, false)
        var back = view.findViewById<View>(R.id.backBtn) as ImageButton
        var addCart = view.findViewById<View>(R.id.btAddToShopCart) as Button
        var addNewIceCream = view.findViewById<View>(R.id.btAddNewIceCream) as Button
        val sp = view.findViewById<View>(R.id.spContainer) as Spinner

        loadContainers(sp)

        back.setOnClickListener {
            backToTopping()
        }

        addCart.setOnClickListener {
            addToCart()
        }

        return view
    }

    private fun loadContainers(s: Spinner) {
        val containers = Enrrolato.instance.listContainers
        val nameList = ArrayList<String>()

        nameList.add(getString(R.string.container_selector))

        for (container in containers) {

            if (container.available) {
                nameList.add(container.name)
            }
        }
        s.adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, nameList)
    }

    private fun backToTopping() {
        val fragment = ToppingFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_container, fragment)
        transaction.addToBackStack(null)
       transaction.commit()
   }

    private fun addToCart() {
        var u: String = ""
        // ANTES DE AGREGARLA AL CARRITO DEBE MOSTRARSE UN MENSAJE EN DONDE SE LE SOLICITA EL NOMBRE DE USUARIO (SI NO LO TIENE)
        if(enrrolato.getUsername().equals("")) {
            u = enterUsername()
            enrrolato.setUsername(u)
            username = u
        }
        else {
            username = enrrolato.getUsername()
        }

        // MANDARLO AL CARRITO (ESCRIBIR EL PEDIDO A LA BASE DE DATOS )
        // DEBE IR INCLUIDO EL NOMBRE = USERNAME
        var id = enrrolato.getId()
    }

    private fun enterUsername(): String {
        var txt = "Ingrese su nombre"
        val alertDialogBuilder = context?.let { AlertDialog.Builder(it, R.style.alert_dialog) }
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val popupUsername = layoutInflater.inflate(R.layout.popup_username, null)
        val msg: TextView = popupUsername.findViewById(R.id.txtMessage)
        msg.text = txt
        val bt_ok: Button = popupUsername.findViewById(R.id.btOkUsername);
        val bt_cancel: Button = popupUsername.findViewById(R.id.btCancelUsername);
        val username: TextView = popupUsername.findViewById(R.id.eTextUsername)
        alertDialogBuilder?.setView(popupUsername)
        val alertDialog: AlertDialog = alertDialogBuilder!!.create()
        alertDialog.window?.attributes!!.windowAnimations = R.style.alert_dialog
        alertDialog.show()

        bt_ok.setOnClickListener {
            alertDialog.cancel()
        }

        bt_cancel.setOnClickListener {
            alertDialog.cancel()
        }
        return username.text.toString()
    }

}