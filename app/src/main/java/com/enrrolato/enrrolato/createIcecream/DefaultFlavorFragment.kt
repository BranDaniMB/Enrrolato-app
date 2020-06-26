package com.enrrolato.enrrolato.createIcecream

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.enrrolato.enrrolato.PrincipalMenuFragment
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.database.Enrrolato
import com.enrrolato.enrrolato.iceCream.Flavor

class DefaultFlavorFragment : Fragment() {

    private lateinit var flavorList: ArrayList<String>
    private lateinit var listFlavor: ArrayList<Flavor>
    private var enrrolato = Enrrolato.instance
    private lateinit var flavorSelected: String

    private lateinit var name: String
    private var licour: Boolean = true
    private var special: Boolean = true
    private var exclusive: Boolean = true
    private var avaliable: Boolean = true

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

        chargeSpecial(spSpecial)

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

    private fun chargeSpecial(f: Spinner) {
            listFlavor = enrrolato.listFlavors
            var list: ArrayList<Flavor> = ArrayList()
            flavorList = ArrayList()

            flavorList.add("Seleccione helado")

            for (list in listFlavor) {
                name = list.name
                licour = list.isLiqueur
                special = list.isSpecial
                exclusive = list.isExclusive
                avaliable = list.avaliable

                if (special  && !licour && avaliable) {
                    flavorList.add(name)
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

    private fun chargeSeason(flavor: Spinner) {

    }

    private fun nextStepTopping() {

        if(flavorSelected.equals("Seleccione helado") || flavorSelected.isEmpty()) {
            errorFlavor("Debe seleccionar un helado")
        }
        else {
            // BRINCA DIRECTAMENTE A ESCOGER EL TOPPING
            // TIENE QUE LLEVARSE EL SABOR / HELADO YA DEFINIDO
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
        val popupMaxFlavor = layoutInflater.inflate(R.layout.popup_choose_flavor, null)
        val message = popupMaxFlavor.findViewById<View>(R.id.txtMessage) as TextView
        message.text = msg
        val bt_ok: Button = popupMaxFlavor.findViewById(R.id.btOk);
        alertDialogBuilder?.setView(popupMaxFlavor)
        val alertDialog: AlertDialog = alertDialogBuilder!!.create()
        alertDialog.window?.attributes!!.windowAnimations = R.style.alert_dialog
        alertDialog.show()

        bt_ok.setOnClickListener {
            alertDialog.cancel()
        }
    }


}