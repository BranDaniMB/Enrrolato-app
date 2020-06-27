package com.enrrolato.enrrolato.createIcecream

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bolts.Bolts
import com.enrrolato.enrrolato.AdapterIceCream
import com.enrrolato.enrrolato.PrincipalMenuFragment
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.database.Enrrolato
import com.enrrolato.enrrolato.iceCream.Flavor
import com.enrrolato.enrrolato.iceCream.Topping
import com.facebook.FacebookSdk.getApplicationContext
import kotlinx.android.synthetic.main.fragment_topping.*
import kotlin.properties.Delegates

class ToppingFragment : Fragment() {

    private lateinit var listTopping: ArrayList<Topping>
    private lateinit var toppingList: ArrayList<String>
    private var enrrolato = Enrrolato.instance
    private var listToRecycler: ArrayList<String> = ArrayList()
    private lateinit var toppingSelected: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_topping, container, false)
        //var back = view.findViewById<View>(R.id.btBackToFilling) as ImageButton
        var spinner = view.findViewById<View>(R.id.spTopping) as Spinner
        var fill = view.findViewById<View>(R.id.choosenTopping) as RecyclerView
        var next = view.findViewById<View>(R.id.btCotinueToContainer) as Button

        chargeTopping(spinner, fill)

        //back.setOnClickListener {
        //    back()
        //}

        next.setOnClickListener {
          selectContainer(fill)
        }

        return view
    }

    private fun chargeTopping(spT: Spinner, rv: RecyclerView) {
        listTopping = enrrolato.listToppings
        var list: ArrayList<Flavor> = ArrayList()
        toppingList = ArrayList()

        toppingList.add("Seleccione topping")

        for(list in listTopping) {

            if(list.available) {
                toppingList.add(list.name)
            }
        }
        fillSpinner(spT, rv)
    }

    private fun back() {
        val fragment = PrincipalMenuFragment() // NO VA A PRINCIPAL SINO A FILLING
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_topping, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun fillSpinner(topping: Spinner, rv: RecyclerView) {
        val array: ArrayAdapter<String> = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, toppingList)
        topping.adapter = array
        topping.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(av: AdapterView<*>?, view: View?, i: Int, p3: Long) {
                toppingSelected = av?.getItemAtPosition(i).toString()

                if(!toppingSelected.equals("Seleccione topping")) {
                    chooseTopping(rv)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun selectContainer(rv: RecyclerView) {
        if (rv != null) {
            val fragment = SelectContainerFragment()
            val fm = requireActivity().supportFragmentManager
            val transaction = fm.beginTransaction()
            transaction.replace(R.id.ly_topping, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        } else {
            errorTopping("Debe seleccionar algún topping")
        }
    }

    private fun chooseTopping(recyclerToppings: RecyclerView) {
        recyclerToppings.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        var af: AdapterIceCream

        if (!listToRecycler.contains(toppingSelected)) {

            listToRecycler.add(toppingSelected)
            af = AdapterIceCream(listToRecycler)
            recyclerToppings.adapter = af
        } else {
            errorTopping("Ya seleccionó este topping")
        }
    }

    private fun errorTopping(msg: String) {
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