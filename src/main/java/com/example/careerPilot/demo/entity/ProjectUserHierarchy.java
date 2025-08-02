package com.example.careerPilot.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "project_user_hierarchy",
        uniqueConstraints = @UniqueConstraint(name = "unique_project_subordinate", columnNames = {"project_id", "subordinate_user_id"})
)
public class ProjectUserHierarchy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", referencedColumnName = "projectId", nullable = false, foreignKey = @ForeignKey(name = "fk_hierarchy_project"))
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "superior_user_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_hierarchy_superior_user"))
    private User superiorUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subordinate_user_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_hierarchy_subordinate_user"))
    private User subordinateUser;
}
