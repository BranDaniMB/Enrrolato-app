package com.enrrolato.enrrolato.manager

import com.enrrolato.enrrolato.objects.*

enum class Steps {
    WITHOUT_STARTING, FLAVORS, FILLINGS, TOPPINGS, CONTAINERS, IN_CART;
}

enum class SelectionTypes {
    IS_PREDEFINED_AND_ALLOWS_ANY, IS_PREDEFINED_AND_NOT_ALLOWS_ANY, NOT_PREDEFINED;
}

class MakeIceCream(val enrrolato: Enrrolato) {
    var isPredefined: PredefinedIceCream? = null;
    var step: Steps = Steps.WITHOUT_STARTING;
    val selectedFlavorsList: HashMap<String, Flavor> = HashMap();
    val selectedFillingList: HashMap<String, Filling> = HashMap();
    val selectedToppingList: HashMap<String, Topping> = HashMap();
    var selectedContainer: Container? = null;
    var plus18: Boolean = false;
    private var inPreload = false;

    fun getNextStep(): Steps {
        if (step == Steps.WITHOUT_STARTING) {
            step = Steps.FLAVORS;
        } else if (step == Steps.FLAVORS) {
            step = Steps.FILLINGS;
        } else if (step == Steps.FILLINGS) {
            step = Steps.TOPPINGS;
        } else if (step == Steps.TOPPINGS) {
            step = Steps.CONTAINERS;
        } else if (step == Steps.CONTAINERS) {
            step = Steps.IN_CART;
        }
        return step;
    }

    fun takeStepBack(): Steps {
        if (step == Steps.FLAVORS) {
            step = Steps.WITHOUT_STARTING;
            reset();
        } else if (step == Steps.FILLINGS) {
            step = Steps.FLAVORS;
        } else if (step == Steps.TOPPINGS) {
            step = Steps.FILLINGS;
        } else if (step == Steps.CONTAINERS) {
            step = Steps.TOPPINGS;
        } else if (step == Steps.IN_CART) {
            step = Steps.CONTAINERS;
        }
        return step;
    }

    fun isPredefinedAndAllowsAnyFlavor(): SelectionTypes {
        isPredefined?.let {
            return if (it.flavor == "any") {
                SelectionTypes.IS_PREDEFINED_AND_ALLOWS_ANY;
            } else {
                SelectionTypes.IS_PREDEFINED_AND_NOT_ALLOWS_ANY;
            }
        }?: run {
            return SelectionTypes.NOT_PREDEFINED;
        }
    }

    fun isPredefinedAndAllowsAnyFilling(): SelectionTypes {
        isPredefined?.let {
            return if (it.filling == "any") {
                SelectionTypes.IS_PREDEFINED_AND_ALLOWS_ANY;
            } else {
                SelectionTypes.IS_PREDEFINED_AND_NOT_ALLOWS_ANY;
            }
        }?: run {
            return SelectionTypes.NOT_PREDEFINED;
        }
    }

    fun isPredefinedAndAllowsAnyTopping(): SelectionTypes {
        isPredefined?.let {
            return if (it.topping == "any") {
                SelectionTypes.IS_PREDEFINED_AND_ALLOWS_ANY;
            } else {
                SelectionTypes.IS_PREDEFINED_AND_NOT_ALLOWS_ANY;
            }
        }?: run {
            return SelectionTypes.NOT_PREDEFINED;
        }
    }

    fun isPredefinedAndAllowsAnyContainer(): SelectionTypes {
        isPredefined?.let {
            return if (it.container == "any") {
                SelectionTypes.IS_PREDEFINED_AND_ALLOWS_ANY;
            } else {
                SelectionTypes.IS_PREDEFINED_AND_NOT_ALLOWS_ANY;
            }
        }?: run {
            return SelectionTypes.NOT_PREDEFINED;
        }
    }

    fun getPredefinedFlavors(): ArrayList<Flavor> {
        val temp = ArrayList<Flavor>()
        val split = isPredefined!!.flavor.split(",");
        for (flavor in split) {
            enrrolato.listFlavors[flavor]?.let { it1 -> temp.add(it1) }
        }
        return temp;
    }

    fun getPredefinedFilling(): ArrayList<Filling> {
        val temp = ArrayList<Filling>()
        val split = isPredefined!!.filling.split(",");
        for (filling in split) {
            enrrolato.listFillings[filling]?.let { it1 -> temp.add(it1) }
        }
        return temp;
    }

    fun getPredefinedTopping(): ArrayList<Topping> {
        val temp = ArrayList<Topping>()
        val split = isPredefined!!.topping.split(",");
        for (topping in split) {
            enrrolato.listToppings[topping]?.let { it1 -> temp.add(it1) }
        }
        return temp;
    }

