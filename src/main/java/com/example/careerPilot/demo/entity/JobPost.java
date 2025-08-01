package com.example.careerPilot.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Builder
@Data
@Entity
@Table(name = "job_posts")
@AllArgsConstructor
@NoArgsConstructor
public class JobPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "job_description", columnDefinition = "TEXT", nullable = false)
    private String jobDescription;

    @Column(name = "requirements", columnDefinition = "TEXT")
    private String requirements;

    @Column(name = "upper_range_salary")
    private int upperSalary;
    @Column(name = "lower_range_salary")
    private int lowerSalary;

    @Column(name = "location")
    private String location;


@Column(name = "job_type", columnDefinition = "ENUM('full_time', 'part_time', 'contract', 'internship', 'temporary') default 'full_time'")
@Convert(converter = JobType.JobTypeConverter.class)
private JobType jobType = JobType.FULL_TIME;

    @Column(name = "job_category")
    private String jobCategory;

    @Column(name = "application_deadline")
    private LocalDate applicationDeadline;

    @Column(name = "status", columnDefinition = "ENUM('open', 'closed', 'pending') default 'open'")
    @Enumerated(EnumType.STRING)
    private Status status = Status.OPEN;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public enum JobType {
        FULL_TIME("full_time"),
        PART_TIME("part_time"),
        CONTRACT("contract"),
        INTERNSHIP("internship"),
        TEMPORARY("temporary");

        private final String dbValue;

        JobType(String dbValue) {
            this.dbValue = dbValue;
        }

        public String getDbValue() {
            return dbValue;
        }

        @Converter(autoApply = true)
        public static class JobTypeConverter implements AttributeConverter<JobType, String> {
            @Override
            public String convertToDatabaseColumn(JobType jobType) {
                return jobType != null ? jobType.getDbValue() : null;
            }

            @Override
            public JobType convertToEntityAttribute(String dbValue) {
                if (dbValue == null) return null;

                return Arrays.stream(JobType.values())
                        .filter(jt -> jt.getDbValue().equalsIgnoreCase(dbValue))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Unknown job type: " + dbValue));
            }
        }
    }
    // Enum for status
    public enum Status {
        OPEN,
        CLOSED,
        PENDING
    }

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default // Add this for Lombok builder
    private List<JobSkill> jobSkills = new ArrayList<>();


    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<JobApplication> applications = new ArrayList<>();

    @Column(name = "is_fulfilled", columnDefinition = "boolean default false")
    private boolean fulfilled = false;

    public void addJobSkill(JobSkill jobSkill) {
        jobSkills.add(jobSkill);
        jobSkill.setJob(this);
    }

    public void removeJobSkill(JobSkill jobSkill) {
        jobSkills.remove(jobSkill);
        jobSkill.setJob(null);
    }
}