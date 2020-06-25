package com.enrrolato.enrrolato.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.AdapterIceCream
import com.enrrolato.enrrolato.database.Enrrolato
import com.enrrolato.enrrolato.iceCream.Flavor

class ChooseFlavorFragment : Fragment() {

    private lateinit var flavorList: ArrayList<String>
    private lateinit var listFlavor: ArrayList<Flavor>
    private var enrrolato = Enrrolato.instance

    private lateinit var name: String
    private var licour: Boolean = true
    private var special: Boolean = true
    private var exclusive: Boolean = true
    private var avaliable: Boolean = true

    private var traditional: ArrayList<String> = ArrayList()
    private var liqour: ArrayList<String> = ArrayList()
    private var specialF: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_choose_flavor, container, false)
        var back = view.findViewById<View>(R.id.btBackToMenu2) as ImageButton
        var trad = view.findViewById<View>(R.id.rvTrad) as RecyclerView
        var lic = view.findViewById<View>(R.id.rvLic) as RecyclerView
        var spec = view.findViewById<View>(R.id.rvSpecial) as RecyclerView

        back.setOnClickListener {
            back()
        }

        chargeTrad(trad, traditional)
        chargeLic(lic, liqour)
        chargeSpecial(spec, specialF)

        return view
    }

    private fun back() {
        val fragment = MenuFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_flavor_menu, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun chargeTrad(rv: RecyclerView, a: ArrayList<String>) {
        listFlavor = enrrolato.listFlavors
        var list: ArrayList<Flavor> = ArrayList()
        flavorList = ArrayList()

        for (list in listFlavor) {
            name = list.name
            licour = list.isLiqueur
            special = list.isSpecial
            exclusive = list.isExclusive
            avaliable = list.avaliable

            if (!special && !exclusive && avaliable) {
                flavorList.add(name)
                chooseFlavor(rv, list, a)
            }
        }
    }

    private fun chargeLic(rv: RecyclerView, a: ArrayList<String>) {
        listFlavor = enrrolato.listFlavors
        var list: ArrayList<Flavor> = ArrayList()
        flavorList = ArrayList()

        for (list in listFlavor) {
            name = list.name
            licour = list.isLiqueur
            special = list.isSpecial
            exclusive = list.isExclusive
            avaliable = list.avaliable

            if((special && licour) && !exclusive && avaliable) {
                flavorList.add(name)
                chooseFlavor(rv, list, a)
            }
        }
    }

    private fun chargeSpecial(rv: RecyclerView, a: ArrayList<String>) {
        listFlavor = enrrolato.listFlavors
        var list: ArrayList<Flavor> = ArrayList()
        flavorList = ArrayList()

        for (list in listFlavor) {
            name = list.name
            licour = list.isLiqueur
            special = list.isSpecial
            exclusive = list.isExclusive
            avaliable = list.avaliable

            if (special && !licour && avaliable) {
                flavorList.add(name)
                chooseFlavor(rv, list, a)
            }
            // NO SE ESTAN CARGANDO TODOS
        }
    }

    private fun chooseFlavor(recyclerFlavors: RecyclerView, name: Flavor, listToRecycler: ArrayList<String>) {
        recyclerFlavors.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                var af: AdapterIceCream
                listToRecycler.add(name.name)
                af = AdapterIceCream(listToRecycler)
                recyclerFlavors.adapter = af
        }




}