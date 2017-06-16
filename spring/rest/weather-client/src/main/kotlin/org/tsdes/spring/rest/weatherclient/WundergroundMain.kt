package org.tsdes.spring.rest.weatherclient

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.UriBuilder

/**
 * Created by arcuri82 on 14-Jun-17.
 */

fun main(args: Array<String>) {

    //http://www.wunderground.com/
    //http://api.wunderground.com/api/302f56c7ad8e8c82/geolookup/conditions/forecast/q/Norway/Oslo.xml

    /*
    Need to register. Linked to a user.
    Up to 500 calls per day, 10 per minute.
    To have more, you need to pay.
    */
    val code = "302f56c7ad8e8c82"
    val country = "Norway"
    val city = "Oslo"

    //parameters passed as path elements, and a not as "?name=value" parameters
    val uri = UriBuilder
            .fromUri("http://api.wunderground.com/api/$code/geolookup/conditions/forecast/q/$country/$city.json")
            .port(80)
            .build()

    val client = ClientBuilder.newClient()
    val response = client.target(uri).request("application/json").get()

    val result = response.readEntity(String::class.java)
    println("Result as string : $result")

    /*
    Missing XSD:

    https://apicommunity.wunderground.com/weatherapi/topics/is_there_an_xsd_document_available_to_create_an_object_model_from_for_deserialization_in_c

    so cannot automatically generate Java classes for binding/conversion

    No documentation, just list of field names:

    http://www.wunderground.com/weather/api/d/docs?d=data/conditions&MR=1

    could manually write a POJO, and then map/translate with GSon, but quite large, see w_example.json
    */

    //just extract one element of interest
    val parser = JsonParser()
    val json = parser.parse(result) as JsonObject
    val temperature = json.get("current_observation").asJsonObject.get("temp_c").asString

    println("Temperature in Oslo: $temperature C'")
}

