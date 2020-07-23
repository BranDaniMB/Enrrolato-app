package com.enrrolato.enrrolato.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.adapter.AdapterMenu
import com.enrrolato.enrrolato.database.Enrrolato
import com.enrrolato.enrrolato.iceCream.Filling
import com.enrrolato.enrrolato.iceCream.Topping

class MenuFillingToppingFragment : Fragment() {

    private var selection: Boolean = false
    private lateinit var fillingList: ArrayList<String>
    private lateinit var toppingList: ArrayList<String>
    private lateinit var listFilling: ArrayList<Filling>
    private lateinit var listTopping: ArrayList<Topping>
    private var enrrolato = Enrrolato.instance
    private var listToRecycler: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_menu_ft, container, false)
        var back = view.findViewById<View>(R.id.btBackToMenu) as ImageButton
        var items =  view.findViewById<View>(R.id.menuFT) as RecyclerView

        back.setOnClickListener {
            back()
        }

        if(selection) {
            var title = getString(R.string.filling_menu)
            var menu = view.findViewById<View>(R.id.txtFTMenu) as TextView
            menu.text = title
            chargeFilling(items)
        }
        else {
            var title = getString(R.string.topping_menu)
            var menu = view.findViewById<View>(R.id.txtFTMenu) as TextView
            menu.text = title
            chargeTopping(items)
        }
        return view
    }

   fun selector(bool: Boolean) {
       selection = bool
   }

    private fun back() {
        val fragment = MenuFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_ft_menu, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun chargeFilling(rv: RecyclerView) {
        listFilling = enrrolato.listFillings
        fillingList = ArrayList()

        for (list in listFilling) {
            if (list.available) {
                fillingList.add(list.name)
                choose(rv, list.name)
            }
        }
    }

    private fun chargeTopping(rv: RecyclerView) {
        listTopping = enrrolato.listToppings
        toppingList = ArrayList()

        for (list in listTopping) {
            if (list.available) {
                toppingList.add(list.name)
                choose(rv, list.name)
            }
        }
    }

    private fun choose(recycler: RecyclerView, fs:String) {
        recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listToRecycler.add(fs)
        var af = AdapterMenu(listToRecycler)
        recycler.adapter = af
    }


}