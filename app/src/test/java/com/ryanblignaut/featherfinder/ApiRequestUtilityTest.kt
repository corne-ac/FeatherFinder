package com.ryanblignaut.featherfinder

import com.ryanblignaut.featherfinder.model.TestApi
import com.ryanblignaut.featherfinder.utils.ApiRequestUtility
import org.junit.Assert
import org.junit.Test

class ApiRequestUtilityTest {


    // Make sure that the deserialization works as expected.
    @Test
    fun checkDeserialize() {
        val testApi = TestApi(1, "Ryan")
        val jsonModel = """{"id": 1,"name": "Ryan"}"""
        val fromJson = ApiRequestUtility.fromJson<TestApi>(jsonModel)
        Assert.assertEquals(true, fromJson.isSuccess)
        Assert.assertEquals(testApi, fromJson.getOrNull())
    }

    // Make sure that Error is returned when the JSON is invalid.
    @Test
    fun checkDeserializeError() {
        val jsonModel = """{"id": 1,"name": }"""
        val fromJson = ApiRequestUtility.fromJson<TestApi>(jsonModel)
        Assert.assertEquals(true, fromJson.isFailure)
        print(fromJson.exceptionOrNull())
    }

    // Make sure that Error is returned when the JSON is invalid.
    @Test
    fun checkDeserializeMissingField() {
        val testApi = TestApi(1, null)
        val jsonModel = """{"id": 1}"""
        val fromJson = ApiRequestUtility.fromJson<TestApi>(jsonModel)

        Assert.assertEquals(true, fromJson.isSuccess)
        Assert.assertEquals(testApi, fromJson.getOrNull())
    }

}