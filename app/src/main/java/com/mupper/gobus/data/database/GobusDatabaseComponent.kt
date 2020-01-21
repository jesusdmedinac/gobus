package com.mupper.gobus.data.database

import dagger.Subcomponent

@Subcomponent
interface GobusDatabaseComponent {
    val db: GobusDatabase
}