package com.enrrolato.enrrolato.iceCream

class FlavorDecorator constructor(
    flavor: Flavor,
    component: IceCreamComponent
) : IceCreamDecorator(flavor, component) {

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