package com.kodego.velascoben.roomdemo.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EmployeeDao {
    @Insert
    fun addEmployee(employee: Employee)

    @Query("SELECT * FROM Employee")
    fun getAllEmployees() : MutableList<Employee>

    @Query("DELETE FROM Employee WHERE id = :id")
    fun deleteEmployee(id:Int)

    @Query("UPDATE Employee SET name = :name, salary = :salary WHERE id = :id")
    fun updateEmployee(name : String, salary : Int, id : Int)
}