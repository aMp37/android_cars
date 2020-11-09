package com.example.android_internship.error.database

import java.lang.RuntimeException

open class DatabaseError(override val message: String): Error()