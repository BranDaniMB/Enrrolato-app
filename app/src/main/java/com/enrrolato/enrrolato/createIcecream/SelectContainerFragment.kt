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
    private var flag: Boolean = false

    private lateinit var back: ImageButton
    private lateinit var addCart: Button
    private lateinit var addNewIceCream: Button
    private lateinit var sp: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_select_container, container, false)
        back = view.findViewById(R.id.backBtn)
        addCart = view.findViewById(R.id.btAddToShopCart)
        addNewIceCream = view.findViewById(R.id.btAddNewIceCream)
        sp = view.findViewById(R.id.spContainer)

        loadContainers()

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

    private fun loadContainers() {
        val containers = Enrrolato.instance.listContainers
        nameList = ArrayList()
        nameList.add(getString(R.string.container_selector))

        for (container in containers) {
            if (container.available && !container.isExclusive) {
                nameList.add(container.name)
            }
        }
        fillContainer()
    }

    private fun fillContainer() {
        val array: ArrayAdapter<String> =
            ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, nameList)
        sp.adapter = array
        sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(av: AdapterView<*>?, view: View?, i: Int, p3: Long) {
                containerSelected = av?.getItemAtPosition(i).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    fun isSeason(flag: Boolean) {
        this.flag = flag
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
        }
        else {
            enrrolato.createIceCream().addContainer(containerSelected)

            if(flag) {
                enrrolato.addListSeason()
            }
            else {
                enrrolato.addList()
            }
        }
        enrrolato.createIceCream().cleanData()
        goToCart()
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

            if(flag) {
                enrrolato.addListSeason()
            }
            else {
                enrrolato.addList()
            }
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