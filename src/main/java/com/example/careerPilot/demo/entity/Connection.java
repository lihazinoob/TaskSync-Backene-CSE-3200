package com.example.careerPilot.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_to_user")
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id1" , nullable = false)
    private User user1;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id2" , nullable = false)
    private User user2;
    public enum status{
        ACCEPTED,
        REJECTED,
        PENDING
    }
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "VARCHAR(10)")
    private status status;

}
