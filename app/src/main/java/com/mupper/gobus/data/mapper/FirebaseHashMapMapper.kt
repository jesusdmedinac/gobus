package com.mupper.gobus.data.mapper

import com.mupper.domain.LatLng
import com.mupper.domain.bus.Bus as DomainBus
import com.mupper.domain.traveler.Traveler as DomainTraveler
import com.mupper.gobus.commons.FIELDS_BUS
import com.mupper.gobus.commons.FIELD_BUS_CAPACITY
import com.mupper.gobus.commons.FIELD_BUS_COLOR
import com.mupper.gobus.commons.FIELD_BUS_PATH
import com.mupper.gobus.commons.FIELDS_TRAVELER
import com.mupper.gobus.commons.FIELD_TRAVELER_CURRENT_POSITION
import com.mupper.gobus.commons.FIELD_TRAVELER_CURRENT_POSITION_LATITUDE
import com.mupper.gobus.commons.FIELD_TRAVELER_CURRENT_POSITION_LONGITUDE
import com.mupper.gobus.commons.FIELD_TRAVELER_EMAIL
import com.mupper.gobus.commons.FIELD_TRAVELER_IS_TRAVELING
import com.mupper.gobus.commons.extension.hasAll

fun Map<String, Any>.toDomainBus(): DomainBus {
    var busPath = ""
    var busCapacity = 0
    var busColor = ""
    if (hasAll(FIELDS_BUS)) {
        busPath = get(FIELD_BUS_PATH) as String
        busCapacity = get(FIELD_BUS_CAPACITY) as Int
        busColor = get(FIELD_BUS_COLOR) as String
    }

    return DomainBus(
        busPath,
        busColor,
        busCapacity,
        false
    )
}

fun Map<String, Any>.toDomainTraveler(): DomainTraveler {
    var currentPosition = LatLng(0.0, 0.0)
    var email = ""
    if (hasAll(FIELDS_TRAVELER)) {
        val currentPositionMap =
            get(FIELD_TRAVELER_CURRENT_POSITION) as Map<*, *>

        currentPosition =
            LatLng(
                currentPositionMap[FIELD_TRAVELER_CURRENT_POSITION_LATITUDE] as Double? ?: 0.0,
                currentPositionMap[FIELD_TRAVELER_CURRENT_POSITION_LONGITUDE] as Double? ?: 0.0
            )
        email = get(FIELD_TRAVELER_EMAIL) as String
    }
    var isTraveling = false
    if (containsKey(FIELD_TRAVELER_IS_TRAVELING)) {
        isTraveling = get(FIELD_TRAVELER_IS_TRAVELING) as Boolean
    }

    return DomainTraveler(
        email,
        currentPosition,
        isTraveling
    )
}
