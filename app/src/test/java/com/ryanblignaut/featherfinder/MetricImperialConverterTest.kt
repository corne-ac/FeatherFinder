package com.ryanblignaut.featherfinder

import com.ryanblignaut.featherfinder.utils.MetricImperialConverter
import junit.framework.TestCase
import org.junit.Test

class MetricImperialConverterTest {
    @Test
    fun testKilometersToMiles() {
        val miles = MetricImperialConverter.kilometersToMiles(10.0)
        val eMiles = 6.21371
        TestCase.assertEquals(eMiles, miles, 0.001)
    }

    @Test
    fun testMilesToKilometers() {
        val kilometers = MetricImperialConverter.milesToKilometers(6.21371)
        val eKms = 10.0
        TestCase.assertEquals(eKms, kilometers, 0.001)
    }
}