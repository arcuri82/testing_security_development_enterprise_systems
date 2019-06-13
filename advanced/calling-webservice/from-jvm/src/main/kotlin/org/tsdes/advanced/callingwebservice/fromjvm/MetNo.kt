package org.tsdes.advanced.callingwebservice.fromjvm

import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.UriBuilder

/**
 * The Norwegian "Meteorologisk Institutt" provides web services for querying forecast:
 *
 * https://www.met.no/
 * https://api.met.no/weatherapi/
 * https://api.met.no/weatherapi/textforecast/2.0/?forecast=landoverview
 *
 * Created by arcuri82 on 13-Jun-19.
 */



fun main() {

    /*
        Here, we use JAX-RS to access/create RESTful web services.
        But any HTTP client would do.

        JAX-RS is just a spec. Different implementations are for example
        RestEasy (from JBoss/Wildfly) and Jersey.
        This is the same concept of Hibernate being just an implementation of JPA.
    */

    //https://api.met.no/weatherapi/textforecast/2.0/?forecast=landoverview
    val uri = UriBuilder.fromUri("https://api.met.no/weatherapi/textforecast/2.0/")
            .port(443) // not necessary, as 443 is default anyway for https
            .queryParam("forecast", "landoverview") // equivalent to "?forecast=landoverview"
            .build()

    val client = ClientBuilder.newClient()
    val response = client.target(uri).request("application/xml").get()

    val xml = response.readEntity(String::class.java)

    println("Result as string : $xml")
}
