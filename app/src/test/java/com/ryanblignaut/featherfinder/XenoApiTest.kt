package com.ryanblignaut.featherfinder

import com.ryanblignaut.featherfinder.api.EBirdApi
import com.ryanblignaut.featherfinder.api.XenoApi
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import org.junit.Test

class XenoApiTest {

    @Test
    fun testLocation() = runTest {
        val nearbyHotspots = XenoApi.fetchBirdsInLocation("Pretoria")
        TestCase.assertTrue(nearbyHotspots.isSuccess)
        println(nearbyHotspots.getOrNull())
    }

    @Test
    fun testLocation1() = runTest {
        val nearbyHotspots = XenoApi.fetchBirdsInLocation("Cape")
        TestCase.assertTrue(nearbyHotspots.isSuccess)
        println(nearbyHotspots.getOrNull())
    }

}