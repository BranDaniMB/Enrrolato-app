package com.enrrolato.enrrolato.createIcecream.process

import com.enrrolato.enrrolato.database.Enrrolato
import com.enrrolato.enrrolato.objects.Container
import com.enrrolato.enrrolato.objects.Price

class PriceManager {

    private var enrrolato = Enrrolato.instance
    private var listPrices: ArrayList<Price> = enrrolato.listPrices
    private var listContainer: ArrayList<Container> = enrrolato.listContainers

    fun getRegularPrice(): Int {
        for(l in listPrices) {
            if(l.type.equals("regular_price")) {
                return l.price.toInt()
            }
        }
        return 0
    }

    fun getLiqueurPrice(): Int {
        for(l in listPrices) {
            if(l.type.equals("liqueur_flavor")) {
                return l.price.toInt()
            }
        }
        return 0
    }

    fun getSpecialPrice(): Int {
        for(l in listPrices) {
            if(l.type.equals("special_flavor")) {
                return l.price.toInt()
            }
        }
        return 0
    }

    fun getSeasonPrice(): Int {
        for(l in listPrices) {
            if(l.type.equals("season_ice_cream")) {
                return l.price.toInt()
            }
        }
        return 0
    }

    fun getExtraTopping(): Int {
        for(l in listPrices) {
            if(l.type.equals("extra_topping_price")) {
                return l.price.toInt()
            }
        }
        return 0
    }

    fun getSpecialContainer(): Int {
        for(l in listContainer) {
            if(l.price != 0) {
                return l.price
            }
        }
        return 0
    }

}