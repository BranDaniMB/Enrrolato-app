package com.enrrolato.enrrolato.createIceCream

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enrrolato.enrrolato.adapter.AdapterIceCream
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.manager.Enrrolato
import com.enrrolato.enrrolato.manager.SelectionTypes
import com.enrrolato.enrrolato.manager.Steps


class ToppingFragment : Fragment() {
    private var enrrolato = Enrrolato.instance
    private var listToRecycler: ArrayList<String> = ArrayList()
    private var count: Int = 0
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var back: ImageButton
    private lateinit var af: AdapterIceCream

    private lateinit var spinner: Spinner
    private lateinit var fill: RecyclerView
    private lateinit var next: Button
    private lateinit var msg: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_topping, container, false)
        spinner = view.findViewById(R.id.spTopping)
        fill = view.findViewById(R.id.choosenTopping)
        next = view.findViewById(R.id.btCotinueToContainer)
        msg = view.findViewById(R.id.txtToppingAlert)
        back = view.findViewById(R.id.backBtn)

        chargeStyle()
        loadToppings()

        if (enrrolato.manager.selectedToppingList.isNotEmpty()) {
            loadAlreadySelected();
        }

        back.setOnClickListener {
            goBack()
        }

        next.setOnClickListener {
            nextStep()
        }

        return view
    }

    private fun loadAlreadySelected() {
        for (topping in enrrolato.manager.selectedToppingList.keys) {
            addToRecycler(topping);
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
    }

    private fun loadToppings() {
        val leakedList = ArrayList<String>();
        leakedList.add(getString(R.string.topping_selector));

        when (enrrolato.manager.isPredefinedAndAllowsAnyTopping()) {
            SelectionTypes.IS_PREDEFINED_AND_NOT_ALLOWS_ANY -> {
                for (topping in enrrolato.manager.getPredefinedTopping()) {
                    if (topping.available
                        && !enrrolato.manager.selectedToppingList.containsKey(topping.name)
                    ) {
                        leakedList.add(topping.name);
                    }
                }
            }
            SelectionTypes.IS_PREDEFINED_AND_ALLOWS_ANY, SelectionTypes.NOT_PREDEFINED -> {
                for (topping in enrrolato.listToppings.values) {
                    if (!topping.isExclusive
                        && topping.available
                        && !enrrolato.manager.selectedToppingList.containsKey(topping.name)
                    ) {
                        leakedList.add(topping.name);
                    }
                }
            }
        }


        fillSpinner(leakedList);
    }

    private fun fillSpinner(leakedList: ArrayList<String>) {
        if (leakedList.size <= 1) {
            leakedList.clear();
            leakedList.add(getString(R.string.no_elements));
        }

        val array: ArrayAdapter<String> =
            ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, leakedList)
        spinner.adapter = array
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(av: AdapterView<*>?, view: View?, i: Int, p3: Long) {
                val it = av?.getItemAtPosition(i).toString();
                if (it != getString(R.string.topping_selector) && it != getString(R.string.no_elements)) {
                    chooseTopping(it)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun chooseTopping(topping: String) {
        if (!listToRecycler.contains(topping)) {
            addToRecycler(topping)
            enrrolato.manager.addTopping(topping)
        } else {
            errorTopping(getString(R.string.duplicated_topping))
        }
    }

    private fun addToRecycler(topping: String) {
        if (!listToRecycler.contains(topping)) {
            listToRecycler.add(topping)
            af = AdapterIceCream(listToRecycler)

            af.setOnItemClickListener(object : AdapterIceCream.OnItemClickListener {
                override fun onDeleteClick(position: Int) {
                    if (listToRecycler.size == 1 || listToRecycler.size == 0) {
                        enrrolato.manager.removeTopping(af.list[0])
                        Toast.makeText(
                            context,
                            "Se eliminó " + af.list[0],
                            Toast.LENGTH_SHORT
                        ).show()
                        listToRecycler.removeAt(0)
                        af.notifyItemRemoved(0)
                    } else {
                        enrrolato.manager.removeTopping(af.list[position])
                        Toast.makeText(
                            context,
                            "Se eliminó " + af.list[position],
                            Toast.LENGTH_SHORT
                        ).show()
                        listToRecycler.removeAt(position)
                        af.notifyItemRemoved(position)
                        af.itemCount - 1
                    }

                    fill.setItemViewCacheSize(listToRecycler.size)
                    count -= 1

                    if (count < enrrolato.prices.toppingAmount || msg.visibility == View.VISIBLE) {
                        msg.visibility = View.INVISIBLE
                    }

                    loadToppings();
                }
            })

            count += 1
            fill.adapter = af

            if (count > 2) {
                if (msg.visibility == View.INVISIBLE) {
                    msg.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun chargeStyle() {
        mLayoutManager = LinearLayoutManager(context)
        fill.setHasFixedSize(true)
        fill.itemAnimator = DefaultItemAnimator()
        fill.layoutManager = mLayoutManager
        fill.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun errorTopping(msg: String) {
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