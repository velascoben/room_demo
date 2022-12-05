package com.kodego.velascoben.roomdemo

import android.app.Dialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.kodego.velascoben.roomdemo.databinding.ActivityMainBinding
import com.kodego.velascoben.roomdemo.databinding.UpdateDialogBinding
import com.kodego.velascoben.roomdemo.db.CompanyDatabase
import com.kodego.velascoben.roomdemo.db.Employee
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var companyDB : CompanyDatabase
    lateinit var adapter: EmployeeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        companyDB = CompanyDatabase.invoke(this)

        // Display Data
        view()

        binding.btnSave.setOnClickListener() {
            var name = binding.etName.text.toString()
            var salary = binding.etSalary.text.toString().toInt()

            val employee = Employee(name,salary)
            save(employee)
            adapter.employeeModel.add(employee)
            adapter.notifyDataSetChanged()
            displayMessage("New Employee Added")
        }

    }

    private fun delete(item:Employee) {
        GlobalScope.launch (Dispatchers.IO) {
            companyDB.getEmployees().deleteEmployee((item.id))
            view()
        }
    }

    private fun view() {
        lateinit var employee : MutableList<Employee>
        GlobalScope.launch(Dispatchers.IO) {
            employee = companyDB.getEmployees().getAllEmployees()

            withContext(Dispatchers.Main) {
                adapter = EmployeeAdapter(employee)
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)

                adapter.onItemDelete = {
                    item : Employee, position : Int ->

                    delete(item)
                    adapter.employeeModel.removeAt(position)
                    adapter.notifyDataSetChanged()
                    displayMessage("Employee Deleted")
                }

                adapter.onUpdate = {
                        item : Employee, position : Int ->

                    showUpdateDialog(item.name,item.salary, item.id)
                    adapter.notifyDataSetChanged()

                }

            }

        }
    }

    private fun save(employee: Employee) {
        GlobalScope.launch(Dispatchers.IO) {
            companyDB.getEmployees().addEmployee(employee)
        }
    }

    fun showUpdateDialog (name : String, salary : Int, id : Int) {
        val dialog = MaterialAlertDialogBuilder(this)
//        val binding : UpdateDialogBinding = UpdateDialogBinding.inflate(layoutInflater)
        val binding : UpdateDialogBinding = UpdateDialogBinding.inflate(layoutInflater)
//        dialog.setContentView(binding.root)
        dialog.setView(binding.root)
//        dialog.show()
            .setTitle("UPDATE EMPLOYEE DETAILS")
            .setMessage("Enter changes in employee data")
            .setPositiveButton("Update") { dialog, _ ->
                var newName = binding.etNewName.text.toString()
                var newSalary = binding.etNewSalary.text.toString()

                GlobalScope.launch(Dispatchers.IO) {

                    if(newName=="") {
                        newName = name
                    }

                    if(newSalary=="") {
                        newSalary = salary.toString()
                    }

                        companyDB.getEmployees().updateEmployee(newName,newSalary.toInt(),id)
                        displayMessage("Employee Updated")
                        view()
                }

                adapter.notifyDataSetChanged()

                /**
                 * Do as you wish with the data here --
                 * Download/Clone the repo from my Github to see the entire implementation
                 * using the link provided at the end of the article.
                 */

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                displayMessage("Operation Cancelled")
                dialog.dismiss()
            }
            .show()

//        binding.btnUpdate.setOnClickListener() {
//            var newQuantity : Int = binding.etQuantity.text.toString().toInt()
//            adapter.products[position].itemQuantity = newQuantity
//            adapter.notifyDataSetChanged()
//            dialog.dismiss()
//        }
    }

//    private fun showUpdateDialog(id : Int) {
//        val dialog = Dialog(this)
//        val binding : UpdateDialogBinding = UpdateDialogBinding.inflate(layoutInflater)
//    }

    private fun displayMessage(message : String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .setBackgroundTint(Color.parseColor("#499C54"))
            .setActionTextColor(Color.parseColor("#BB4444"))
            .setAction("DISMISS") {
//                Toast.makeText(applicationContext,"Snackbar clicked",Toast.LENGTH_LONG).show()
            }.show()
    }
}