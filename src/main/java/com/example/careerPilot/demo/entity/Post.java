    package com.example.careerPilot.demo.entity;

    import com.example.careerPilot.demo.converter.VisibilityConverter;
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import org.springframework.lang.Nullable;

    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;

    @Entity
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name = "Post")
    public class Post {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long postId;

        @Column(columnDefinition = "TEXT", nullable = false)
        private String content;

        @Column(length = 255)
        private String image;

        @Convert(converter = VisibilityConverter.class)
        @Column(nullable = false, columnDefinition = "enum('public', 'friends', 'private') default 'public'")
        private Visibility visibility = Visibility.PUBLIC;

        @Column(name = "likes_count", nullable = false)
        private int likesCount = 0;

        @Column(name = "shares_count", nullable = false)
        private int sharesCount = 0;

        @Column(name = "approved" , nullable = false)
        private boolean approved = false;

        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        @Column(name = "deleted_at")
        private LocalDateTime deletedAt;

        @PrePersist
        protected void onCreate() {
            createdAt = LocalDateTime.now();
        }

        @PreUpdate
        protected void onUpdate() {
            updatedAt = LocalDateTime.now();
        }

        public enum Visibility {
            PUBLIC, FRIENDS, PRIVATE
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        @JsonIgnoreProperties({"posts", "password", "authorities"})
        private User user;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "community_id")
        @JsonIgnoreProperties({"posts", "password", "authorities"})
        private Community community = null;



        // Post to Comments
        @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonIgnoreProperties("post")
        private List<Comment> comments = new ArrayList<>();


    }