package com.mupper.data.source.resources

interface TravelControlDataSource<D> {
    val playIcon: D
    val stopIcon: D
    val defaultFabColor: Int
    val defaultFabIconColor: Int
}