package com.todolistspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class TodoController {
    private TodoService todoService;
    @Autowired
    public TodoController(TodoService todoService){
        this.todoService = todoService;
    }
    @GetMapping("/")
    public String displayTodoList(@RequestParam(required = false) String message,
                                  @RequestParam(required = false) String error,
                                  Model model){
        model.addAttribute("message", message);
        model.addAttribute("error", error);
        model.addAttribute("todoList", this.todoService.getAllTodoItems());
        return "todoList";
    }
    @GetMapping("/add-todo")
    public String displayAddTodoPage(){
        return "addTodo";
    }
    @PostMapping ("/add-todo")
    public String createTodo(Todo todo){
        try {
            System.out.println(" created todo item "+ todo);
            this.todoService.addItem(todo);
            return "redirect:/?message = TODO_CREATED_SUCCESS";
        }catch (Exception exception){
            return "redirect:/?message = TODO_CREATION_FAILED"+ exception.getMessage();
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable () UUID id){
        try{
            this.todoService.deleteTodoItem(id);
            return "redirect:/?message=TODO_DELETED_SUCCESSFULLY";
        }catch (Exception exception){
            return "redirect:/?message=TODO_DELETE_FAILED=" + exception.getMessage();
        }
    }

    @GetMapping("/update-status/{id}/{status}")
    public String updateTodo(@PathVariable () UUID id, @PathVariable String status){
        try{
            Todo foundTodo = this.todoService.findTodoById(id);
            foundTodo.setStatus(TodoStatus.valueOf(status));
            this.todoService.updateTodo(foundTodo);
            return "redirect:/?message=TODO_UPDATED_SUCCESSFULLY";
        }catch (Exception exception){
            return "redirect:/?message=TODO_UPDATED_FAILED=" + exception.getMessage();
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditeTodoPage(@PathVariable UUID id, Model model){
        try{
           Todo todo = this.todoService.findTodoById(id);
            model.addAttribute("todoItem", todo);
            return "editTodo";
        }catch (Exception exception){
            return "redirect:/?message=TODO_EDITED_FAILED&error=" + exception.getMessage();
        }
    }

    @PostMapping("/edit/{id}")
    public String editTodo(@PathVariable UUID id, Todo todo){
        try{
            this.todoService.findTodoById(id);
            todo.setId(id);
            this.todoService.updateTodo(todo);
            return "redirect:/?message=TODO_EDITED_SUCCESSFULLY";
        }catch (Exception exception){
            return "redirect:/?message=TODO_EDITED_FAILED&error=" + exception.getMessage();
        }
    }
}
