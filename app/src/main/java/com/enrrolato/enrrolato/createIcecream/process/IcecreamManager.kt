package com.enrrolato.enrrolato.createIcecream.process

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.enrrolato.enrrolato.iceCream.Flavor
import com.enrrolato.enrrolato.iceCream.Topping
import java.lang.ProcessBuilder.Redirect.to
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList

class IcecreamManager {

    private var listFlavor: ArrayList<Flavor> = ArrayList()
    private var listTopping: ArrayList<Topping> = ArrayList()
    private var username: String = ""
    private var filling: String = ""
    private var container: String = ""
    private var status: Boolean = false
    private var price: Int = 2000

    private var flavor: String = ""
    private var topping: String = ""
    private var count: Int = 0

    fun addFlavor(flavor: Flavor) {
            listFlavor.add(flavor)
            //setFlavor(flavor.name)

            if (flavor.isSpecial && !flavor.isLiqueur && flavor.avaliable) { // DEFAULT
                addFilling("leche condensada")
                price += 900
            }

        if ((flavor.isSpecial && flavor.isLiqueur) && !flavor.isExclusive && flavor.avaliable) { // LICOR
            price += 1000
        }

        //checkPriceFlavor()
    }

    /*
    fun removeFlavor(flavor: Flavor) {
        listFlavor.remove(flavor)
    }

    fun removeTopping(topping: Topping) {
        listTopping.remove(topping)
    }
     */

    fun addTopping(topping: Topping) {
        if(!listTopping.contains(topping)) {
            listTopping.add(topping)
            //setTopping(topping.name)
        count++
        }

        if(listTopping.size > 2) {
     //   if(count > 2) {
            price += 400
        }

        //checkPriceTopping()
    }

    fun setUsername(username: String) {
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

    fun getFlavor(): ArrayList<Flavor> {
        return listFlavor
    }

    fun getTopping(): ArrayList<Topping> {
        return listTopping
    }

    /*
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


    private fun checkPriceTopping(): Int {
        if(listTopping.size > 2) {
            price += 400
        }
        return price
    }
     */

    fun getDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US)
        /*val timeZoneID = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ZoneId.of("America/Costa Rica")
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sdf.timeZone = TimeZone.getTimeZone(timeZoneID)
        } */

        var date = sdf.format(Date().time).replace("/", "")
            .replace(":", "")
        return date
    }

    fun getPrice(): Int {
        return price
    }

    /*
    private fun setFlavor(f: String) {
        this.flavor = f
    }

    fun getFlavor(): String {
        return flavor.plus(", ").plus(flavor)
    }

    private fun setTopping(t: String) {
        this.topping = t
    }

    fun getTopping(): String {
        return topping.plus(", ").plus(topping)
    }
     */

    fun getFilling(): String {
        return filling
    }

    fun getContainer(): String {
        return container
    }

    fun getUsername(): String {
        return username
    }

//    fun createIceCream(): IcecreamObject {
//       return IcecreamObject(listFlavor, getFilling(), listTopping, getContainer(), getPrice(), getDate(), username, status)
//    }

}
