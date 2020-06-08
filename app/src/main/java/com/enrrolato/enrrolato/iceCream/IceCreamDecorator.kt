package com.enrrolato.enrrolato.iceCream

abstract class IceCreamDecorator constructor(
    protected var iceCreamElement: IceCreamElement,
    protected var component: IceCreamComponent
) : IceCreamComponent {
    override fun groupComponents(components: MutableMap<String, ArrayList<IceCreamComponent>>?): MutableMap<String, ArrayList<IceCreamComponent>> {
        val className: String? = iceCreamElement::class.simpleName
        return components?.let {
            it[className!!]?.add(this) ?: run {
                it[className] = arrayListOf<IceCreamComponent>()
                it[className]!!.add(this)
            }
            component.groupComponents(components)
        } ?: run {
            val temp: MutableMap<String, ArrayList<IceCreamComponent>>? = mutableMapOf()
            temp?.let {
                it[className!!]?.add(this) ?: run {
                    it[className] = arrayListOf<IceCreamComponent>()
                    it[className]!!.add(this)
                }
            }
            component.groupComponents(temp)
        }
    }
}
