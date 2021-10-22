package org.wit.freedomfood.console.models

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import mu.KotlinLogging

import org.wit.freedomfood.console.helpers.*
import java.util.*

private val logger = KotlinLogging.logger {}

const val JSON_FILE = "freedomfoods.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()!!
val listType = object : TypeToken<ArrayList<FreedomFoodModel>>() {}.type!!

fun generateRandomId(): Long {
    return Math.abs(Random().nextLong())
}

class FreedomFoodJSONStore : FreedomFoodStore {

    private var freedomfoods = mutableListOf<FreedomFoodModel>()

    init {
        if (exists(JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): List<FreedomFoodModel> {
        return freedomfoods
    }

    override fun findOne(id: Long): List<FreedomFoodModel> {
        return listOf(freedomfoods.find { p -> p.id == id }!!)
    }

    override fun toEdit(id: Long): FreedomFoodModel? {
        return freedomfoods.find { p -> p.id == id }
    }

    override fun create(freedomfood: FreedomFoodModel) {
        freedomfood.id = generateRandomId()
        freedomfood.restaurantname = freedomfood.restaurantname
        freedomfood.restaurantdescription = freedomfood.restaurantdescription
        freedomfood.rating = freedomfood.rating
        freedomfood.meal = freedomfood.meal
        freedomfood.allergenFree = freedomfood.allergenFree
        freedomfoods.add(freedomfood)
        serialize()
    }

    override fun update(freedomfood: FreedomFoodModel) {
        val foundRestaurant = toEdit(freedomfood.id)
        print(foundRestaurant)
        if (foundRestaurant != null) {
            foundRestaurant.restaurantname = freedomfood.restaurantname
            foundRestaurant.restaurantdescription = freedomfood.restaurantdescription
            foundRestaurant.rating = freedomfood.rating
            freedomfood.meal = freedomfood.meal
            freedomfood.allergenFree = freedomfood.allergenFree
        }
        serialize()
    }

    override fun delete(freedomfood: FreedomFoodModel) {
        freedomfoods.remove(freedomfood)
        serialize()
    }

    internal fun logAll() {
        freedomfoods.forEach { logger.info("$it") }
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(freedomfoods, listType)
        write(JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(JSON_FILE)
        freedomfoods = Gson().fromJson(jsonString, listType)
    }
}