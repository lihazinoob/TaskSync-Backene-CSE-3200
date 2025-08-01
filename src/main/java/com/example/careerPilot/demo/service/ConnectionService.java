package com.example.careerPilot.demo.service;

import com.example.careerPilot.demo.dto.ConnectionDTO;
import com.example.careerPilot.demo.entity.Connection;
import com.example.careerPilot.demo.entity.User;
import com.example.careerPilot.demo.repository.ConnectionRepository;
import com.example.careerPilot.demo.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConnectionService {
    @Autowired
    private ConnectionRepository connectionRepository;
    @Autowired
    private userRepository userRepository;

    public Connection addConnection(Long userId , User user) {
        Connection connection = new Connection();
        User user2 =  userRepository.findById(userId).orElseThrow(()->new RuntimeException("User Not Found"));
        if(connectionRepository.findByUser1AndUser2(user , user2) ==null){
            connection.setUser2(user2);
            connection.setUser1(user);
            connection.setStatus(Connection.status.PENDING);
            return connectionRepository.save(connection);
        }
        throw new RuntimeException("Already Sent");

    }

    public Connection acceptConnection(Long connectionId, User user) {
        Connection connection = connectionRepository.findById(connectionId).orElseThrow(()->new RuntimeException("Connection Not Found"));
        if(connection.getUser2().getId().equals(user.getId())){
            connection.setStatus(Connection.status.ACCEPTED);
            return connectionRepository.save(connection);
        }else {
            throw new RuntimeException(user.getUsername() + " Not Authorized ");
        }

    }

    public Page<ConnectionDTO> getAllConnections(User user, Pageable pageable) {
        List<Connection> connections = connectionRepository.findByUser2(user);
        if(connections.isEmpty()){
            throw new RuntimeException("Empty Connection");
        }
        List<ConnectionDTO> connectionDTOS = new ArrayList<>();
        for (Connection connection : connections) {
            if(connection.getStatus().equals(Connection.status.ACCEPTED)){
                ConnectionDTO connectionDTO = ConnectionDTO.fromEntity(connection);
                connectionDTOS.add(connectionDTO);
            }
        }
        return new PageImpl<>(connectionDTOS, pageable, connectionDTOS.size());
    }

    public Page<ConnectionDTO> getAllConnectionRequest(User user, Pageable pageable) {
        List<Connection> connections = connectionRepository.findByUser2(user);
        if(connections.isEmpty()){
            throw new RuntimeException("Empty Connection");
        }
        List<ConnectionDTO> connectionDTOS = new ArrayList<>();
        for (Connection connection : connections) {
            if(connection.getStatus().equals(Connection.status.PENDING)){
                ConnectionDTO connectionDTO = ConnectionDTO.fromEntity(connection);
                connectionDTOS.add(connectionDTO);
            }
        }
        return new PageImpl<>(connectionDTOS, pageable, connections.size());
    }
}
