package com.enrrolato.enrrolato.createIcecream

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
        val view: View = inflater.inflate(R.layout.fragment_topping, container, false)
        val spinner = view.findViewById<View>(R.id.spTopping) as Spinner
        val fill = view.findViewById<View>(R.id.choosenTopping) as RecyclerView
        val next = view.findViewById<View>(R.id.btCotinueToContainer) as Button
        val msg = view.findViewById<View>(R.id.txtToppingAlert) as TextView

        loadToppings(msg, spinner, fill)

        next.setOnClickListener {
          selectContainer(fill)
        }

        return view
    }

    private fun loadToppings(msg: TextView, spT: Spinner, rv: RecyclerView) {
        listTopping = enrrolato.listToppings
        var list: ArrayList<Flavor> = ArrayList()
        toppingList = ArrayList()

        toppingList.add(getString(R.string.topping_selector))

        for(list in listTopping) {

            if(list.available) {
                toppingList.add(list.name)
            }
        }
        fillSpinner(msg, spT, rv)
    }

    private fun fillSpinner(msg: TextView, topping: Spinner, rv: RecyclerView) {
        val array: ArrayAdapter<String> = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, toppingList)
        topping.adapter = array
        topping.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(av: AdapterView<*>?, view: View?, i: Int, p3: Long) {
                toppingSelected = av?.getItemAtPosition(i).toString()

                if(!toppingSelected.equals(getString(R.string.topping_selector))) {
                    chooseTopping(msg, rv)
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
        if (rv == null || toppingSelected.equals(getString(R.string.topping_selector)) || toppingSelected.isEmpty()) {
            errorTopping(getString(R.string.no_topping))
        } else {

            val fragment = SelectContainerFragment()
            val fm = requireActivity().supportFragmentManager
            val transaction = fm.beginTransaction()
            transaction.replace(R.id.ly_topping, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun chooseTopping(msg:TextView, recyclerToppings: RecyclerView) {
        mLayoutManager = LinearLayoutManager(context)
        recyclerToppings.setHasFixedSize(true)
        recyclerToppings.itemAnimator = DefaultItemAnimator()
        recyclerToppings.layoutManager = mLayoutManager
        recyclerToppings.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        if (!listToRecycler.contains(toppingSelected)) {

            listToRecycler.add(toppingSelected)
            af = AdapterIceCream(listToRecycler)
            count += 1

                af.setOnClickListener(object: View.OnClickListener {
                    override fun onClick(v: View) {
                        var m: String = getString(R.string.delete_topping_prompt)
                        //listToRecycler.get(recyclerFlavors.getChildAdapterPosition(v)).toString()
                        popupMessage(recyclerToppings.getChildAdapterPosition(v), af, m)
                        count -= 1
                    }
                })
                recyclerToppings.adapter = af

            if(count > 2) {
                if(msg.visibility == View.INVISIBLE) {
                    msg.visibility = View.VISIBLE
                }
            }

        } else {
            errorTopping(getString(R.string.duplicated_topping))
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