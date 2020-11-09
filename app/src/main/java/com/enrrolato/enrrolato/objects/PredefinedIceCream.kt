package com.enrrolato.enrrolato.objects

class PredefinedIceCream constructor(
    public val name: String,
    public val flavor: String,
    public val filling: String,
    public val topping: String,
    public val container: String,
    public val isSeason: Boolean,
    public val isSpecial: Boolean,
    public val isLiqueur: Boolean,
    public val available: Boolean
) {}