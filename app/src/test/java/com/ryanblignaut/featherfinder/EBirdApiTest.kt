package com.ryanblignaut.featherfinder

import com.ryanblignaut.featherfinder.api.EBirdApi
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import org.junit.Test

class EBirdApiTest {

    @Test
    fun testGetBirds() = runTest {
        val nearbyHotspots = EBirdApi.fetchNearbyHotspots(-25.873390, 28.197800, 10.0)
        TestCase.assertTrue(nearbyHotspots.isSuccess)
        /*  nearbyHotspots.getOrNull()?.forEach {
              println(it)
          }*/
    }

    @Test
    fun testFetchRegionBirds() = runTest {
        val nearbyHotspots = EBirdApi.fetchHotspotsInRegion("ZA")
        TestCase.assertTrue(nearbyHotspots.isSuccess)
        /* nearbyHotspots.getOrNull()?.forEach {
             println(it)
         }*/
    }

    @Test
    fun testHotspotInfo() = runTest {
        val hotspotInfo = EBirdApi.fetchHotspotInfo("L6101408")
        TestCase.assertTrue(hotspotInfo.isSuccess)
        print(hotspotInfo)
    }

}