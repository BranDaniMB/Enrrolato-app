package com.enrrolato.enrrolato.createIcecream

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.enrrolato.enrrolato.CartScreenFragment
import com.enrrolato.enrrolato.PrincipalMenuFragment
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.database.Enrrolato
import com.enrrolato.enrrolato.iceCream.Flavor
import com.enrrolato.enrrolato.iceCream.SeasonIcecream

class DefaultFlavorFragment : Fragment() {

    private lateinit var flavorList: ArrayList<String>
    private lateinit var listFlavor: ArrayList<Flavor>
    private lateinit var listSeason: ArrayList<SeasonIcecream>
    private var enrrolato = Enrrolato.instance
    private lateinit var flavorSelected: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_default_flavor, container, false)

        val backPrincipal = view.findViewById<View>(R.id.btBackToPrincipal) as ImageButton
        val makeIcecream = view.findViewById<View>(R.id.btCreateOurIcecream) as Button
        val spSpecial = view.findViewById<View>(R.id.spDefaultFlavor) as Spinner
        val spSeason = view.findViewById<View>(R.id.spSeasonFlavor) as Spinner
        val next = view.findViewById<View>(R.id.btNext) as Button

        loadSpecial(spSpecial)
        loadSeason(spSeason)

        backPrincipal.setOnClickListener {
            backToPrincipal()
        }

        makeIcecream.setOnClickListener {
            makeOwns()
        }

        next.setOnClickListener {
            nextStepTopping()
        }
        return view
    }

    private fun backToPrincipal() {
        val fragment = PrincipalMenuFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_default, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun makeOwns() {
        val fragment = FlavorsFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_default, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun loadSpecial(f: Spinner) {
            listFlavor = enrrolato.listFlavors
            flavorList = ArrayList()
            flavorList.add(getString(R.string.preset_flavor_list))

            for (list in listFlavor) {
                if (list.isSpecial  && !list.isLiqueur && list.avaliable) {
                    flavorList.add(list.name)
                }
            }
            fillSpinner(f)
        }

    private fun loadSeason(f: Spinner) {
        listSeason = enrrolato.listSeasonIcecream
        flavorList = ArrayList()
        flavorList.add(getString(R.string.choose_season))

        for (list in listSeason) {
            if(list.avaliable) {
                flavorList.add(list.name)
            }
        }
        fillSpinner(f)
    }

        private fun fillSpinner(flavor: Spinner) {
            val array: ArrayAdapter<String> = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, flavorList)
            flavor.adapter = array
            flavor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(av: AdapterView<*>?, view: View?, i: Int, p3: Long) {
                    flavorSelected = av?.getItemAtPosition(i).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }

    private fun flavorProcessAdd(f: String) {
        listFlavor = enrrolato.listFlavors

        for(list in listFlavor) {
            if(list.name.equals(f)) {
                enrrolato.createIceCream().addFlavor(list)
            }
        }
    }

    private fun nextStepSeason() {
        if(flavorSelected.equals(getString(R.string.choose_season)) || flavorSelected.isEmpty()) {
            errorFlavor(getString(R.string.no_preset))
        }
        else {
            // AQUI BRINCA DIRECTAMENTE AL CARRITO YA QUE TODO ESTÁ PREDEFINIDO
            // TIENE QUE TENER UN MÉTODO QUE LOS CAPTURE Y LOS MANDE AL OTRO LADO

            val fragment = CartScreenFragment()
            val fm = requireActivity().supportFragmentManager
            val transaction = fm.beginTransaction()
            transaction.replace(R.id.ly_default, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun nextStepTopping() {
        if(flavorSelected.equals(getString(R.string.preset_flavor_list)) || flavorSelected.isEmpty()) {
            errorFlavor(getString(R.string.no_preset))
        }
        else {
            // BRINCA DIRECTAMENTE A ESCOGER EL TOPPING --> TIENE QUE LLEVARSE EL SABOR / HELADO YA DEFINIDO
            flavorProcessAdd(flavorSelected)

            val fragment = ToppingFragment()
            val fm = requireActivity().supportFragmentManager
            val transaction = fm.beginTransaction()
            transaction.replace(R.id.ly_default, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun errorFlavor(msg: String) {
        val alertDialogBuilder = context?.let { AlertDialog.Builder(it, R.style.alert_dialog) }
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val popupF = layoutInflater.inflate(R.layout.popup_alert_message, null)
        val message = popupF.findViewById<View>(R.id.txtMessage) as TextView
        message.text = msg
        val bt_ok: Button = popupF.findViewById(R.id.btOk);
        alertDialogBuilder?.setView(popupF)
        val alertDialog: AlertDialog = alertDialogBuilder!!.create()
        alertDialog.window?.attributes!!.windowAnimations = R.style.alert_dialog
        alertDialog.show()

        bt_ok.setOnClickListener {
            alertDialog.cancel()
        }
    }


}