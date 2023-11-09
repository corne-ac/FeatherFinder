package com.ryanblignaut.featherfinder

import com.ryanblignaut.featherfinder.api.OpenRoutesApi
import kotlinx.coroutines.test.runTest
import org.json.JSONObject
import org.junit.Test

class OpenRoutesApiTest {
    @Test
    fun testRoute() = runTest {
        val api = OpenRoutesApi()
        val route = api.fetchRoute(-25.8752517, 28.1994317, 28.1769551, -25.8562937, -1)

        route.map(::JSONObject)
        if (route.isSuccess)
        {
            val message = route.getOrNull()



            println(message)
        }

    //        println(route)
//        assert(route.isSuccess)
    }

}