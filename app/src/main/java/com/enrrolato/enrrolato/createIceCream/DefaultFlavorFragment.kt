package com.enrrolato.enrrolato.createIceCream

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.enrrolato.enrrolato.PrincipalMenuFragment
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.manager.Enrrolato
import com.enrrolato.enrrolato.manager.Steps

class DefaultFlavorFragment : Fragment() {
    private var enrrolato = Enrrolato.instance
    private var flavorSelected: String? = null

    private lateinit var backPrincipal: ImageButton
    private lateinit var makeIceCream: Button
    private lateinit var spSpecial: Spinner
    private lateinit var spSeason: Spinner
    private lateinit var next: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_default_flavor, container, false)
        backPrincipal = view.findViewById(R.id.btBackToPrincipal)
        makeIceCream = view.findViewById(R.id.btCreateOurIcecream)
        spSpecial = view.findViewById(R.id.spDefaultFlavor)
        spSeason = view.findViewById(R.id.spSeasonFlavor)
        next = view.findViewById(R.id.btNext)

        loadSpecial()
        loadSeason()

        backPrincipal.setOnClickListener {
            backToPrincipal()
        }

        makeIceCream.setOnClickListener {
            makeOwns()
        }

        next.setOnClickListener {
            nextStep()
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
        flavorSelected?.let {
            confirmSelection();
        }?:run {
            goToMake();
        }
    }

    private fun goToMake() {
        enrrolato.manager.isPredefined = null;
        val fragment = when(enrrolato.manager.getNextStep()) {
            Steps.FLAVORS -> FlavorsFragment()
            Steps.FILLINGS -> FillingFragment()
            Steps.TOPPINGS -> ToppingFragment()
            Steps.CONTAINERS -> ContainerFragment()
            else -> DefaultFlavorFragment()
        }
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_default, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun confirmSelection() {
        val alertDialogBuilder = context?.let {
            AlertDialog.Builder(it, R.style.alert_dialog)
        }
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)

        // Construct
        val popupConfirmExitView =
            layoutInflater.inflate(R.layout.popup_confirmation_message, null)
        val btOk: Button = popupConfirmExitView.findViewById(R.id.btOk);
        val btCancel: Button = popupConfirmExitView.findViewById(R.id.btCancel);
        val text: TextView = popupConfirmExitView.findViewById(R.id.txtConfirmMessage)
        text.text = getString(R.string.predefined_select_warn)

        // Set
        alertDialogBuilder?.setView(popupConfirmExitView)
        val alertDialog: AlertDialog = alertDialogBuilder!!.create()
        alertDialog.window?.attributes!!.windowAnimations = R.style.alert_dialog
        alertDialog.show()

        btOk.setOnClickListener {
            alertDialog.cancel()
            goToMake();
        }

        btCancel.setOnClickListener {
            alertDialog.cancel()
        }
    }

    private fun loadSpecial() {
        val temp = ArrayList<String>()
        temp.add(getString(R.string.preset_flavor_list))

        for (list in enrrolato.listPredefinedIceCream.values) {
            if (list.isSpecial && list.available && !list.isSeason) {
                temp.add(list.name)
            }
        }
        fillSpinner(spSpecial, temp)
    }

    private fun loadSeason() {
        val temp = ArrayList<String>()
        temp.add(getString(R.string.season_flavor_list))

        for (list in enrrolato.listPredefinedIceCream.values) {
            if (list.available && list.isSeason) {
                temp.add(list.name)
            }
        }
        fillSpinner(spSeason, temp)
    }

    private fun fillSpinner(flavor: Spinner, list: ArrayList<String>) {
        if (list.size <= 1) {
            list.clear();
            list.add(getString(R.string.no_elements));
        }

        val array: ArrayAdapter<String> =
            ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, list)
        flavor.adapter = array
        flavor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(av: AdapterView<*>?, view: View?, i: Int, p3: Long) {
                val it = av?.getItemAtPosition(i).toString()
                if (it != getString(R.string.season_flavor_list)
                    && it != getString(R.string.preset_flavor_list)
                    && it != getString(R.string.no_elements)) {
                    flavorSelected = it;
                    enrrolato.listPredefinedIceCream[it]?.let {
                        if (it.isSeason) {
                            loadSpecial();
                        } else {
                            loadSeason();
                        }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun choosePredefined(f: String) {
        for (predefined in enrrolato.listPredefinedIceCream.values) {
            if (predefined.name == f) {
                enrrolato.manager.fillIngredients(predefined);
            }
        }
    }

    private fun nextStep() {
        flavorSelected?.let {
            var fragment: Fragment? = null;
            fragment = when(enrrolato.manager.getNextStep()) {
                Steps.FLAVORS -> FlavorsFragment()
                Steps.FILLINGS -> FillingFragment()
                Steps.TOPPINGS -> ToppingFragment()
                Steps.CONTAINERS -> ContainerFragment()
                else -> DefaultFlavorFragment()
            }
            choosePredefined(flavorSelected!!)
            val fm = requireActivity().supportFragmentManager
            val transaction = fm.beginTransaction()
            transaction.replace(R.id.ly_default, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }?:run {
            errorFlavor(getString(R.string.no_preset))
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