package com.example.careerPilot.demo.controller;

import com.example.careerPilot.demo.Reponse.ApiResponse;
import com.example.careerPilot.demo.dto.CommunityUserDtO;
import com.example.careerPilot.demo.entity.Community;
import com.example.careerPilot.demo.entity.CommunityUser;
import com.example.careerPilot.demo.entity.User;
import com.example.careerPilot.demo.repository.CommunityRepository;
import com.example.careerPilot.demo.repository.CommunityUserRepository;
import com.example.careerPilot.demo.repository.userRepository;
import com.example.careerPilot.demo.service.CommunityUserService;
import com.example.careerPilot.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityUserController {
    @Autowired
    private final CommunityUserService communityUserService;
    @Autowired
    private final userRepository userRepository;
    @Autowired
    private final CommunityRepository communityRepository;
    @Autowired
    private final CommunityUserRepository communityUserRepository;

    //send request /api/community/request/{id}?communityId=x
    @PostMapping("/request/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> sendRequest(@PathVariable Long userId, @RequestParam Long communityId) {

        //check to see if request already sent
        if( ! (communityUserService.sendRequest(userId,communityId))){
            ApiResponse apiResponse = new ApiResponse("Request already sent", Map.of("userid",userId));
            return ResponseEntity.ok(apiResponse);
        }
        ApiResponse apiResponse = new ApiResponse("Request sent", Map.of("userid",userId));
        return ResponseEntity.ok(apiResponse);
    }

    // accept request /api/community/accept/{id}
    @PostMapping("/accept/{requestId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> acceptRequest(@PathVariable Long requestId) {
        communityUserService.acceptRequest(requestId);
        ApiResponse apiResponse = new ApiResponse("Accepted", Map.of("requestId", requestId));
        return ResponseEntity.ok(apiResponse);
    }

    // add moderator api/community/addMod/id?communityId=
    @PostMapping("/addMod/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> addMod(
            @PathVariable Long userId,
            @RequestParam Long communityId,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        //get authUser and Community
        User authUser = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Community community = communityRepository.findById(communityId).orElseThrow(() -> new RuntimeException("Community not found"));
        //find the entry from UserCommunity Table
        CommunityUser entry = communityUserRepository.findByUserAndCommunity(authUser,community);

        if(entry.getRole() == CommunityUser.role.MEMBER ) {
            ApiResponse apiResponse = new ApiResponse(authUser.getUsername() +"has no permission ", Map.of("requestId", userId));
            return ResponseEntity.ok(apiResponse);
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        communityUserService.addMod(userId,communityId);
        ApiResponse apiResponse = new ApiResponse(user.getUsername() +" has been made Moderator", Map.of("requestId", userId));
        return ResponseEntity.ok(apiResponse);
    }
    
    // add ADMIN api/community/addAdmin/id?communityId=
    @PostMapping("/addAdmin/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> addAdmin(
            @PathVariable Long userId,
            @RequestParam Long communityId,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        //get authUser and Community
        User authUser = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Community community = communityRepository.findById(communityId).orElseThrow(() -> new RuntimeException("Community not found"));
        //find the entry from UserCommunity Table
        CommunityUser entry = communityUserRepository.findByUserAndCommunity(authUser,community);

        if(entry.getRole() == CommunityUser.role.MEMBER || entry.getRole() == CommunityUser.role.MODERATOR ) {
            ApiResponse apiResponse = new ApiResponse(authUser.getUsername() +"has no permission ", Map.of("requestId", userId));
            return ResponseEntity.ok(apiResponse);
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        communityUserService.addAdmin(userId,communityId);
        ApiResponse apiResponse = new ApiResponse(user.getUsername() +" has been made Moderator", Map.of("requestId", userId));
        return ResponseEntity.ok(apiResponse);
    }
    //remove user api/community/userId?communityId=x
    @PostMapping("/remove/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> remove(@PathVariable Long userId, @RequestParam Long communityId, @AuthenticationPrincipal UserDetails userDetails) {
        try{
            User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
            Community community = communityRepository.findById(communityId).orElseThrow(() -> new RuntimeException("Community not found"));
            User authUser = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(()->new RuntimeException("User not found"));
            CommunityUser authCommunityUser = communityUserRepository.findByUserAndCommunity(user, community);
            if(authCommunityUser.getRole().equals(CommunityUser.role.MEMBER)) {
                return ResponseEntity.ok(new ApiResponse("You have no permission to remove this member", Map.of("requestId", userId)));
            }
            communityUserService.removeUser(userId,communityId,userDetails);
            return ResponseEntity.ok(new ApiResponse("Removed Succesfully", Map.of("requestId", userId)));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("An unexpected error occurred: " + e.getMessage(), null));
        }
    }
    //get request by user /getRequest?page=0&size=4
    @GetMapping("/getRequest")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getRequest(@AuthenticationPrincipal UserDetails userDetails, Pageable pageable) {
        try{
            Page<CommunityUserDtO> communityUserDtO =communityUserService.getByUser(userDetails , pageable);
            return ResponseEntity.ok(communityUserDtO);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }

    }
    //get all members of community users
    @GetMapping("/getAllMember/{communityId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllMember(Pageable pageable , @PathVariable Long communityId){
        try {
            Page<CommunityUserDtO> communityUserDtOS = communityUserService.getAllMember(pageable , communityId);
            return ResponseEntity.ok(communityUserDtOS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
