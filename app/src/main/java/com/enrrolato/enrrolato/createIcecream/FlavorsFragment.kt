package com.enrrolato.enrrolato.createIcecream

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enrrolato.enrrolato.adapter.AdapterIceCream
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.database.Enrrolato
import com.enrrolato.enrrolato.iceCream.Flavor


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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_flavors, container, false)
        val backDefault = view.findViewById<View>(R.id.btBackToDefault) as ImageButton
        val flavor = view.findViewById<View>(R.id.spFlavor) as Spinner
        val trad = view.findViewById<View>(R.id.rbTradicional) as RadioButton
        val lic = view.findViewById<View>(R.id.rbLicour) as RadioButton
        val recyclerFlavors = view.findViewById<View>(R.id.choosenFlavors) as RecyclerView
        val next = view.findViewById<View>(R.id.btContinue) as Button

        loadFlavors(flavor, recyclerFlavors)
        ll(recyclerFlavors)

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
        val fragment = DefaultFlavorFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_flavor, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun nextStep(rf: RecyclerView) {
        if(rf == null || flavorSelected.equals(getString(R.string.flavor_selector)) || flavorSelected.isEmpty() || count == 0) {
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

    private fun loadFlavors(f: Spinner, rv: RecyclerView) {
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
        fillSpinner(f, rv)
    }

    private fun filtrerTrad(f: Spinner, rv: RecyclerView) {
        listFlavor = enrrolato.listFlavors
        flavorList = ArrayList()
        flavorList.add(getString(R.string.flavor_selector))

        for(list in listFlavor) {

            if (!list.isSpecial && !list.isExclusive && list.avaliable) {
                flavorList.add(list.name)
            }
        }
        fillSpinner(f, rv)
    }

    private fun filtrerLic(f: Spinner, rv: RecyclerView) {
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
        fillSpinner(f, rv)
    }

    private fun fillSpinner(flavor: Spinner, rv: RecyclerView) {
        val array: ArrayAdapter<String> = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, flavorList)
        flavor.adapter = array
        flavor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(av: AdapterView<*>?, view: View?, i: Int, p3: Long) {
                flavorSelected = av?.getItemAtPosition(i).toString()

                if(!flavorSelected.equals(getString(R.string.flavor_selector))) {
                    chooseFlavor(rv)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
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
    private fun chooseFlavor(recyclerFlavors: RecyclerView) {
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

                af.setOnItemClickListener(object: AdapterIceCream.OnItemClickListener {
                    override fun onDeleteClick(position: Int) {

                        // SE CAE CUANDO SOLO QUEDA UN SABOR EN LA LISTA
                        flavorProcessRemove(af.list[position])

                        if(listToRecycler.size == 1 || listToRecycler.size == 0) {
                            listToRecycler.removeAt(0)
                            af.notifyItemRemoved(0)
                        }
                        else {
                            listToRecycler.removeAt(position)
                            af.notifyItemRemoved(position)
                            af.itemCount - 1
                        }

                        Toast.makeText(context, "HOLA " + af.itemCount, Toast.LENGTH_SHORT).show()
                        recyclerFlavors.setItemViewCacheSize(listToRecycler.size)
                        count -= 1
                        alert?.visibility = View.INVISIBLE
                    }
                })

               /*
                af.setOnClickListener(View.OnClickListener { v ->
                    //listToRecycler.get(recyclerFlavors.getChildAdapterPosition(v)).toString()
                    var i = recyclerFlavors.getChildAdapterPosition(v)
                    //popupMessage(i, af, getString(R.string.delete_flavor_prompt))

                    //listToRecycler.removeAt(i)
                    //af.notifyItemRemoved(i)

                      recyclerFlavors.setItemViewCacheSize(listToRecycler.size)

                    // AQUI VA A BUSCAR DE LA LISTA GRANDE Y MANDARLA AL MANAGER PARA ELIMINARLA
                    flavorProcessRemove(af.list.get(recyclerFlavors.getChildAdapterPosition(v)))

                    count -= 1
                    alert?.visibility = View.INVISIBLE
                })

                */

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

    private fun ll(rf: RecyclerView) {
        mLayoutManager = LinearLayoutManager(context)
        rf.setHasFixedSize(true)
        rf.itemAnimator = DefaultItemAnimator()
        rf.layoutManager = mLayoutManager
        rf.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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

    private fun popupMessage(i: Int, a: AdapterIceCream, m: String) {
        val alertDialogBuilder = context?.let { AlertDialog.Builder(it, R.style.alert_dialog) }
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val popupConfirmMessageView = layoutInflater.inflate(R.layout.popup_confirmation_message, null)
        val msg: TextView = popupConfirmMessageView.findViewById(R.id.txtConfirmMessage)
        msg.text = m
        val bt_ok: Button = popupConfirmMessageView.findViewById(R.id.btOk1);
        val bt_cancel: Button = popupConfirmMessageView.findViewById(R.id.btCancel1);
        alertDialogBuilder?.setView(popupConfirmMessageView)
        val alertDialog: AlertDialog = alertDialogBuilder!!.create()
        alertDialog.window?.attributes!!.windowAnimations = R.style.alert_dialog
        alertDialog.show()

        bt_ok.setOnClickListener {
            alertDialog.cancel()
            listToRecycler.removeAt(i)
            a.notifyItemRemoved(i)
        }

        bt_cancel.setOnClickListener {
            alertDialog.cancel()
        }
    }

}