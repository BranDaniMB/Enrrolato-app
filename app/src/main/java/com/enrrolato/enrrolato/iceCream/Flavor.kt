package com.enrrolato.enrrolato.iceCream

class Flavor constructor(
    public val name: String,
    public val isLiqueur: Boolean,
    public val isSpecial: Boolean,
    public val isExclusive: Boolean,
    public val avaliable: Boolean
): IceCreamElement {

}