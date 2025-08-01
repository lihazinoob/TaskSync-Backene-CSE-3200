package com.example.careerPilot.demo.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "skills"
)
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skillId;
    @NonNull
    @Column(name = "skillName" , nullable = false)
    private String skillName;
    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<UserSkill> userSkills;
    @OneToMany(mappedBy = "skill" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<JobSkill> jobSkills;

}
