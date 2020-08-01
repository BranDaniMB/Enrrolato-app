package com.enrrolato.enrrolato.objects

class Icecream constructor(
    public var id: String?,
    public var flavor: String,
    public var filling:String,
    public var topping: String,
    public var container: String,
    public var price: Int,
    public var delivered: Boolean,
    public var favorite: Boolean,
    public var sent: Boolean) {
}