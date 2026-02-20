package com.kaiqkt.nerv.unit.resources.exceptions

import com.kaiqkt.nerv.resources.exceptions.UnexpectedResourceException
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UnexpectedResourceExceptionTest {

    @Test
    fun `given an exception, it should correctly return the message and source location`() {
        val errorMessage = "Test error message"
        val exception = UnexpectedResourceException(errorMessage)

        assertEquals(errorMessage, exception.message)

        val sourceLocation = exception.sourceLocation()
        assertTrue(sourceLocation!!.contains("UnexpectedResourceExceptionTest.kt"))
    }

    @Test
    fun `given an exception, when stacktrace is null should return a null source location`() {
        val errorMessage = "Test error message"
        val exception = UnexpectedResourceException(errorMessage).apply {
            this.stackTrace = arrayOf()
        }

        assertEquals(errorMessage, exception.message)

        val sourceLocation = exception.sourceLocation()
        assertTrue(sourceLocation.isNullOrBlank())
    }

    @Test
    fun `given an exception, getLoggedMessage should return a formatted message`() {
        val errorMessage = "Another test error"
        val exception = UnexpectedResourceException(errorMessage)
        val sourceLocation = exception.sourceLocation()
        val expectedLoggedMessage = "Unexpected resource exception occurred at ($sourceLocation): $errorMessage"

        assertEquals(expectedLoggedMessage, exception.getLoggedMessage())
    }
}
