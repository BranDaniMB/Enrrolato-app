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
import com.enrrolato.enrrolato.objects.Flavor
import com.enrrolato.enrrolato.objects.SeasonIcecream

class DefaultFlavorFragment : Fragment() {

    private lateinit var flavorList: ArrayList<String>
    private lateinit var listFlavor: ArrayList<Flavor>
    private lateinit var listSeason: ArrayList<SeasonIcecream>
    private var enrrolato = Enrrolato.instance
    private lateinit var flavorSelected: String
    private var isSeason: Boolean = false

    private lateinit var backPrincipal: ImageButton
    private lateinit var makeIcecream: Button
    private lateinit var spSpecial: Spinner
    private lateinit var spSeason: Spinner
    private lateinit var next: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_default_flavor, container, false)
        backPrincipal = view.findViewById(R.id.btBackToPrincipal)
        makeIcecream = view.findViewById(R.id.btCreateOurIcecream)
        spSpecial = view.findViewById(R.id.spDefaultFlavor)
        spSeason = view.findViewById(R.id.spSeasonFlavor)
        next = view.findViewById(R.id.btNext)

        loadSpecial()
        loadSeason()

        backPrincipal.setOnClickListener {
            backToPrincipal()
        }

        makeIcecream.setOnClickListener {
            makeOwns()
        }

        next.setOnClickListener {
            if(!isSeason) {
                nextStepTopping()
            } else {
                nextStepSeason()
            }
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

    private fun loadSpecial() {
            listFlavor = enrrolato.listFlavors
            flavorList = ArrayList()
            flavorList.add(getString(R.string.preset_flavor_list))

            for (list in listFlavor) {
                if (list.isSpecial  && !list.isLiqueur && list.avaliable) {
                    flavorList.add(list.name)
                }
            }
            fillSpinner(spSpecial)
        }

    private fun loadSeason() {
        listSeason = enrrolato.listSeasonIcecream
        flavorList = ArrayList()
        flavorList.add(getString(R.string.choose_season))

        for (list in listSeason) {
            if(list.avaliable) {
                flavorList.add(list.name)
            }
        }
        fillSpinner(spSeason)
    }

        private fun fillSpinner(flavor: Spinner) {
            val array: ArrayAdapter<String> = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, flavorList)
            flavor.adapter = array
            flavor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(av: AdapterView<*>?, view: View?, i: Int, p3: Long) {
                    flavorSelected = av?.getItemAtPosition(i).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }

    private fun flavorProcessAdd(f: String) {
        listFlavor = enrrolato.listFlavors
        listSeason = enrrolato.listSeasonIcecream

        for(list in listFlavor) {
            if(list.name.equals(f)) {
                enrrolato.createIceCream().addFlavor(list)
                isSeason = false
            }
        }

        for(list in listSeason) {
            if(list.name.equals(f)) {
                enrrolato.createIceCream().addSeasonIcecream(list)
                isSeason = true
            }
        }

    }

    private fun nextStepSeason() {
        if(flavorSelected.equals(getString(R.string.choose_season)) || flavorSelected.isEmpty()) {
            errorFlavor(getString(R.string.no_preset))
        }
        else {
            flavorProcessAdd(flavorSelected)
            enrrolato.addListSeason()
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