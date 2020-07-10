package com.enrrolato.enrrolato.createIcecream.process

import com.enrrolato.enrrolato.iceCream.Filling
import com.enrrolato.enrrolato.iceCream.Flavor
import com.enrrolato.enrrolato.iceCream.Topping
import java.util.*
import kotlin.collections.ArrayList

class IcecreamObject {

    private lateinit var listFlavor: ArrayList<Flavor>
    private lateinit var filling: String
    private lateinit var listTopping: ArrayList<Topping>
    private lateinit var container: String
    private var price: Int = 0
    private lateinit var date: String
    private lateinit var name: String
    private var status: Boolean = false

    constructor()

    constructor(listFlavor: ArrayList<Flavor>, filling: String, listTopping: ArrayList<Topping>, container: String, price: Int, date: String, name: String, status: Boolean) {
        this.listFlavor = listFlavor
        this.filling = filling
        this.listTopping = listTopping
        this.container = container
        this.price = price
        this.date = date
        this.name = name
        this.status = status
    }




}