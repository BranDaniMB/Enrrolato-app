package com.enrrolato.enrrolato.createIcecream

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enrrolato.enrrolato.AdapterIceCream
import com.enrrolato.enrrolato.PrincipalMenuFragment
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.database.Enrrolato
import com.enrrolato.enrrolato.iceCream.Flavor
import com.enrrolato.enrrolato.iceCream.Topping
import org.w3c.dom.Text


class ToppingFragment : Fragment() {

    private lateinit var listTopping: ArrayList<Topping>
    private lateinit var toppingList: ArrayList<String>
    private var enrrolato = Enrrolato.instance
    private var listToRecycler: ArrayList<String> = ArrayList()
    private lateinit var toppingSelected: String

    private var count: Int = 0
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var af: AdapterIceCream

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_topping, container, false)
        var spinner = view.findViewById<View>(R.id.spTopping) as Spinner
        var fill = view.findViewById<View>(R.id.choosenTopping) as RecyclerView
        var next = view.findViewById<View>(R.id.btCotinueToContainer) as Button
        var alert = view.findViewById<View>(R.id.txtToppingAlert) as TextView

        loadToppings(spinner, fill, alert)

        next.setOnClickListener {
          selectContainer(fill)
        }

        return view
    }

    private fun loadToppings(spT: Spinner, rv: RecyclerView, alert: TextView) {
        listTopping = enrrolato.listToppings
        var list: ArrayList<Flavor> = ArrayList()
        toppingList = ArrayList()

        toppingList.add("Seleccione topping")

        for(list in listTopping) {

            if(list.available) {
                toppingList.add(list.name)
            }
        }
        fillSpinner(spT, rv, alert)
    }

    private fun back() {
        val fragment = PrincipalMenuFragment() // NO VA A PRINCIPAL SINO A FILLING
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_topping, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun fillSpinner(topping: Spinner, rv: RecyclerView, alert: TextView) {
        val array: ArrayAdapter<String> = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, toppingList)
        topping.adapter = array
        topping.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(av: AdapterView<*>?, view: View?, i: Int, p3: Long) {
                toppingSelected = av?.getItemAtPosition(i).toString()

                if(!toppingSelected.equals("Seleccione topping")) {
                    chooseTopping(rv, alert)
                    //}
                    //else {
                    //    errorTopping("Debe seleccionar algún topping")
                    //}
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun selectContainer(rv: RecyclerView) {
        if (rv == null || toppingSelected.equals("Seleccione topping") || toppingSelected.isEmpty()) {
            errorTopping("Debe seleccionar algún topping")
        } else {

            val fragment = SelectContainerFragment()
            val fm = requireActivity().supportFragmentManager
            val transaction = fm.beginTransaction()
            transaction.replace(R.id.ly_topping, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    @SuppressLint("WrongConstant")
    private fun chooseTopping(recyclerToppings: RecyclerView, alert: TextView) {
        mLayoutManager = LinearLayoutManager(context)
        recyclerToppings.setHasFixedSize(true)
        recyclerToppings.itemAnimator = DefaultItemAnimator()
        recyclerToppings.layoutManager = mLayoutManager
        recyclerToppings.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        if (!listToRecycler.contains(toppingSelected)) {

            listToRecycler.add(toppingSelected)
            af = AdapterIceCream(listToRecycler)

            af.setOnClickListener(View.OnClickListener { v ->
                var m: String = "Desea eliminar el topping"
                //listToRecycler.get(recyclerFlavors.getChildAdapterPosition(v)).toString()
                popupMessage(recyclerToppings.getChildAdapterPosition(v), af, m)
                count -= 1
            })

            recyclerToppings.adapter = af
            count += 1

            if(count > 2) {
                if(alert.visibility == View.INVISIBLE) {
                    alert.visibility = View.VISIBLE
                }
            }

            //} else {
            // YA SELECCIONÓ EL MÁXIMO
            // ¿QUIERE ELEGIR UN TOPPING MÁS?
            //}

        } else {
            errorTopping("Ya seleccionó este topping")
        }
    }

    private fun remove(p:Int) {
        listToRecycler.removeAt(p)
        af.notifyItemRemoved(p)
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

    private fun popupMessage(i: Int, a: AdapterIceCream, m: String) {
        val alertDialogBuilder = context?.let { AlertDialog.Builder(it, R.style.alert_dialog) }
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val popupConfirmMessageView =
            layoutInflater.inflate(R.layout.popup_confirmation_message, null)
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
            //Toast.makeText(context, "Usted ha eliminado " + n, Toast.LENGTH_SHORT).show()
        }

        bt_cancel.setOnClickListener {
            alertDialog.cancel()
        }
    }

}