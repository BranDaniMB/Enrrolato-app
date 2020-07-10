package com.enrrolato.enrrolato.createIcecream.process

import android.os.Build
import androidx.annotation.RequiresApi
import com.enrrolato.enrrolato.iceCream.Flavor
import com.enrrolato.enrrolato.iceCream.Topping
import java.util.*
import kotlin.collections.ArrayList

class IcecreamManager {

    private var listFlavor: ArrayList<Flavor> = ArrayList()
    private var listTopping: ArrayList<Topping> = ArrayList()
    private lateinit var username: String
    private lateinit var filling: String
    private lateinit var container: String
    private var status: Boolean = false
    private var price: Int = 2000

    fun addFlavor(flavor: Flavor)  {
        if(!listFlavor.contains(flavor)) {
            listFlavor.add(flavor)

            if (flavor.isSpecial && !flavor.isLiqueur && flavor.avaliable) {
                addFilling("leche condensada")
            }
        }
        checkPriceFlavor()
    }

    fun removeFlavor(flavor: Flavor) {
        listFlavor.remove(flavor)
    }

    fun removeTopping(topping: Topping) {
        listTopping.remove(topping)
    }

    fun addTopping(topping: Topping) {
        if(!listTopping.contains(topping)) {
            listTopping.add(topping)
        }
        checkPriceTopping()
    }

    fun getUsername(username: String) {
        this.username = username
    }

    fun addContainer(container: String) {
        if(container.equals("cono")) {
            price += 500
        }
        this.container = container
    }

    fun addFilling(filling: String) {
        this.filling = filling
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDate(): String {
        var date: Date = Date()
        return date.toInstant().toString()
    }

    private fun checkPriceFlavor(): Int {
        var list: ArrayList<Flavor> = ArrayList()

        for (list in listFlavor) {
            if ((list.isSpecial && list.isLiqueur) && !list.isExclusive && list.avaliable) { // LICOR
                price += 1000
                return price
            }

            if (list.isSpecial  && !list.isLiqueur && list.avaliable) { // ESPECIALES
                price += 900
            }
        }
        return price
    }

    fun getPrice(): Int {
        return price
    }

    private fun checkPriceTopping(): Int {
        if(listTopping.size > 2) {
            price += 400
        }
        return price
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createIceCream(): IcecreamObject {
       return IcecreamObject(listFlavor, filling, listTopping, container, price, getDate(), username, status)
    }

}