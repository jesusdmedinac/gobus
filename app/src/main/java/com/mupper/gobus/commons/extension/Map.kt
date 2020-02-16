package com.mupper.gobus.commons.extension


/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
fun Map<String, Any>.hasAll(keys: List<String>): Boolean {
    for (element in keys) {
        if (!this.containsKey(element)) {
            return false
        }
    }
    return true
}
