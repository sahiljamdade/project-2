package com.example;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private static final List<Todo> todos = new ArrayList<>();
    private static int nextId = 1;

    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        todo.setId(nextId++);
        todos.add(todo);
        return ResponseEntity.ok(todo);
    }

    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos() {
        return ResponseEntity.ok(todos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable int id) {
        return todos.stream()
                .filter(todo -> todo.getId() == id)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<Todo> updateTodoStatus(@PathVariable int id, @RequestBody Map<String, String> statusUpdate) {
        Todo todo = todos.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);
        if (todo == null) {
            return ResponseEntity.notFound().build();
        }
        todo.setStatus(statusUpdate.get("status"));
        return ResponseEntity.ok(todo);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<String> deleteTodoById(@PathVariable int id) {
        boolean removed = todos.removeIf(todo -> todo.getId() == id);
        if (removed) {
            return ResponseEntity.ok("Todo deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }

    // New Reset Endpoint
    @PostMapping("/reset")
    public ResponseEntity<String> resetTodos() {
        todos.clear();  // Clears the list of todos
        nextId = 1;     // Resets the ID counter
        return ResponseEntity.ok("All todos have been reset.");
    }

    // Todo class remains the same
    private static class Todo {
        private int id;
        private String description;
        private String status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
          this.description = description;
       }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
