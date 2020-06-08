package com.enrrolato.enrrolato.iceCream

interface IceCreamComponent {
    fun getPrice(): Double

    fun getName(): String

    fun getID(): Int

    fun groupComponents(components: MutableMap<String, ArrayList<IceCreamComponent>>?): MutableMap<String, ArrayList<IceCreamComponent>>
}