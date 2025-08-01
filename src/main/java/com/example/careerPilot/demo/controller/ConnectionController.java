package com.example.careerPilot.demo.controller;

import com.example.careerPilot.demo.Reponse.ApiResponse;
import com.example.careerPilot.demo.dto.ConnectionDTO;
import com.example.careerPilot.demo.entity.Connection;
import com.example.careerPilot.demo.entity.User;
import com.example.careerPilot.demo.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/connection")
@RequiredArgsConstructor
public class ConnectionController {
    private final ConnectionService connectionService;

    //send connection request /api/connection/add/userId
    @PostMapping("/add/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addConnection(@PathVariable Long userId , @AuthenticationPrincipal User user) {
        try{
            connectionService.addConnection(userId,user);
            return ResponseEntity.ok(new ApiResponse("Sent Succesfully", Map.of("requestId", userId)));
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //accept connection /accept/connectionId
    @PostMapping("/accept/{connectionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> acceptConnection(@PathVariable Long connectionId, @AuthenticationPrincipal User user) {
        try{
            connectionService.acceptConnection(connectionId,user);
            return ResponseEntity.ok(new ApiResponse("Accepted", Map.of("requestId", connectionId)));
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //get connections of authenticated user
    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllConnections(@AuthenticationPrincipal User user, Pageable pageable) {
        try{
            Page<ConnectionDTO> connectionDTOS = connectionService.getAllConnections(user, pageable);
            return new ResponseEntity<>(connectionDTOS, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    // get connection request of authenticated user
    @GetMapping("/requests")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllConnectionsRequests(@AuthenticationPrincipal User user, Pageable pageable) {
        try {
            Page<ConnectionDTO> connectionDTOS = connectionService.getAllConnectionRequest(user,pageable);
            return new ResponseEntity<>(connectionDTOS, HttpStatus.OK);

        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

