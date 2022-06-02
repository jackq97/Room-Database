package com.example.roomdemo

import android.app.Application

class EmployeeApp: Application() {

    // this class helps us to get access for dao for our function
    // by lazy is late int for val
    // lazy will load the data when needed not directly
    val db by lazy{
        EmployeeDatabase.getInstance(this)
    }

}