package com.enrrolato.enrrolato.iceCream

class FillingDecorator constructor(filling: Filling, component: IceCreamComponent) :
    IceCreamDecorator(filling, component) {

    override fun getPrice(): Double {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        TODO("Not yet implemented")
    }

    override fun getID(): Int {
        TODO("Not yet implemented")
    }
}