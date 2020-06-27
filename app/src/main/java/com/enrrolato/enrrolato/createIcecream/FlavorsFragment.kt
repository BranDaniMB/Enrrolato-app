package com.enrrolato.enrrolato.createIcecream

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enrrolato.enrrolato.AdapterIceCream
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.database.Enrrolato
import com.enrrolato.enrrolato.iceCream.Flavor


class FlavorsFragment : Fragment() {

    private lateinit var flavorList: ArrayList<String>
    private lateinit var listFlavor: ArrayList<Flavor>
    private var enrrolato = Enrrolato.instance
    private lateinit var flavorSelected: String
    private var listToRecycler: ArrayList<String> = ArrayList()
    private var count: Int = 0

    private lateinit var name: String
    private var licour: Boolean = true
    private var special: Boolean = true
    private var exclusive: Boolean = true
    private var avaliable: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_flavors, container, false)
        val backDefault = view.findViewById<View>(R.id.btBackToDefault) as ImageButton
        val flavor = view.findViewById<View>(R.id.spFlavor) as Spinner
        val trad = view.findViewById<View>(R.id.rbTradicional) as RadioButton
        val lic = view.findViewById<View>(R.id.rbLicour) as RadioButton
        val recyclerFlavors = view.findViewById<View>(R.id.choosenFlavors) as RecyclerView
        val next = view.findViewById<View>(R.id.btContinue) as Button

        chargeFlavors(flavor, recyclerFlavors)

        trad.setOnClickListener {
          filtrerTrad(flavor, recyclerFlavors)
        }

        lic.setOnClickListener {
            filtrerLic(flavor, recyclerFlavors)
        }

        backDefault.setOnClickListener {
            backToDefault()
        }

        next.setOnClickListener {
            nextStep(recyclerFlavors)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun backToDefault() {
        val fragment =
            DefaultFlavorFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_flavor, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun nextStep(rf: RecyclerView) {
        if(rf == null || flavorSelected.equals("Seleccione sabor") || flavorSelected.isEmpty()) {
            errorFlavor("No se han seleccionado sabores")
        }
        else {
            // DIRIGIRSE A LA PANTALLA DE SELECCIÓN DE JARABES
        }
    }

    private fun chargeFlavors(f: Spinner, rv: RecyclerView) {
        listFlavor = enrrolato.listFlavors
        var list: ArrayList<Flavor> = ArrayList()
        flavorList = ArrayList()

        flavorList.add("Seleccione sabor")

        for (list in listFlavor) {
            name = list.name
            licour = list.isLiqueur
            special = list.isSpecial
            exclusive = list.isExclusive
            avaliable = list.avaliable

            if (!special || (special && licour) && !exclusive && avaliable) {
                flavorList.add(name)
            }
        }

        fillSpinner(f, rv)
    }

    private fun fillSpinner(flavor: Spinner, rv: RecyclerView) {
        val array: ArrayAdapter<String> = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, flavorList)
        flavor.adapter = array
        flavor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(av: AdapterView<*>?, view: View?, i: Int, p3: Long) {
                flavorSelected = av?.getItemAtPosition(i).toString()

                if(!flavorSelected.equals("Seleccione sabor")) {
                    chooseFlavor(rv)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun filtrerTrad(f: Spinner, rv: RecyclerView) {
        listFlavor = enrrolato.listFlavors
        var list: ArrayList<Flavor>
        flavorList = ArrayList()

        flavorList.add("Seleccione sabor")

        for(list in listFlavor) {
            name = list.name
            licour = list.isLiqueur
            special = list.isSpecial
            exclusive = list.isExclusive
            avaliable = list.avaliable

            if (!special && !exclusive && avaliable) {
                flavorList.add(name)
            }
        }
        fillSpinner(f, rv)
    }

    private fun filtrerLic(f: Spinner, rv: RecyclerView) {
        listFlavor = enrrolato.listFlavors
        var list: ArrayList<Flavor>
        flavorList = ArrayList()

        flavorList.add("Seleccione sabor")

        for(list in listFlavor) {
            name = list.name
            licour = list.isLiqueur
            special = list.isSpecial
            exclusive = list.isExclusive
            avaliable = list.avaliable

            if ((special && licour) && !exclusive && avaliable) {
                flavorList.add(name)
            }
        }
        fillSpinner(f, rv)
    }

    private fun flavorProcess() {
        // CODE HERE
    }

    private fun chooseFlavor(recyclerFlavors: RecyclerView) {
        if(count < 3) {
            recyclerFlavors.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            var  af: AdapterIceCream

            if(!listToRecycler.contains(flavorSelected)) {

                listToRecycler.add(flavorSelected)
                af = AdapterIceCream(listToRecycler)
                recyclerFlavors.adapter = af
                count += 1
            }
            else {
                errorFlavor("Ya seleccionó este sabor")
            }
        }
        else {
            errorFlavor("Usted excedió el máximo de sabores")
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

/* database.child("business").child("ingredients").child("flavors").addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (data: DataSnapshot in dataSnapshot.children) {
                        var id: String = data.key.toString()
                        var name: String = data.child("name").getValue().toString()
                        var licour: String = data.child("isLiqueur").getValue().toString()
                        var special: String = data.child("isSpecial").getValue().toString()
                        var exclusive: String = data.child("isExclusive").getValue().toString()
                        var avaliable: String = data.child("avaliable").getValue().toString()

                        if(special.equals("0") || (special.equals("1") && licour.equals("1")) && exclusive.equals("0")) {
                            flavorList = ArrayList()
                            flavorList.add(name)

                            // VAN LOS FILTROS
                        }
                    }
                    fillSpinner(f)
                }
            }
            override fun onCancelled(ds: DatabaseError) {
            }
        })  */