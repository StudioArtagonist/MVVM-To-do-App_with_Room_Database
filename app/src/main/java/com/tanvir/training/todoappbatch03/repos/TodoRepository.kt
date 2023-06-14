package com.tanvir.training.todoappbatch03.repos

import com.tanvir.training.todoappbatch03.daos.TodoDao
import com.tanvir.training.todoappbatch03.entities.TodoModel

class TodoRepository(val todoDao: TodoDao) {

    suspend fun insertTodo(todoModel: TodoModel) {
        todoDao.insertTodo(todoModel)
    }

    fun getTodoByUserId(userId: Long) = todoDao.getTodosByUserId(userId)

    fun getTodoByStatusUserId(userId: Long, status: Int) =
        todoDao.getTodosByStatusAndUserId(userId, status)

    fun getTodoByPriorityAndUserId(userId: Long, priority: String) =
        todoDao.getTodosByPriorityAndUserId(userId, priority)

    suspend fun updateTodo(todoModel: TodoModel) = todoDao.updateTodo(todoModel)

    suspend fun deleteTodo(todoModel: TodoModel) = todoDao.deleteTodo(todoModel)
}