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
import com.enrrolato.enrrolato.PrincipalMenuFragment
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.adapter.AdapterMenu
import com.enrrolato.enrrolato.manager.Enrrolato

class MenuSeasonFragment : Fragment() {
    private var enrrolato = Enrrolato.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_menu_season, container, false)
        val rvSeason = view.findViewById<View>(R.id.menuSeason) as RecyclerView
        val back = view.findViewById<View>(R.id.btBackToPrincipal3) as ImageButton
        val noSeason = view.findViewById<View>(R.id.txtNoSeason) as TextView

        chargePredefined(rvSeason)

        if (rvSeason.adapter == null) {
            noSeason.visibility = View.VISIBLE
        }
        else {
            noSeason.visibility = View.INVISIBLE
        }

        back.setOnClickListener {
            back()
        }
        return view
    }

    private fun back() {
        val fragment = PrincipalMenuFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_menu_season, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun chargePredefined(rv: RecyclerView) {
        val temp: ArrayList<String> = ArrayList()

        for (item in enrrolato.listPredefinedIceCream.values) {
            if (item.available && item.isSeason) {
                temp.add(item.name)
            }
        }
        choose(rv, temp)
    }

    private fun choose(recycler: RecyclerView, list: ArrayList<String>) {
        recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val af = AdapterMenu(list)
        recycler.adapter = af
    }
}