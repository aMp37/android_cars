package com.example.android_internship.util

import org.junit.Assert.*
import org.junit.Test

class ValidationUtilsTest {
    @Test
    fun shouldReturnTrueWhenStringContainsSpecialCharacter() {
        val stringWithSpecialCharacter = "oijfsdoidfsmom!ndmokgmo"
        assertTrue(stringWithSpecialCharacter.containSpecialCharacter())
    }

    @Test
    fun shouldReturnFalseWhenStringDoesNotContainsSpecialCharacter() {
        val stringWithSpecialCharacter = "oijfsdoidfsmomndmokgmo"
        assertFalse(stringWithSpecialCharacter.containSpecialCharacter())
    }
}