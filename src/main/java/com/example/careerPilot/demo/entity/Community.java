package com.example.careerPilot.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "community", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "category")
    private String category;

    @Column(name = "visibility", columnDefinition = "VARCHAR(10) default 'PUBLIC'")
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Column(name = "member_count", columnDefinition = "BIGINT default 0")
    private Long memberCount = 0L ;

    public enum Visibility {
        PUBLIC, FRIEND
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "community" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<CommunityUser> communityUsers;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Post> posts;

}