    fun getPredefinedContainer(): ArrayList<Container> {
        val temp = ArrayList<Container>()
        val split = isPredefined!!.container.split(",");
        for (container in split) {
            enrrolato.listContainers[container]?.let { it1 -> temp.add(it1) }
        }
        return temp;
    }

    fun checkLiquor(): Boolean {
        for (it in selectedFlavorsList.values) {
            if (it.isLiqueur) {
                return true;
            }
        }
        return false;
    }

    fun fillIngredients(iceCream: PredefinedIceCream) {
        isPredefined = iceCream;
        isPredefined?.let {
            inPreload = true;
            var temp: List<String>;
            if (it.flavor != "any") {
                temp = it.flavor.split(",");
                for (flavor in temp) {
                    addFlavor(flavor);
                }
            }

            if (it.filling != "any") {
                temp = it.filling.split(",");
                for (filling in temp) {
                    addFilling(filling);
                }
            }

            if (it.topping != "any") {
                temp = it.topping.split(",");
                for (topping in temp) {
                    addTopping(topping);
                }
            }

            if (it.container != "any") {
                temp = it.container.split(",");
                for (container in temp) {
                    addContainer(container);
                }
            }
            inPreload = false;
        }
    }

    fun addFlavor(name: String) {
        if (step == Steps.FLAVORS || inPreload) {
            if (!selectedFlavorsList.containsKey(name)) {
                enrrolato.listFlavors[name]?.let {
                    selectedFlavorsList.put(name, it)
                    if (it.isLiqueur) {
                        plus18 = true;
                    }
                };
            }
        }
    }

    fun removeFlavor(name: String) {
        if (step == Steps.FLAVORS) {
            if (selectedFlavorsList.containsKey(name)) {
                selectedFlavorsList.remove(name);
            }
        }
    }

    fun addFilling(name: String) {
        if (step == Steps.FILLINGS || inPreload) {
            if (!selectedFillingList.containsKey(name)) {
                enrrolato.listFillings[name]?.let { selectedFillingList.put(name, it) };
            }
        }
    }

    fun removeFilling(name: String) {
        if (step == Steps.FILLINGS) {
            if (selectedFillingList.containsKey(name)) {
                selectedFillingList.remove(name);
            }
        }
    }

    fun addTopping(name: String) {
        if (step == Steps.TOPPINGS || inPreload) {
            if (!selectedToppingList.containsKey(name)) {
                enrrolato.listToppings[name]?.let { selectedToppingList.put(name, it) };
            }
        }
    }

    fun removeTopping(name: String) {
        if (step == Steps.TOPPINGS) {
            if (selectedToppingList.containsKey(name)) {
                selectedToppingList.remove(name);
            }
        }
    }

    fun addContainer(name: String) {
        if (step == Steps.CONTAINERS || inPreload) {
            enrrolato.listContainers[name]?.let { selectedContainer = it };
        }
    }

    fun removeContainer() {
        if (step == Steps.CONTAINERS) {
            selectedContainer = null;
        }
    }

    fun exportToCart() {
        enrrolato.addIceCreamToShoppingCart(exportToIceCream())
        reset();
    }

    private fun exportToIceCream(): IceCream {
        return IceCream(
            enrrolato.getId(),
            selectedFlavorsList.keys.joinToString(","),
            selectedFillingList.keys.joinToString(","),
            selectedToppingList.keys.joinToString(","),
            selectedContainer!!.name,
            getPrice(),
            false,
            false,
            false,
            checkLiquor()
        );
    }

    private fun getPrice(): Int {
        var price: Int = 0
        isPredefined?.let {
            if (it.isSeason) {
                price += enrrolato.prices.seasonIceCream;
            } else if (it.isLiqueur) {
                price += enrrolato.prices.liqueurFlavor;
            } else if (it.isSpecial) {
                price += enrrolato.prices.specialFlavor;
            }
        }?: run {
            for (it in selectedFlavorsList.values) {
                if (it.isLiqueur) {
                    price += enrrolato.prices.liqueurFlavor;
                    break;
                } else if (it.isSpecial) {
                    price += enrrolato.prices.specialFlavor;
                    break;
                }
            }

            if (selectedToppingList.size > enrrolato.prices.toppingAmount) {
                price += enrrolato.prices.toppingAmount * (selectedToppingList.size - enrrolato.prices.toppingAmount)
            }

            selectedContainer?.let {
                if (it.price > 0) {
                    price += it.price;
                }
            }
        }

        return price;
    }

    private fun reset() {
        isPredefined = null;
        step = Steps.WITHOUT_STARTING;
        selectedFlavorsList.clear();
        selectedFillingList.clear();
        selectedToppingList.clear();
        selectedContainer = null;
    }
}