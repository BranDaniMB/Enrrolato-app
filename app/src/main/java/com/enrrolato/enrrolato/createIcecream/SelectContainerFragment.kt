package com.enrrolato.enrrolato.createIcecream

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.enrrolato.enrrolato.CartScreenFragment
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.database.Enrrolato

class SelectContainerFragment : Fragment() {

    private var enrrolato = Enrrolato.instance
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

        loadContainers(sp)

        back.setOnClickListener {
            backToTopping()
        }

        addCart.setOnClickListener {
            addToCart()
        }

        addNewIceCream.setOnClickListener {
            newIC()
        }

        return view
    }

    private fun loadContainers(s: Spinner) {
        val containers = Enrrolato.instance.listContainers
        nameList = ArrayList()
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

    private fun newIcecream() {
        enrrolato.createIceCream().cleanData()
        val fragment = DefaultFlavorFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
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
        if (containerSelected.equals(getString(R.string.container_selector))) {
            errorContainer(getString(R.string.no_container))
        } else {
            enrrolato.createIceCream().addContainer(containerSelected)
            enrrolato.addList()

            // SE DIRIGE AL CARRITO DE COMPRAS
            goToCart()
            enrrolato.createIceCream().cleanData()
        }

        /*
        // ANTES DE AGREGARLA AL CARRITO DEBE MOSTRARSE UN MENSAJE EN DONDE SE LE SOLICITA EL NOMBRE DE USUARIO (SI NO LO TIENE)

        // ESTO HAY QUE MEJORARLO PARA QUE NO EL NOMBRE LO SOLICITE EN OTRA PARTE
        if (hide.text.equals("")) {
            enterUsername(hide)
        } else {
            // MANDARLO AL CARRITO O TENERLE UNA OPCIÓN PARA MANDARLO DE UNA VEZ ?
            //enrrolato.createIceCream().setUsername(hide.text.toString())

            // ANTES DE MANDARLO, MANDAR UN MENSAJE DICIENDO QUE SE MANDÓ AL CARRITO O ENVIADO AL LOCAL
            enrrolato.createOrders()
        }
         */
    }

    private fun goToCart() {
        val fragment = CartScreenFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun newIC() {
        if (containerSelected.equals(getString(R.string.container_selector))) {
            errorContainer(getString(R.string.no_container))
        } else {
            enrrolato.createIceCream().addContainer(containerSelected)
            enrrolato.addList()
            newIcecream()
        }
    }

    private fun errorContainer(msg: String) {
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