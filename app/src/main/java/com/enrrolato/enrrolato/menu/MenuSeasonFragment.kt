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
import com.enrrolato.enrrolato.createIcecream.ToppingFragment
import com.enrrolato.enrrolato.database.Enrrolato
import com.enrrolato.enrrolato.iceCream.SeasonIcecream

class MenuSeasonFragment : Fragment() {

    private lateinit var listSeason: ArrayList<SeasonIcecream>
    private lateinit var seasonList: ArrayList<String>
    private var enrrolato = Enrrolato.instance
    private var listToRecycler: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_menu_season, container, false)
        var rvSeason = view.findViewById<View>(R.id.menuSeason) as RecyclerView
        var back = view.findViewById<View>(R.id.btBackToPrincipal3) as ImageButton
        var noSeason = view.findViewById<View>(R.id.txtNoSeason) as TextView

        chargeSeason(rvSeason)

        if (rvSeason?.adapter == null) {
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
        transaction.replace(R.id.ly_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun chargeSeason(rv: RecyclerView) {
        listSeason = enrrolato.listSeasonIcecream
        seasonList = ArrayList()

        for (list in listSeason) {
            if (list.avaliable) {
                seasonList.add(list.name)
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