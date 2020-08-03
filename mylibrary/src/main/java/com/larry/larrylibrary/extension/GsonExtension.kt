package com.larry.larrylibrary.extension

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException

/**
 * Created by user on 2018/7/10.
 */
inline fun <reified T> Gson.dataListFromJson(json: String): List<T>?{
    return try {
        val type = object : TypeToken<List<T>>() {}.type
        fromJson<List<T>>(json, type)
    }catch (e: JSONException){
        e.printStackTrace()
        null
    }
}