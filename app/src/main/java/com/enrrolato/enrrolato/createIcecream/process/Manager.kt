package com.enrrolato.enrrolato.createIcecream.process

import com.enrrolato.enrrolato.database.Enrrolato
import com.enrrolato.enrrolato.objects.Icecream
import com.enrrolato.enrrolato.objects.SeasonIcecream

class Manager {

    private var createIcecream: IcecreamManager
    private var enrrolato: Enrrolato

    constructor(e: Enrrolato) {
        enrrolato = e
        createIcecream = IcecreamManager(enrrolato)
    }

    fun create(id:String?): Icecream {
        return Icecream(
            id,
            createIcecream.gFlavor(),
            createIcecream.getFilling(),
            createIcecream.gTopping(),
            createIcecream.getContainer(),
            createIcecream.getPrice(),
            false,
            false,
            false
        )
    }

    fun createSeasonIceCream(id: String?): Icecream {
        return Icecream(
            id,
            createIcecream.getSeasonIcecream().flavor + ",",
            createIcecream.getSeasonIcecream().filling,
            createIcecream.getSeasonIcecream().topping + ",",
            createIcecream.getContainer(),
            createIcecream.getSeasonPrice(),
            false,
            false,
            false
        )
    }

    fun getIcecreamManager(): IcecreamManager {
        return createIcecream
    }

    fun createIcecream(icecream: Icecream): Icecream {
        return icecream
    }

}