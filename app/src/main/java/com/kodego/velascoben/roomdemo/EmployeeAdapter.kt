package com.kodego.velascoben.roomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kodego.velascoben.roomdemo.databinding.RowItemBinding
import com.kodego.velascoben.roomdemo.db.Employee

class EmployeeAdapter (var employeeModel: MutableList<Employee>) : RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>(){

    inner class EmployeeViewHolder(var binding : RowItemBinding) : RecyclerView.ViewHolder(binding.root)

    var onItemDelete : ((Employee, Int) -> Unit) ? = null
    var onUpdate : ((Employee, Int) -> Unit) ? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowItemBinding.inflate(layoutInflater, parent, false)
        return EmployeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        holder.binding.apply {
            tvName.text = employeeModel[position].name
            tvSalary.text = employeeModel[position].salary.toString()
            btnDelete.setOnClickListener() {
                onItemDelete?.invoke(employeeModel[position],position)
            }
            btnEdit.setOnClickListener() {
                onUpdate?.invoke(employeeModel[position],position)
            }
        }
    }

    override fun getItemCount(): Int {
        return employeeModel.size
    }

}