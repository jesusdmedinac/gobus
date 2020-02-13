package com.mupper.data.repository

import com.mupper.data.source.resources.MapResourcesDataSource
import com.mupper.domain.resources.BusIcon
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapResourcesRepositoryImplTest {
    @Mock
    lateinit var mapResourcesDataSource: MapResourcesDataSource<BusIcon>

    lateinit var mapResourcesRepository: MapResourcesRepository<BusIcon>

    @Before
    fun setUp() {
        mapResourcesRepository = MapResourcesRepositoryDerived(mapResourcesDataSource)
    }

    @Test
    fun `getBusIcon should call getBusIcon of mapResourcesDataSource`() {
        // WHEN
        mapResourcesRepository.getBusIcon()

        // THEN
        verify(mapResourcesDataSource).getBusIcon()
    }
}