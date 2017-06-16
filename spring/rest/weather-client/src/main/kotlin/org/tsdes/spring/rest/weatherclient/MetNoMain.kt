package org.tsdes.spring.rest.weatherclient

import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.UriBuilder

/**
 *  A list of Web Services
 *
 *  http://www.programmableweb.com/
 *
 *  Example: Weather forecast
 *
 *  http://www.yr.no/
 *
 *  http://api.met.no/weatherapi/documentation
 *
 *  http://api.met.no/weatherapi/textforecast/1.6/schema
 *
 *  Created by arcuri82 on 14-Jun-17.
 */

fun main(args: Array<String>) {

    /*
    We use JAX-RS to access/create RESTful web services.

    REST =  Representational State Transfer

    JAX-RS is just a spec. Different implementations are for example
    RestEasy (from JBoss/Wildfly) and Jersey.
    This is the same concept of Hibernate being just an implementation of JPA.
    */

    //http://api.met.no/weatherapi/textforecast/1.6/?forecast=land;language=nb
    val uri = UriBuilder.fromUri("http://api.met.no/weatherapi/textforecast/1.6")
            .port(80) // not necessary, as 80 is default anywat
            .queryParam("forecast", "land") // equivalent to "?forecast=land"
            .queryParam("language", "nb")   // equivalent to "&language=nb"
            .build()

    val client = ClientBuilder.newClient()
    val response = client.target(uri).request("application/xml").get()

    val xml = response.readEntity(String::class.java)

    println("Result as string : $xml")
}
