package org.tsdes.advanced.callingwebservice.fromjvm

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.UriBuilder

/**
 * Another weather web service is:
 * https://openweathermap.org
 *
 * Created by arcuri82 on 13-Jun-19.
 */

fun main(){

    /*
    We are going to use a third-party REST API from the network.
    You need to be connected to internet to get this example working,
    as we will need to connect to http://api.openweathermap.org

    Furthermore, to use such API, we need to be registered as a user.
    Every time we access the API, we need to send an authentication token,
    representing who we are.
    It is not uncommon for APIs to have "rate-limits" to the number of calls
    you can do. If you want to do more calls, you need to pay.

    It comes without saying that, if you want to build your own app that
    does access http://api.openweathermap.org, you should register a new
    user and use a different key, instead of the following.
    */

    val API_KEY = "bde08c38dcc24dfbffc449466cea7e44"

    //http://api.openweathermap.org/data/2.5/weather?appid=bde08c38dcc24dfbffc449466cea7e44&units=metric&q=Oslo,no
    val uri = UriBuilder.fromUri("http://api.openweathermap.org/data/2.5/weather")
            .port(80) // not necessary, as 80 is default anyway for http
            .queryParam("appid", API_KEY)
            .queryParam("units", "metric")
            .queryParam("q", "Oslo,no")
            .build()

    val client = ClientBuilder.newClient()
    val response = client.target(uri).request("application/json").get()

    val result = response.readEntity(String::class.java)
    println("Result as string : $result")

    //just extract one element of interest
    val parser = JsonParser()
    val json = parser.parse(result) as JsonObject
    val temperature = json.get("main").asJsonObject.get("temp").asString

    println("Temperature in Oslo: $temperature C'")
}
