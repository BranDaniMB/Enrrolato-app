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
import com.enrrolato.enrrolato.AdapterIceCream
import com.enrrolato.enrrolato.database.Enrrolato
import com.enrrolato.enrrolato.iceCream.Filling
import com.enrrolato.enrrolato.iceCream.Flavor
import com.enrrolato.enrrolato.iceCream.Topping

class ChooseMenuFTFragment : Fragment() {

    private var selection: Boolean = false

    private lateinit var fillingList: ArrayList<String>
    private lateinit var toppingList: ArrayList<String>
    private lateinit var listFilling: ArrayList<Filling>
    private lateinit var listTopping: ArrayList<Topping>
    private var enrrolato = Enrrolato.instance
    private var listToRecycler: ArrayList<String> = ArrayList()

    private lateinit var name: String
    private var exclusive: Boolean = true
    private var avaliable: Boolean = true

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

        if(selection == true) {
            var title: String = "Menú de jarabes"
            var menu = view.findViewById<View>(R.id.txtFTMenu) as TextView
            menu.text = title
            chargeFilling(items)
        }
        else {
            var title: String = "Menú de toppings"
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
        var list: ArrayList<Flavor> = ArrayList()
        fillingList = ArrayList()

        for (list in listFilling) {
            name = list.name
            exclusive = list.isExclusive
            avaliable = list.available

            if (avaliable) {
                fillingList.add(name)
                choose(rv, name)
            }
        }
    }

    private fun chargeTopping(rv: RecyclerView) {
        listTopping = enrrolato.listToppings
        var list: ArrayList<Flavor> = ArrayList()
        toppingList = ArrayList()

        for (list in listTopping) {
            name = list.name
            avaliable = list.available

            if (avaliable) {
                toppingList.add(name)
                choose(rv, name)
            }
        }
    }

    private fun choose(recycler: RecyclerView, fs:String) {
        recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        var af: AdapterIceCream

        listToRecycler.add(fs)
        af = AdapterIceCream(listToRecycler)
        recycler.adapter = af
    }


}