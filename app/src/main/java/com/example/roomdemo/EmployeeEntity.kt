package com.example.roomdemo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/* first we use annotation(metadata for our data base) to mark out table as an entity
* marking our data class as an entity and then naming it */

// make sure to explicitly mention the data type for our column entries

@Entity(tableName = "employee-table")
data class EmployeeEntity(
    // primary key gives unique id to each of our entry
    @PrimaryKey(autoGenerate = true)
    // this id var is a column in our table
    var id: Int = 0,
    var name: String = "",
    // giving custom name to our column with column info annotation
    @ColumnInfo(name = "email-id")
    var email: String = ""
)
