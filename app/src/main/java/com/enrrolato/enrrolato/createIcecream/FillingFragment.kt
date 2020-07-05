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
import com.enrrolato.enrrolato.iceCream.Filling

class FillingFragment : Fragment() {


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
            if (selected == null || selected.equals(getString(R.string.filling_selector))) { // Si es que debe
                errorPopup(getString(R.string.no_filling))
            } else {
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
        val nameList = ArrayList<String>()

        nameList.add(getString(R.string.filling_selector))

        for (filling in fillings) {

            if (filling.available && !filling.isExclusive) {
                nameList.add(filling.name)
            }
        }

        s.adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, nameList)
    }

    private fun errorPopup(msg: String) {
        val alertDialogBuilder = context?.let { AlertDialog.Builder(it, R.style.alert_dialog) }
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val popupMaxFlavor = layoutInflater.inflate(R.layout.popup_choose_flavor, null)
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