package com.example.careerPilot.demo.dto;


import com.example.careerPilot.demo.entity.Tasks;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class TasksDTO {
    private Long id;
    private String title;
    private String description;
    private String dueDate;
    private Boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long assignedById;
    private Long assignedToId;
    private Long parentId;
    private List<Long> subTaskIds;
    private Long projectId;

    public static TasksDTO fromEntity(Tasks task) {
        if (task == null) return null;
        return TasksDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .projectId(task.getProject() != null ? task.getProject().getProjectId() : null)
                .assignedById(task.getAssignedBy() != null ? task.getAssignedBy().getId() : null)
                .assignedToId(task.getAssignedTo() != null ? task.getAssignedTo().getId() : null)
                .parentId(task.getParent() != null ? task.getParent().getId() : null)
                .subTaskIds(task.getSubTasks() != null
                        ? task.getSubTasks().stream().map(Tasks::getId).collect(Collectors.toList())
                        : null)
                .build();
    }
}