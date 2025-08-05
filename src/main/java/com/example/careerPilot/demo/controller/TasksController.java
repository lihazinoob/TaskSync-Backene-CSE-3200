package com.example.careerPilot.demo.controller;

import com.example.careerPilot.demo.dto.TasksDTO;
import com.example.careerPilot.demo.service.TasksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TasksController {

    private final TasksService tasksService;

    @GetMapping("/project/{projectId}")
    public List<TasksDTO> getAllTasks(@PathVariable Long projectId ) {
        return tasksService.getAllTasks(projectId );
    }
    @GetMapping("/assigned-to/{userId}")
    public List<TasksDTO> getTasksByAssignedTo(@PathVariable Long userId) {
        return tasksService.getTasksByAssignedTo(userId);
    }
    @GetMapping("/assigned-by/{userId}")
    public List<TasksDTO> getTasksByAssignedBy(@PathVariable Long userId) {
        return tasksService.getTasksByAssignedBy(userId);
    }
    @PostMapping("/{parentId}/subtasks")
    public ResponseEntity<TasksDTO> createChildTask(@PathVariable Long parentId, @RequestBody TasksDTO dto) {
        TasksDTO created = tasksService.createChildTask(parentId, dto);
        return created != null ? ResponseEntity.ok(created) : ResponseEntity.notFound().build();
    }

    // Get all child tasks for a parent task
    @GetMapping("/{parentId}/subtasks")
    public List<TasksDTO> getChildTasks(@PathVariable Long parentId) {
        return tasksService.getChildTasks(parentId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TasksDTO> getTaskById(@PathVariable Long id) {
        TasksDTO dto = tasksService.getTaskById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TasksDTO> createTask(@RequestBody TasksDTO dto) {
        TasksDTO created = tasksService.createTask(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TasksDTO> updateTask(@PathVariable Long id, @RequestBody TasksDTO dto) {
        TasksDTO updated = tasksService.updateTask(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    @PutMapping("/{id}/complete")
    public ResponseEntity<TasksDTO> completeTask(@PathVariable Long id) {
        TasksDTO completed = tasksService.completeTask(id);
        return completed != null ? ResponseEntity.ok(completed) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        tasksService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}