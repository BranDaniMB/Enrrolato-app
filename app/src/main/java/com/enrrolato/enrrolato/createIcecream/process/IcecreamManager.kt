package com.enrrolato.enrrolato.createIcecream.process

import com.enrrolato.enrrolato.database.Enrrolato
import com.enrrolato.enrrolato.iceCream.Flavor
import com.enrrolato.enrrolato.iceCream.Topping
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class IcecreamManager {

    private var listFlavor: ArrayList<Flavor> = ArrayList()
    private var listTopping: ArrayList<Topping> = ArrayList()
    private var filling: String = ""
    private var container: String = ""
    private var price: Int = 2000
    private var flavor: String = ""
    private var topping: String = ""
    private var count: Int = 0

    fun addFlavor(flavor: Flavor) {
        listFlavor.add(flavor)

        if (flavor.isSpecial && !flavor.isLiqueur && flavor.avaliable) { // DEFAULT
            addFilling("leche condensada")
            //enrrolato.listPrices
            price += 900
        }

        if ((flavor.isSpecial && flavor.isLiqueur) && !flavor.isExclusive && flavor.avaliable) { // LICOR
            price += 1000
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
            price -= 1000
        }

    }

    fun removeTopping(topping: Topping) {
        listTopping.remove(topping)

        if(listTopping.size >= 2) {
            price -= 400
        }

    }

    fun seasonIcecream() {
        // ESTE SE COGE DE ENRROLATO, NADA MAS SE CAPTURA EL NOMBRE Y SE LLEVA LO QUE TIENE DENTRO
        // OJO -> SE DEBE BRINCAR TODOS LOS PASOS
    }

    fun addTopping(topping: Topping) {
        if (!listTopping.contains(topping)) {
            listTopping.add(topping)
            count++
        }

        if (listTopping.size > 2) {
            price += 400
        }
    }

    fun addContainer(container: String) {
        if (container.equals("cono")) {
            price += 500
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

    fun getFilling(): String {
        return filling
    }

    fun getContainer(): String {
        return container
    }

    fun isEmpty(): Boolean {
        if(flavor.equals("") && listFlavor.isEmpty() && filling.equals("") && topping.equals("") && listTopping.isEmpty() && container.equals("")) {
            return true
        }
        return false
    }

    fun cleanData() {
        flavor = ""
        listFlavor.clear()
        listTopping.clear()
        filling = ""
        topping = ""
        container = ""
        price = 2000
        count = 0
    }

}
