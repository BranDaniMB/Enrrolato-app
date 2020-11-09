package com.enrrolato.enrrolato.createIceCream

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.adapter.AdapterIceCream
import com.enrrolato.enrrolato.manager.Enrrolato
import com.enrrolato.enrrolato.manager.SelectionTypes
import com.enrrolato.enrrolato.manager.Steps

class FillingFragment : Fragment() {
    private var enrrolato = Enrrolato.instance
    private var listToRecycler: ArrayList<String> = ArrayList()

    private lateinit var sp: Spinner
    private lateinit var back: ImageButton
    private lateinit var next: Button
    private lateinit var recyclerViewFillings: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_filling, container, false)
        sp = view.findViewById(R.id.spFilling)
        back = view.findViewById(R.id.backBtn)
        next = view.findViewById(R.id.btContinue)
        recyclerViewFillings = view.findViewById(R.id.selectedFillings)

        loadFillings()

        if (enrrolato.manager.selectedFillingList.isNotEmpty()) {
            loadAlreadySelected();
        }

        back.setOnClickListener {
            goBack();
        }

        next.setOnClickListener {
            nextStep();
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun loadAlreadySelected() {
        for (filling in enrrolato.manager.selectedFillingList.keys) {
            addToRecycler(filling);
        }
    }

    private fun goBack() {
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
        if (enrrolato.manager.selectedFillingList.size >= 1) {
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
            errorPopup("Debes elegir al menos un relleno.");
        }
    }

    private fun loadFillings() {
        val leakedList = ArrayList<String>();
        leakedList.add(getString(R.string.filling_selector));

        when (enrrolato.manager.isPredefinedAndAllowsAnyFilling()) {
            SelectionTypes.IS_PREDEFINED_AND_NOT_ALLOWS_ANY -> {
                for (filling in enrrolato.manager.getPredefinedFilling()) {
                    if (filling.available
                        && !enrrolato.manager.selectedFillingList.containsKey(filling.name)
                    ) {
                        leakedList.add(filling.name);
                    }
                }
            }
            SelectionTypes.IS_PREDEFINED_AND_ALLOWS_ANY, SelectionTypes.NOT_PREDEFINED -> {
                for (filling in enrrolato.listFillings.values) {
                    if (!filling.isExclusive
                        && filling.available
                        && !enrrolato.manager.selectedFillingList.containsKey(filling.name)
                    ) {
                        leakedList.add(filling.name);
                    }
                }
            }
        }


        fillFilling(leakedList)
    }

    private fun fillFilling(leakedList: ArrayList<String>) {
        if (leakedList.size <= 1) {
            leakedList.clear();
            leakedList.add(getString(R.string.no_elements));
        }

        val array: ArrayAdapter<String> =
            ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, leakedList)
        sp.adapter = array
        sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(av: AdapterView<*>?, view: View?, i: Int, p3: Long) {
                val it = av?.getItemAtPosition(i).toString();
                if (it != getString(R.string.filling_selector) && it != getString(R.string.no_elements)) {
                    chooseFilling(it);
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun chooseFilling(filling: String) {
        if (enrrolato.manager.selectedFillingList.size < enrrolato.prices.fillingAmount) {
            if (!listToRecycler.contains(filling)) {
                addToRecycler(filling)
                enrrolato.manager.addFilling(filling);
            } else {
                errorPopup(getString(R.string.duplicated_filling))
            }
        } else {
            errorPopup(getString(R.string.max_filling))
        }
    }

    private fun addToRecycler(filling: String) {
        if (!listToRecycler.contains(filling)) {
            recyclerViewFillings.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            listToRecycler.add(filling)
            val af: AdapterIceCream = AdapterIceCream(listToRecycler)

            // ELIMINAR UN SABOR
            af.setOnItemClickListener(object : AdapterIceCream.OnItemClickListener {
                override fun onDeleteClick(position: Int) {
                    if (listToRecycler.size == 1 || listToRecycler.size == 0) {
                        enrrolato.manager.removeFilling(af.list[0])
                        Toast.makeText(
                            context,
                            "Se eliminó " + af.list[0],
                            Toast.LENGTH_SHORT
                        ).show()
                        listToRecycler.removeAt(0)
                        af.notifyItemRemoved(0)
                    } else {
                        enrrolato.manager.removeFilling(af.list[position])
                        Toast.makeText(
                            context,
                            "Se eliminó " + af.list[position],
                            Toast.LENGTH_SHORT
                        ).show()
                        listToRecycler.removeAt(position)
                        af.notifyItemRemoved(position)
                        af.itemCount - 1
                    }

                    recyclerViewFillings.setItemViewCacheSize(listToRecycler.size);

                    loadFillings();
                }
            })
            recyclerViewFillings.adapter = af
        }
    }

    private fun errorPopup(msg: String) {
        val alertDialogBuilder = context?.let { AlertDialog.Builder(it, R.style.alert_dialog) }
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val popupMaxFlavor = layoutInflater.inflate(R.layout.popup_alert_message, null)
        val message = popupMaxFlavor.findViewById<View>(R.id.txtMessage) as TextView
        message.text = msg
        val btOK: Button = popupMaxFlavor.findViewById(R.id.btOk);
        alertDialogBuilder?.setView(popupMaxFlavor)
        val alertDialog: AlertDialog = alertDialogBuilder!!.create()
        alertDialog.window?.attributes!!.windowAnimations = R.style.alert_dialog
        alertDialog.show()

        btOK.setOnClickListener {
            alertDialog.cancel()
        }
    }
}