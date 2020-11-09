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
import com.enrrolato.enrrolato.adapter.AdapterMenu
import com.enrrolato.enrrolato.manager.Enrrolato
import com.enrrolato.enrrolato.objects.Flavor

class MenuFlavorFragment : Fragment() {
    private var enrrolato = Enrrolato.instance
    private var traditional: ArrayList<String> = ArrayList()
    private var licour: ArrayList<String> = ArrayList()
    private var specialF: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_menu_flavor, container, false)
        var back = view.findViewById<View>(R.id.btBackToMenu2) as ImageButton
        var trad = view.findViewById<View>(R.id.rvTrad) as RecyclerView
        var lic = view.findViewById<View>(R.id.rvLic) as RecyclerView
        var spec = view.findViewById<View>(R.id.rvSpecial) as RecyclerView

        back.setOnClickListener {
            back()
        }
        chargeTrad(trad, traditional)
        chargeLic(lic, licour)
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
        for (flavor in enrrolato.listFlavors.values) {
            if (!flavor.isSpecial && !flavor.isExclusive && flavor.avaliable) {
                a.add(flavor.name)
            }
        }
        chooseFlavor(rv, a)
    }

    private fun chargeLic(rv: RecyclerView, a: ArrayList<String>) {
        for (flavor in enrrolato.listFlavors.values) {
            if (flavor.isLiqueur && !flavor.isExclusive && flavor.avaliable) {
                a.add(flavor.name)
            }
        }
        chooseFlavor(rv, a)
    }

    private fun chargeSpecial(rv: RecyclerView, a: ArrayList<String>) {
        for (flavor in enrrolato.listFlavors.values) {
            if (flavor.isSpecial && !flavor.isLiqueur && flavor.avaliable) {
                a.add(flavor.name)
            }
        }
        chooseFlavor(rv, a)
    }

    private fun chooseFlavor(recyclerFlavors: RecyclerView, listToRecycler: ArrayList<String>) {
        recyclerFlavors.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val af = AdapterMenu(listToRecycler)
        recyclerFlavors.adapter = af
    }
}