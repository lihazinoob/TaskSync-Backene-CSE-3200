package com.example.careerPilot.demo.dto;

import com.example.careerPilot.demo.entity.Connection;
import com.example.careerPilot.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionDTO {
    private Long id;
    private String user1;
    private String user2;
    private String status;

    public static ConnectionDTO fromEntity(Connection connection) {
        return ConnectionDTO.builder()
                .id(connection.getId())
                .user1(connection.getUser1().getUsername())
                .user2(connection.getUser2().getUsername())
                .status(connection.getStatus().toString())
                .build();
    }
}
