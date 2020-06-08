package com.enrrolato.enrrolato.iceCream

class IceCream constructor(email: String, id: Int): IceCreamComponent{
    override fun getPrice(): Double {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        TODO("Not yet implemented")
    }

    override fun getID(): Int {
        TODO("Not yet implemented")
    }

    override fun groupComponents(components: MutableMap<String, ArrayList<IceCreamComponent>>?): MutableMap<String, ArrayList<IceCreamComponent>> {
        return components!!
    }
}