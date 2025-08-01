package com.example.careerPilot.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "job_skills")
public class JobSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id" , nullable = false)
    private JobPost job;

//    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id" , nullable = false)
    private Skill skill;

    @Column(name = "proficiency_level", nullable = false)
    @Enumerated(EnumType.STRING)

    private ProficiencyLevel proficiencyLevel;
    public enum ProficiencyLevel {
        BEGINNER,
        INTERMEDIATE,
        EXPERT
    }

}
