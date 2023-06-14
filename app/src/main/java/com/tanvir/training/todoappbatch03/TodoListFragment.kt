package com.tanvir.training.todoappbatch03

import CustomAlertDialog
import android.os.Bundle
import android.view.*
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tanvir.training.todoappbatch03.adapters.TodoAdapter
import com.tanvir.training.todoappbatch03.databinding.FragmentTodoListBinding
import com.tanvir.training.todoappbatch03.entities.TodoModel
import com.tanvir.training.todoappbatch03.prefdata.LoginPreference
import com.tanvir.training.todoappbatch03.viewmodels.TodoViewModel

import kotlinx.coroutines.launch
import priority_all
import priority_high
import priority_low
import priority_normal
import show_all
import show_completed
import show_incompleted
import todo_delete
import todo_edit
import todo_edit_completed

class TodoListFragment : Fragment() {
    private lateinit var binding: FragmentTodoListBinding
    private lateinit var preference: LoginPreference
    private var userId = 0L
    private var menuItemOption = show_all
    private var priorityOption = priority_all
    private lateinit var adapter: TodoAdapter
    private val todoViewModel: TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.todo_list_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val completedItem: MenuItem = menu.findItem(R.id.item_completed)
        val incompletedItem: MenuItem = menu.findItem(R.id.item_incompleted)
        val allItem: MenuItem = menu.findItem(R.id.item_all)

        if (menuItemOption == show_all) {
            allItem.isVisible = false
            completedItem.isVisible = true
            incompletedItem.isVisible = true
        } else if (menuItemOption == show_completed) {
            allItem.isVisible = true
            completedItem.isVisible = false
            incompletedItem.isVisible = true
        } else if (menuItemOption == show_incompleted) {
            allItem.isVisible = true
            completedItem.isVisible = true
            incompletedItem.isVisible = false
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_logout -> {
                lifecycle.coroutineScope.launch {
                    preference.setLoginStatus(status = false, 0L, requireContext())
                }
            }

            R.id.item_completed -> {
                menuItemOption = show_completed
                lifecycle.coroutineScope.launch {
                    todoViewModel
                        .getTodoByStatusUserId(userId, 1)
                        .observe(viewLifecycleOwner) {
                            adapter.submitList(it)
                        }
                }
            }

            R.id.item_incompleted -> {
                menuItemOption = show_incompleted
                lifecycle.coroutineScope.launch {
                    todoViewModel
                        .getTodoByStatusUserId(userId, 0)
                        .observe(viewLifecycleOwner) {
                            adapter.submitList(it)
                        }
                }
            }

            R.id.item_all -> {
                menuItemOption = show_all
                lifecycle.coroutineScope.launch {
                    todoViewModel
                        .getTodoByUserId(userId)
                        .observe(viewLifecycleOwner) {
                            adapter.submitList(it)
                        }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoListBinding.inflate(inflater, container, false)
        preference = LoginPreference(requireContext())
        adapter = TodoAdapter{model, action ->
            performTodoRowAction(model, action)
        }
        binding.todoRV.layoutManager = LinearLayoutManager(requireActivity())
        binding.todoRV.adapter = adapter
        preference.isLoggedInFlow
            .asLiveData()
            .observe(viewLifecycleOwner){
            if (!it) {
                findNavController().navigate(R.id.action_todoListFragment_to_loginFragment)
            }
        }

        preference.userIdFlow.asLiveData()
            .observe(viewLifecycleOwner) {
                userId = it
                todoViewModel.getTodoByUserId(it).observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                    todoViewModel.todoList = it
                }
            }

        binding.cbLayout.setOnCheckedChangeListener { radioGroup, i ->
            priorityOption = when(i) {
                R.id.lowCB -> priority_low
                R.id.normalCB -> priority_normal
                R.id.highCB -> priority_high
                else -> priority_all
            }
            getFilteredList()
        }


        binding.newTodoFab.setOnClickListener {
            findNavController().navigate(R.id.action_todoListFragment_to_newTodoFragment)
        }
        return binding.root
    }

    private fun getFilteredList() {

        if (priorityOption == priority_all) {
            adapter.submitList(todoViewModel.todoList)
        } else {
            val temp = mutableListOf<TodoModel>()
            todoViewModel.todoList.forEach {
                if (it.priority == priorityOption) {
                    temp.add(it)
                }
            }
            adapter.submitList(temp)
        }
    }

    private fun performTodoRowAction(model: TodoModel, action: String) {
        when(action) {
            todo_edit_completed -> {
                todoViewModel.updateTodo(model)
            }
            todo_delete -> {
                CustomAlertDialog(
                    icon = R.drawable.ic_baseline_delete_24,
                    title = "Delete",
                    body = "Sure to delete this item?",
                    posBtnText = "YES",
                    negBtnText = "CANCEL",
                ) {
                    todoViewModel.deleteTodo(model)
                }.show(childFragmentManager, null)
            }
            todo_edit -> {

            }
        }
    }
}