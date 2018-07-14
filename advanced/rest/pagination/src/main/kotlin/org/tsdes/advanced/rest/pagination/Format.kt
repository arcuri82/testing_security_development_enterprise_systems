package org.tsdes.advanced.rest.pagination

/**
 * In Kotlin, with the keyword "object" we can define
 * singleton classes.
 */
object Format {

    /*
        The keyword "const" means that these values are calculated
        at compilation time.
        This is needed for when they are used inside annotations,
        which do not accept values known only at runtime
     */

    const val JSON_V1 = "application/json;charset=UTF-8;version=1"
    const val HAL_V1 = "application/hal+json;charset=UTF-8;version=1"
}
