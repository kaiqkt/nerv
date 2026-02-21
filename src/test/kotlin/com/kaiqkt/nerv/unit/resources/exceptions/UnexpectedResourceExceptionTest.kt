package com.kaiqkt.nerv.unit.resources.exceptions

import com.kaiqkt.nerv.resources.exceptions.UnexpectedResourceException
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UnexpectedResourceExceptionTest {
    @Test
    fun `given an exception when instantiated then correctly return the message and source location`() {
        val errorMessage = "Test error message"

        val exception = UnexpectedResourceException(errorMessage)

        assertEquals(errorMessage, exception.message)
        val sourceLocation = exception.sourceLocation()
        assertTrue(sourceLocation!!.contains("UnexpectedResourceExceptionTest.kt"))
    }

    @Test
    fun `given an exception when stacktrace is null then return a null source location`() {
        val errorMessage = "Test error message"
        val exception =
            UnexpectedResourceException(errorMessage).apply {
                this.stackTrace = arrayOf()
            }

        val sourceLocation = exception.sourceLocation()

        assertEquals(errorMessage, exception.message)
        assertTrue(sourceLocation.isNullOrBlank())
    }

    @Test
    fun `given an exception when calling getLoggedMessage then return a formatted message`() {
        val errorMessage = "Another test error"
        val exception = UnexpectedResourceException(errorMessage)
        val sourceLocation = exception.sourceLocation()
        val expectedLoggedMessage = "Unexpected resource exception occurred at ($sourceLocation): $errorMessage"

        val loggedMessage = exception.getLoggedMessage()

        assertEquals(expectedLoggedMessage, loggedMessage)
    }
}
