package com.enrrolato.enrrolato.iceCream

class ToppingDecorator constructor(topping: Topping, component: IceCreamComponent):
    IceCreamDecorator(topping, component) {

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