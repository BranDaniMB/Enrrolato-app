package com.enrrolato.enrrolato.createIcecream.process

import com.enrrolato.enrrolato.database.Enrrolato
import com.enrrolato.enrrolato.objects.Flavor
import com.enrrolato.enrrolato.objects.SeasonIcecream
import com.enrrolato.enrrolato.objects.Topping
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class IcecreamManager {

    private var listFlavor: ArrayList<Flavor> = ArrayList()
    private var listTopping: ArrayList<Topping> = ArrayList()
    private var pm: PriceManager
    private var filling: String = ""
    private var container: String = ""
    private var price = 0
    private var flavor: String = ""
    private var topping: String = ""
    private lateinit var season: SeasonIcecream
    private var count: Int = 0
    private lateinit var enrrolato: Enrrolato

    constructor(e: Enrrolato) {
        enrrolato = e
        pm = PriceManager(enrrolato)
    }

    fun addFlavor(flavor: Flavor) {
        listFlavor.add(flavor)

        if (flavor.isSpecial && !flavor.isLiqueur && flavor.avaliable) { // DEFAULT
            addFilling("leche condensada")
            price = pm.getSpecialPrice()
        }
        else if ((flavor.isSpecial && flavor.isLiqueur) && !flavor.isExclusive && flavor.avaliable) { // LICOR
            price = pm.getLiqueurPrice()
        }
        else {
          price = pm.getRegularPrice()
        }
    }

    fun gFlavor(): String {
        for (l in listFlavor) {
            flavor += l.name + ", "
        }
        return flavor
    }

    fun gTopping(): String {
        for (t in listTopping) {
            topping += t.name + ", "
        }
        return topping
    }

    fun removeFlavor(flavor: Flavor) {
        listFlavor.remove(flavor)

        if ((flavor.isSpecial && flavor.isLiqueur) && !flavor.isExclusive && flavor.avaliable) { // LICOR
            price -= pm.getLiqueurPrice()
        }
    }

    fun removeTopping(topping: Topping) {
        listTopping.remove(topping)

        if(listTopping.size >= 2) {
            price -= pm.getRegularPrice()
        }
    }

    fun addSeasonIcecream(season: SeasonIcecream) {
        this.season = season
    }

    fun getSeasonIcecream(): SeasonIcecream {
        return season
    }

    fun addTopping(topping: Topping) {
        if (!listTopping.contains(topping)) {
            listTopping.add(topping)
            count++
        }

        if (listTopping.size > 2) {
            price += pm.getExtraTopping()
        }
    }

    fun addContainer(container: String) {
        if (container.equals("cono")) {
            price += pm.getSpecialContainer()
        }
        this.container = container
    }

    fun addFilling(filling: String) {
        this.filling = filling
    }

    fun getDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        sdf.timeZone = TimeZone.getTimeZone("GMT-6")
        return sdf.format(Date().time).replace("/", "").replace(":", "")
    }

    fun getPrice(): Int {
        return price
    }

    fun getSeasonPrice(): Int {
        price = pm.getSeasonPrice()
        return price
    }

    fun getFilling(): String {
        return filling
    }

    fun getContainer(): String {
        return container
    }

    /*
    fun isEmpty(): Boolean {
        if(flavor.equals("") && listFlavor.isEmpty() && filling.equals("") && topping.equals("") && listTopping.isEmpty() && container.equals("")) {
            return true
        }
        return false
    }
     */

    fun cleanData() {
        flavor = ""
        listFlavor.clear()
        listTopping.clear()
        filling = ""
        topping = ""
        container = ""
        price = 0
        count = 0
    }

}
