package com.enrrolato.enrrolato

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.enrrolato.enrrolato.manager.Enrrolato

class DetailFragment : Fragment() {

    private var idIcecream: Int = 0
    private var enrrolato = Enrrolato.instance
    private lateinit var backToCart: Button
    private lateinit var details: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_detail, container, false)
        backToCart = view.findViewById(R.id.btBackToCart)
        details = view.findViewById(R.id.OrderDetail)

        backToCart.setOnClickListener {
            back()
        }

        showDetails()
        return view
    }

    private  fun back() {
        val fragment = CartScreenFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_datails, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun catchID(id: Int) {
        this.idIcecream = id
    }

    private fun showDetails() {
        var list = enrrolato.getList()
        var item = list.get(idIcecream)

        if(item.favorite) {
            details.text = "Sabores: " + item.flavor.substring(0, item.flavor.length - 1) + "\nJarabe: " + item.filling + "\nToppings: " + item.topping.substring(0, item.topping.length - 1) +
                    "\nEnvase: " + item.container + "\nPrecio: " + item.price+ "\nHelado favorito: Sí"
        } else {
            details.text = "Sabores: " + item.flavor.substring(0, item.flavor.length - 1) + "\nJarabe: " + item.filling + "\nToppings: " + item.topping.substring(0, item.topping.length - 1) +
                    "\nEnvase: " + item.container + "\nPrecio: " + item.price+ "\nHelado favorito: No"
        }
    }

}