package com.mupper.gobus.commons


/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
// Room constants
const val DATABASE_NAME = "gobus-db"

// Firestore constants
const val COLLECTION_TRAVELER = "traveler"
const val FIELD_TRAVELER_CURRENT_POSITION = "currentPosition"
const val FIELD_TRAVELER_CURRENT_POSITION_LATITUDE = "latitude"
const val FIELD_TRAVELER_CURRENT_POSITION_LONGITUDE = "longitude"
const val FIELD_TRAVELER_EMAIL = "email"
const val FIELD_TRAVELER_IS_TRAVELING = "isTraveling"
val FIELDS_TRAVELER = arrayListOf(
    FIELD_TRAVELER_EMAIL,
    FIELD_TRAVELER_CURRENT_POSITION
)
const val COLLECTION_BUS = "bus"
const val FIELD_BUS_PATH = "path"
const val FIELD_BUS_COLOR = "color"
const val FIELD_BUS_CAPACITY = "capacity"
const val FIELD_BUS_TRAVELERS = "travelers"
val FIELDS_BUS = arrayListOf(
    FIELD_BUS_PATH,
    FIELD_BUS_COLOR,
    FIELD_BUS_CAPACITY,
    FIELD_BUS_TRAVELERS
)

// TIME
const val MILISECONDS_ONE_SECOND: Long = 1000
const val MILLISECONDS_TEN_MILLIS: Long = 10
const val MILLISECONDS_SIXTEEN_MILLIS: Long = 16

// GOOGLE MAP
const val GOOGLE_MAP_DEFAULT_ZOOM = 17f
