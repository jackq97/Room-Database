package com.example.roomdemo

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

/* When you use the Room persistence library to store your app's data,
 you interact with the stored data by defining data access objects, or DAOs. */

@Dao
interface EmployeeDao {

    // insert data fun
    @Insert
    // yes it is a coroutine function, cause inserting data may take some time
    suspend fun insert(employeeEntity: EmployeeEntity)

    @Update
    suspend fun update(employeeEntity: EmployeeEntity)

    @Delete
    suspend fun delete(employeeEntity: EmployeeEntity)

    // in query we type what kind of query it should be and inside it we type sql code
    // this query mean it will select all(*) entries from table
    @Query("SELECT * FROM `employee-table`")

            /* In coroutines, a flow is a type that can emit multiple values sequentially,
            as opposed to suspend functions that return only a single value. For example,
            you can use a flow to receive live updates from a database. */

    // in our query we use fun that returns flow of our database
    fun fetchAllEmployees(): Flow<List<EmployeeEntity>>

    // query to get specific entry
    @Query("SELECT * FROM `employee-table` WHERE id=:id ")
    fun fetchEmployeeById(id: Int): Flow<EmployeeEntity>
}