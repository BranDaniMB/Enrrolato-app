package com.enrrolato.enrrolato.iceCream

class ContainerDecorator constructor(container: Container, component: IceCreamComponent) :
    IceCreamDecorator(container, component) {

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