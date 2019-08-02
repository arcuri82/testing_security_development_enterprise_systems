package org.tsdes.advanced.kotlin


class KotlinProperty(
        val x: Int
) {
    var y: Int = 0
        private set

    var z: Int
        get(){ return y}
        set(value) {y = value}
}