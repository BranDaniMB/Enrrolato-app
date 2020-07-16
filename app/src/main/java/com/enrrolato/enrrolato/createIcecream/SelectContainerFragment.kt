package com.enrrolato.enrrolato.createIcecream

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.createIcecream.process.IcecreamManager
import com.enrrolato.enrrolato.database.Enrrolato
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class SelectContainerFragment : Fragment() {

    private var enrrolato = Enrrolato.instance
    private var flag: Boolean = true
    private lateinit var containerSelected: String
    private lateinit var nameList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_select_container, container, false)
        var back = view.findViewById<View>(R.id.backBtn) as ImageButton
        var addCart = view.findViewById<View>(R.id.btAddToShopCart) as Button
        var addNewIceCream = view.findViewById<View>(R.id.btAddNewIceCream) as Button
        val sp = view.findViewById<View>(R.id.spContainer) as Spinner
        val hide = view.findViewById<View>(R.id.hiddenUsername) as TextView

        loadContainers(sp)
        catchUsername(hide).toString()

        back.setOnClickListener {
            backToTopping()
        }

        addCart.setOnClickListener {
            addToCart(hide)
        }
        return view
    }

    private fun loadContainers(s: Spinner) {
        val containers = Enrrolato.instance.listContainers
        nameList = ArrayList<String>()

        nameList.add(getString(R.string.container_selector))

        for (container in containers) {

            if (container.available) {
                nameList.add(container.name)
            }
        }
        fillContainer(s)
    }

    private fun fillContainer(c: Spinner) {
        val array: ArrayAdapter<String> =
            ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, nameList)
        c.adapter = array
        c.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(av: AdapterView<*>?, view: View?, i: Int, p3: Long) {
                containerSelected = av?.getItemAtPosition(i).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun backToTopping() {
        val fragment = ToppingFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun addToCart(hide: TextView) {
        if (containerSelected.equals(getString(R.string.container_selector))) {
            errorContainer(getString(R.string.no_container))
        } else {

            enrrolato.createIceCream().addContainer(containerSelected)

            // ANTES DE AGREGARLA AL CARRITO DEBE MOSTRARSE UN MENSAJE EN DONDE SE LE SOLICITA EL NOMBRE DE USUARIO (SI NO LO TIENE)
            if (hide.text.equals("")) {
                enterUsername(hide)

                enrrolato.createIceCream().setUsername(hide.text.toString())

                // ANTES DE MANDARLO, MANDAR UN MENSAJE DICIENDO QUE SE MANDÓ AL CARRITO O ENVIADO AL LOCAL
                enrrolato.createOrders()

            } else {
                // MANDARLO AL CARRITO O TENERLE UNA OPCIÓN PARA MANDARLO DE UNA VEZ ?
                enrrolato.createIceCream().setUsername(hide.text.toString())

                // ANTES DE MANDARLO, MANDAR UN MENSAJE DICIENDO QUE SE MANDÓ AL CARRITO O ENVIADO AL LOCAL
                enrrolato.createOrders()
            }
        }
    }


    private fun catchUsername(hide: TextView) {
        enrrolato.getUsername().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(d: DataSnapshot) {
                hide.text = d.child("username").value.toString()
            }

            override fun onCancelled(d: DatabaseError) {
            }

        })
    }

    private fun enterUsername(hide: TextView) {
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
            hide.text = u.text.toString().trim()
            enrrolato.setUsername(hide.text.toString())
            enrrolato.createIceCream().setUsername(hide.text.toString())
            alertDialog.cancel()
        }

        bt_cancel.setOnClickListener {
            alertDialog.cancel()
        }
    }

    private fun errorContainer(msg: String) {
        val alertDialogBuilder = context?.let { AlertDialog.Builder(it, R.style.alert_dialog) }
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val popup = layoutInflater.inflate(R.layout.popup_choose_flavor, null)
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