package com.enrrolato.enrrolato.createIcecream

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
import com.enrrolato.enrrolato.database.Enrrolato
import com.enrrolato.enrrolato.objects.Flavor

class FlavorsFragment : Fragment() {

    private lateinit var flavorList: ArrayList<String>
    private lateinit var listFlavor: ArrayList<Flavor>
    private var enrrolato = Enrrolato.instance
    private lateinit var flavorSelected: String
    private var listToRecycler: ArrayList<String> = ArrayList()
    private var listLiquour: ArrayList<String> = ArrayList()
    private var count: Int = 0
    private lateinit var mLayoutManager: LinearLayoutManager
    private var listAux: ArrayList<Flavor> = enrrolato.listFlavors

    private lateinit var backDefault: ImageButton
    private lateinit var flavor: Spinner
    private lateinit var trad: RadioButton
    private lateinit var lic: RadioButton
    private lateinit var recyclerFlavors: RecyclerView
    private lateinit var next: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_flavors, container, false)
        backDefault = view.findViewById(R.id.btBackToDefault)
        flavor = view.findViewById(R.id.spFlavor)
        trad = view.findViewById(R.id.rbTradicional)
        lic = view.findViewById(R.id.rbLicour)
        recyclerFlavors = view.findViewById(R.id.choosenFlavors)
        next = view.findViewById(R.id.btContinue)

        loadFlavors()
        chargeStyle()

        trad.setOnClickListener {
          filtrerTrad()
        }

        lic.setOnClickListener {
            filtrerLic()
        }

        backDefault.setOnClickListener {
            backToDefault()
        }

        next.setOnClickListener {
            nextStep()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun backToDefault() {
        val fragment = DefaultFlavorFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_flavor, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun nextStep() {
        if(recyclerFlavors == null || flavorSelected.equals(getString(R.string.flavor_selector)) || flavorSelected.isEmpty() || count == 0) {
            errorFlavor(getString(R.string.no_flavor))
        }
        else {
            val fragment = FillingFragment()
            val fm = requireActivity().supportFragmentManager
            val transaction = fm.beginTransaction()
            transaction.replace(R.id.ly_flavor, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun loadFlavors() {
        listFlavor = enrrolato.listFlavors
        flavorList = ArrayList()

        flavorList.add(getString(R.string.flavor_selector))

        for (list in listFlavor) {

            if (!list.isSpecial || (list.isSpecial && list.isLiqueur) && !list.isExclusive && list.avaliable) {
                flavorList.add(list.name)
            }

            if ((list.isSpecial && list.isLiqueur) && !list.isExclusive && list.avaliable) {
                listLiquour.add(list.name)
            }
        }
        fillSpinner()
    }

    private fun filtrerTrad() {
        listFlavor = enrrolato.listFlavors
        flavorList = ArrayList()
        flavorList.add(getString(R.string.flavor_selector))

        for(list in listFlavor) {

            if (!list.isSpecial && !list.isExclusive && list.avaliable) {
                flavorList.add(list.name)
            }
        }
        fillSpinner()
    }

    private fun filtrerLic() {
        listFlavor = enrrolato.listFlavors
        flavorList = ArrayList()

        flavorList.add(getString(R.string.flavor_selector))

        for(list in listFlavor) {

            if ((list.isSpecial && list.isLiqueur) && !list.isExclusive && list.avaliable) {
                flavorList.add(list.name)

                if(!listLiquour.contains(list.name)) {
                    listLiquour.add(list.name)
                }
            }
        }
        fillSpinner()
    }

    private fun fillSpinner() {
        val array: ArrayAdapter<String> = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, flavorList)
        flavor.adapter = array
        flavor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(av: AdapterView<*>?, view: View?, i: Int, p3: Long) {
                flavorSelected = av?.getItemAtPosition(i).toString()

                if(!flavorSelected.equals(getString(R.string.flavor_selector))) {
                    chooseFlavor()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun flavorProcess(f: String) {
        for(l in listAux) {
            if(l.name.equals(f)) {
                enrrolato.createIceCream().addFlavor(l)
            }
        }
    }

    private fun flavorProcessRemove(f: String) {
        for(l in listAux) {
            if(l.name.equals(f)) {
                enrrolato.createIceCream().removeFlavor(l)
            }
        }
    }

    // Este es el recycler view que manda los sabores a la lista y los muestra
    private fun chooseFlavor() {
        var alert = view?.findViewById<View>(R.id.txtLicourAlert)

        if(count < 3) {
            recyclerFlavors.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            var  af: AdapterIceCream

            if(!listToRecycler.contains(flavorSelected)) {
                listToRecycler.add(flavorSelected)

                // AQUI VA A BUSCAR DE LA LISTA GRANDE Y MANDARLA AL MANAGER PARA AGREGARLA
                flavorProcess(flavorSelected)

                if(listLiquour.contains(flavorSelected)) {
                    alert?.visibility = View.VISIBLE
                }

                af = AdapterIceCream(listToRecycler)

                // ELIMINAR UN SABOR
                af.setOnItemClickListener(object: AdapterIceCream.OnItemClickListener {
                    override fun onDeleteClick(position: Int) {

                        if(listToRecycler.size == 1 || listToRecycler.size == 0) {
                            flavorProcessRemove(af.list[0])
                            Toast.makeText(context, "Se eliminó " + af.list[0], Toast.LENGTH_SHORT).show()
                            listToRecycler.removeAt(0)
                            af.notifyItemRemoved(0)
                        }
                        else {
                            flavorProcessRemove(af.list[position])
                            Toast.makeText(context, "Se eliminó " + af.list[position], Toast.LENGTH_SHORT).show()
                            listToRecycler.removeAt(position)
                            af.notifyItemRemoved(position)
                            af.itemCount - 1
                        }

                        recyclerFlavors.setItemViewCacheSize(listToRecycler.size)
                        count -= 1
                        alert?.visibility = View.INVISIBLE
                    }
                })

                recyclerFlavors.adapter = af
                count += 1
            }
            else {
                errorFlavor(getString(R.string.duplicated_flavor))
            }
        }
        else {
            errorFlavor(getString(R.string.max_flavor))
        }
    }

    private fun chargeStyle() {
        mLayoutManager = LinearLayoutManager(context)
        recyclerFlavors.setHasFixedSize(true)
        recyclerFlavors.itemAnimator = DefaultItemAnimator()
        recyclerFlavors.layoutManager = mLayoutManager
        recyclerFlavors.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun errorFlavor(msg: String) {
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