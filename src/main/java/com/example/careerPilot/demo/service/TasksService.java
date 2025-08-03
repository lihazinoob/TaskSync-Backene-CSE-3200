package com.example.careerPilot.demo.service;


import com.example.careerPilot.demo.dto.TasksDTO;
import com.example.careerPilot.demo.entity.Project;
import com.example.careerPilot.demo.entity.Tasks;
import com.example.careerPilot.demo.entity.User;
import com.example.careerPilot.demo.repository.ProjectRepository;
import com.example.careerPilot.demo.repository.TasksRepository;
import com.example.careerPilot.demo.repository.userRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TasksService {

    private final TasksRepository tasksRepository;
    private final com.example.careerPilot.demo.repository.userRepository userRepository;
    private final ProjectRepository projectRepository;

    public List<TasksDTO> getAllTasks() {
        return tasksRepository.findAll().stream()
                .map(TasksDTO::fromEntity)
                .collect(Collectors.toList());
    }
    public List<TasksDTO> getTasksByAssignedTo(Long userId) {
        return tasksRepository.findAll().stream()
                .filter(task -> task.getAssignedTo() != null && userId.equals(task.getAssignedTo().getId()))
                .map(TasksDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<TasksDTO> getTasksByAssignedBy(Long userId) {
        return tasksRepository.findAll().stream()
                .filter(task -> task.getAssignedBy() != null && userId.equals(task.getAssignedBy().getId()))
                .map(TasksDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public TasksDTO getTaskById(Long id) {
        return tasksRepository.findById(id)
                .map(TasksDTO::fromEntity)
                .orElse(null);
    }
    @Transactional
    public TasksDTO completeTask(Long id) {
        Optional<Tasks> optionalTask = tasksRepository.findById(id);
        if (optionalTask.isEmpty()) return null;
        Tasks task = optionalTask.get();
        task.setStatus(true);
        task.setUpdatedAt(java.time.LocalDateTime.now());
        if (task.getProject() != null) {
            updateProject(task.getProject() != null ? task.getProject().getProjectId() : null);
        }

        return TasksDTO.fromEntity(tasksRepository.save(task));
    }

    @Transactional
    public TasksDTO createTask(TasksDTO dto) {
        Tasks task = Tasks.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .dueDate(dto.getDueDate())
                .status(dto.getStatus())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .assignedBy(getUserById(dto.getAssignedById()))
                .assignedTo(getUserById(dto.getAssignedToId()))
                .parent(dto.getParentId() != null ? tasksRepository.findById(dto.getParentId()).orElse(null) : null)
                .project(getProjectById(dto.getProjectId()))
                .build();
        updateProject(dto.getProjectId());
        return TasksDTO.fromEntity(tasksRepository.save(task));

    }

    @Transactional
    public TasksDTO updateTask(Long id, TasksDTO dto) {
        Optional<Tasks> optionalTask = tasksRepository.findById(id);
        if (optionalTask.isEmpty()) return null;
        Tasks task = optionalTask.get();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setStatus(dto.getStatus());
        task.setUpdatedAt(dto.getUpdatedAt());
        if (dto.getAssignedById() != null) task.setAssignedBy(getUserById(dto.getAssignedById()));
        if (dto.getAssignedToId() != null) task.setAssignedTo(getUserById(dto.getAssignedToId()));
        if (dto.getParentId() != null) task.setParent(tasksRepository.findById(dto.getParentId()).orElse(null));
        if (dto.getProjectId() != null) task.setProject(getProjectById(dto.getProjectId()));
        if (task.getProject() != null) {
            updateProject(dto.getProjectId());
        }
        return TasksDTO.fromEntity(tasksRepository.save(task));
    }
    @Transactional
    public TasksDTO createChildTask(Long parentId, TasksDTO dto) {
        Tasks parent = tasksRepository.findById(parentId).orElse(null);
        if (parent == null) return null;
        Tasks childTask = Tasks.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .dueDate(dto.getDueDate())
                .status(dto.getStatus())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .assignedBy(getUserById(dto.getAssignedById()))
                .assignedTo(getUserById(dto.getAssignedToId()))
                .parent(parent)
                .project(getProjectById(dto.getProjectId()))
                .build();
        updateProject(dto.getProjectId());
        return TasksDTO.fromEntity(tasksRepository.save(childTask));
    }

    // Get all child tasks for a parent task
    public List<TasksDTO> getChildTasks(Long parentId) {
        return tasksRepository.findAll().stream()
                .filter(task -> task.getParent() != null && parentId.equals(task.getParent().getId()))
                .map(TasksDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteTask(Long id) {
        Tasks task = tasksRepository.findById(id).orElseThrow(()-> new RuntimeException("Task not found with id: " + id));
        if (task.getProject() != null) {
            updateProject(task.getProject().getProjectId());
        }
        tasksRepository.deleteById(id);
    }

    // update project details after task operations
    private void updateProject(Long projectId) {
        Project project = getProjectById(projectId);
        if (project != null) {
            updateProjectTaskCount(projectId);
            updateProjectTasksCompleted(projectId);
            updateProjectCompletion(projectId);
        }
    }
    private void updateProjectCompletion(Long projectId) {
        Project project = getProjectById(projectId);
        if (project != null) {
            int totalTasks = project.getTotalTasks();
            int completedTasks = project.getTasksCompleted();
            BigDecimal completion = BigDecimal.ZERO;
            if (totalTasks > 0) {
                completion = BigDecimal.valueOf(completedTasks)
                        .divide(BigDecimal.valueOf(totalTasks), 2, BigDecimal.ROUND_HALF_UP);
            }
            project.setCompletion(completion);
            projectRepository.save(project);
        }
    }
    private void updateProjectTaskCount(Long projectId) {
        Project project = getProjectById(projectId);
        if (project != null) {
            int totalTasks = tasksRepository.countByProject_ProjectId(projectId);
            project.setTotalTasks(totalTasks);
            projectRepository.save(project);
        }
    }
    private void updateProjectTasksCompleted(Long projectId) {
        Project project = getProjectById(projectId);
        if (project != null) {
            int completedTasks = (int) tasksRepository.findAll().stream()
                    .filter(task -> task.getProject() != null
                            && projectId.equals(task.getProject().getProjectId())
                            && Boolean.TRUE.equals(task.getStatus()))
                    .count();
            project.setTasksCompleted(completedTasks);
            projectRepository.save(project);
        }
    }

   //getting things
    private User getUserById(Long id) {
        return id == null ? null : userRepository.findById(id).orElse(null);
    }
    private Project getProjectById(Long id) {
        return id == null ? null : projectRepository.findById(id).orElse(null);
    }
}