package com.enrrolato.enrrolato.objects

class IceCream constructor(
    public var id: String?,
    public var flavor: String,
    public var filling:String,
    public var topping: String,
    public var container: String,
    public var price: Int,
    public var favorite: Boolean,
    public var sent: Boolean,
    public var finished: Boolean,
    public var plus18: Boolean) {
}