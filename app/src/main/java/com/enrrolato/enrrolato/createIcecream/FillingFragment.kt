package com.enrrolato.enrrolato.createIcecream

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.database.Enrrolato

class FillingFragment : Fragment() {

    private lateinit var nameList: ArrayList<String>
    private lateinit var fillingSelected: String
    private var enrrolato = Enrrolato.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View =  inflater.inflate(R.layout.fragment_filling, container, false)
        val sp = view.findViewById<View>(R.id.spFilling) as Spinner
        val back = view.findViewById<View>(R.id.backBtn) as ImageButton
        val next = view.findViewById<View>(R.id.btContinue) as Button

        loadFillings(sp)

        back.setOnClickListener {
            val fragment = FlavorsFragment()
            val fm = requireActivity().supportFragmentManager
            val transaction = fm.beginTransaction()
            transaction.replace(R.id.ly_filling, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        next.setOnClickListener {
            val selected = sp.selectedItem as String
            if (selected == null || selected.equals(getString(R.string.filling_selector))) {
                errorPopup(getString(R.string.no_filling))
            }
            else {
                enrrolato.createIceCream().addFilling(fillingSelected)

                val fragment = ToppingFragment()
                val fm = requireActivity().supportFragmentManager
                val transaction = fm.beginTransaction()
                transaction.replace(R.id.ly_filling, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
        return view
    }

    private fun loadFillings(s: Spinner) {
        val fillings = Enrrolato.instance.listFillings
        nameList = ArrayList<String>()

        nameList.add(getString(R.string.filling_selector))

        for (filling in fillings) {
            if (filling.available && !filling.isExclusive) {
                nameList.add(filling.name)
            }
        }
        fillFilling(s)
    }

    private fun fillFilling(f: Spinner) {
        val array: ArrayAdapter<String> = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, nameList)
        f.adapter = array
        f.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(av: AdapterView<*>?, view: View?, i: Int, p3: Long) {
                fillingSelected = av?.getItemAtPosition(i).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
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