package com.enrrolato.enrrolato.createIceCream

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enrrolato.enrrolato.adapter.AdapterIceCream
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.manager.Enrrolato
import com.enrrolato.enrrolato.manager.SelectionTypes
import com.enrrolato.enrrolato.manager.Steps

class FlavorsFragment : Fragment() {
    private var enrrolato = Enrrolato.instance
    private var listToRecycler: ArrayList<String> = ArrayList()
    private lateinit var mLayoutManager: LinearLayoutManager

    private lateinit var backDefault: ImageButton
    private lateinit var flavor: Spinner
    private lateinit var radioGroup: RadioGroup
    private lateinit var all: RadioButton
    private lateinit var trad: RadioButton
    private lateinit var lic: RadioButton
    private lateinit var recyclerViewFlavors: RecyclerView
    private lateinit var next: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_flavors, container, false)
        backDefault = view.findViewById(R.id.btBackToDefault)
        flavor = view.findViewById(R.id.spFlavor)
        radioGroup = view.findViewById(R.id.rgFlavor)
        all = view.findViewById(R.id.showAllFlavors)
        trad = view.findViewById(R.id.rbTradicional)
        lic = view.findViewById(R.id.rbLicour)
        recyclerViewFlavors = view.findViewById(R.id.selectedFlavors)
        next = view.findViewById(R.id.btContinue)

        chargeStyle()

        if (enrrolato.manager.selectedFlavorsList.isNotEmpty()) {
            loadAlreadySelected();
        }

        all.setOnClickListener {
            loadFlavors()
        }

        trad.setOnClickListener {
            filterTrad()
        }

        lic.setOnClickListener {
            filterLic()
        }

        backDefault.setOnClickListener {
            backToDefault()
        }

        next.setOnClickListener {
            nextStep()
        }

        radioGroup.check(all.id)
        all.performClick();
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun loadAlreadySelected() {
        for (flavor in enrrolato.manager.selectedFlavorsList.keys) {
            addToRecycler(flavor);
        }
    }

    private fun backToDefault() {
        val fragment = when (enrrolato.manager.takeStepBack()) {
            Steps.FLAVORS -> FlavorsFragment()
            Steps.FILLINGS -> FillingFragment()
            Steps.TOPPINGS -> ToppingFragment()
            Steps.CONTAINERS -> ContainerFragment()
            else -> DefaultFlavorFragment()
        }
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_flavor, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun nextStep() {
        if (enrrolato.manager.selectedFlavorsList.size >= 1) {
            val fragment = when (enrrolato.manager.getNextStep()) {
                Steps.FLAVORS -> FlavorsFragment()
                Steps.FILLINGS -> FillingFragment()
                Steps.TOPPINGS -> ToppingFragment()
                Steps.CONTAINERS -> ContainerFragment()
                else -> DefaultFlavorFragment()
            }
            val fm = requireActivity().supportFragmentManager
            val transaction = fm.beginTransaction()
            transaction.replace(R.id.ly_flavor, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        } else {
            errorPopup("Debes elegir al menos un sabor.");
        }
    }

    private fun loadFlavors() {
        val leakedFlavorList = ArrayList<String>()
        leakedFlavorList.add(getString(R.string.flavor_selector));

        when (enrrolato.manager.isPredefinedAndAllowsAnyFlavor()) {
            SelectionTypes.IS_PREDEFINED_AND_ALLOWS_ANY, SelectionTypes.NOT_PREDEFINED -> {
                for (flavor in enrrolato.listFlavors.values) {
                    if (!flavor.isExclusive
                        && flavor.avaliable
                        && !enrrolato.manager.selectedFlavorsList.containsKey(flavor.name)
                    ) {
                        leakedFlavorList.add(flavor.name)
                    }
                }
            }
            SelectionTypes.IS_PREDEFINED_AND_NOT_ALLOWS_ANY -> {
                for (flavor in enrrolato.manager.getPredefinedFlavors()) {
                    if (flavor.avaliable
                        && !enrrolato.manager.selectedFlavorsList.containsKey(flavor.name)
                    ) {
                        leakedFlavorList.add(flavor.name)
                    }
                }
            }
        }
        fillSpinner(leakedFlavorList)
    }

    private fun filterTrad() {
        val leakedFlavorList = ArrayList<String>()
        leakedFlavorList.add(getString(R.string.flavor_selector));

        when (enrrolato.manager.isPredefinedAndAllowsAnyFlavor()) {
            SelectionTypes.NOT_PREDEFINED, SelectionTypes.IS_PREDEFINED_AND_ALLOWS_ANY -> {
                for (flavor in enrrolato.listFlavors.values) {
                    if (!flavor.isExclusive
                        && flavor.avaliable
                        && !flavor.isLiqueur
                        && !enrrolato.manager.selectedFlavorsList.containsKey(flavor.name)
                    ) {
                        leakedFlavorList.add(flavor.name)
                    }
                }
            }
            SelectionTypes.IS_PREDEFINED_AND_NOT_ALLOWS_ANY -> {
                for (flavor in enrrolato.manager.getPredefinedFlavors()) {
                    if (flavor.avaliable
                        && !flavor.isLiqueur
                        && !enrrolato.manager.selectedFlavorsList.containsKey(flavor.name)
                    ) {
                        leakedFlavorList.add(flavor.name)
                    }
                }
            }
        }

        fillSpinner(leakedFlavorList)
    }

    private fun filterLic() {
        val leakedFlavorList = ArrayList<String>()
        leakedFlavorList.add(getString(R.string.flavor_selector));

        when (enrrolato.manager.isPredefinedAndAllowsAnyFlavor()) {
            SelectionTypes.NOT_PREDEFINED, SelectionTypes.IS_PREDEFINED_AND_ALLOWS_ANY -> {
                for (flavor in enrrolato.listFlavors.values) {
                    if (!flavor.isExclusive
                        && flavor.avaliable
                        && flavor.isLiqueur
                        && !enrrolato.manager.selectedFlavorsList.containsKey(flavor.name)
                    ) {
                        leakedFlavorList.add(flavor.name)
                    }
                }
            }
            SelectionTypes.IS_PREDEFINED_AND_NOT_ALLOWS_ANY -> {
                for (flavor in enrrolato.manager.getPredefinedFlavors()) {
                    if (flavor.avaliable
                        && flavor.isLiqueur
                        && !enrrolato.manager.selectedFlavorsList.containsKey(flavor.name)
                    ) {
                        leakedFlavorList.add(flavor.name)
                    }
                }
            }
        }

        fillSpinner(leakedFlavorList)
    }

    private fun fillSpinner(leakedFlavorList: ArrayList<String>) {
        if (leakedFlavorList.size <= 1) {
            leakedFlavorList.clear();
            leakedFlavorList.add(getString(R.string.no_elements));
        }

        val array: ArrayAdapter<String> =
            ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, leakedFlavorList)
        flavor.adapter = array
        flavor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(av: AdapterView<*>?, view: View?, i: Int, p3: Long) {
                val it = av?.getItemAtPosition(i).toString();
                if (it != getString(R.string.flavor_selector) && it != getString(R.string.no_elements)) {
                    chooseFlavor(it);
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun chooseFlavor(flavor: String) {
        if (enrrolato.manager.selectedFlavorsList.size < enrrolato.prices.flavorAmount) {
            if (!listToRecycler.contains(flavor)) {
                addToRecycler(flavor);
                enrrolato.manager.addFlavor(flavor);
            } else {
                errorPopup(getString(R.string.duplicated_flavor))
            }
        } else {
            errorPopup(getString(R.string.max_flavor))
        }
    }

    private fun addToRecycler(flavor: String) {
        if (!listToRecycler.contains(flavor)) {
            val alert = view?.findViewById<View>(R.id.txtLicourAlert)
            recyclerViewFlavors.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            listToRecycler.add(flavor)

            if (enrrolato.listFlavors[flavor]?.isLiqueur!!) {
                alert?.visibility = View.VISIBLE
            }

            val af: AdapterIceCream = AdapterIceCream(listToRecycler)

            // ELIMINAR UN SABOR
            af.setOnItemClickListener(object : AdapterIceCream.OnItemClickListener {
                override fun onDeleteClick(position: Int) {
                    if (listToRecycler.size <= 1) {
                        enrrolato.manager.removeFlavor(af.list[0])
                        Toast.makeText(
                            context,
                            "Se eliminó " + af.list[0],
                            Toast.LENGTH_SHORT
                        ).show()
                        listToRecycler.removeAt(0)
                        af.notifyItemRemoved(0)
                    } else {
                        enrrolato.manager.removeFlavor(af.list[position])
                        Toast.makeText(
                            context,
                            "Se eliminó " + af.list[position],
                            Toast.LENGTH_SHORT
                        ).show()
                        listToRecycler.removeAt(position)
                        af.notifyItemRemoved(position)
                        af.itemCount - 1
                    }

                    recyclerViewFlavors.setItemViewCacheSize(listToRecycler.size)
                    if (!enrrolato.manager.checkLiquor()) {
                        alert?.visibility = View.INVISIBLE
                    }

                    reloadSpinner();
                }
            })
            recyclerViewFlavors.adapter = af
        }
    }

    private fun chargeStyle() {
        mLayoutManager = LinearLayoutManager(context)
        recyclerViewFlavors.setHasFixedSize(true)
        recyclerViewFlavors.itemAnimator = DefaultItemAnimator()
        recyclerViewFlavors.layoutManager = mLayoutManager
        recyclerViewFlavors.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun reloadSpinner() {
        when (radioGroup.checkedRadioButtonId) {
            all.id -> loadFlavors()
            trad.id -> filterTrad()
            lic.id -> filterLic()
        }
    }

    private fun errorPopup(msg: String) {
        val alertDialogBuilder = context?.let { AlertDialog.Builder(it, R.style.alert_dialog) }
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val popupMaxFlavor = layoutInflater.inflate(R.layout.popup_alert_message, null)
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