package com.tanvir.training.todoappbatch03.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tanvir.training.todoappbatch03.db.TodoDatabase
import com.tanvir.training.todoappbatch03.entities.TodoModel
import com.tanvir.training.todoappbatch03.repos.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val todoDao = TodoDatabase.getDB(application).todoDao()
    private val repository = TodoRepository(todoDao)
    var todoList = listOf<TodoModel>()
    fun insertTodo(todoModel: TodoModel) {
        viewModelScope.launch {
            repository.insertTodo(todoModel)
        }
    }

    fun getTodoByUserId(userId: Long) = repository.getTodoByUserId(userId)

    fun getTodoByStatusUserId(userId: Long, status: Int) =
        repository.getTodoByStatusUserId(userId, status)

    fun getTodoByPriorityAndUserId(userId: Long, priority: String) =
        repository.getTodoByPriorityAndUserId(userId, priority)

    fun updateTodo(todoModel: TodoModel) {
        viewModelScope.launch {
            repository.updateTodo(todoModel)
        }
    }

    fun deleteTodo(todoModel: TodoModel) {
        viewModelScope.launch {
            repository.deleteTodo(todoModel)
        }
    }
}