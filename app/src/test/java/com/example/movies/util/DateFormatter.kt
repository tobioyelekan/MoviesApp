package com.example.movies.util

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class DateFormatter {
    @Test
    fun `convert date from yyyy-mm-dd to mmm dd, yyyy`() {
        val date = "2020-09-22"

        val newDate = date.formatDate()
        assertThat(newDate, `is`("Sep 22, 2020"))
    }

    @Test
    fun `convert wrong date format to mmm dd, yyyy and assert that it returns the former date`() {
        val date = "09/22/2020"

        val newDate = date.formatDate()
        assertThat(newDate, `is`(date))
    }
}