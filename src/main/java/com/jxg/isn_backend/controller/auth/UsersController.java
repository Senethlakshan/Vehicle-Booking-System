package com.jxg.isn_backend.controller.auth;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.jxg.isn_backend.dto.auth.ChangePasswordRequestDTO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.jxg.isn_backend.dto.auth.UserMinDTO;
import com.jxg.isn_backend.mapper.UserMapper;
import com.jxg.isn_backend.model.FileBlob;
import com.jxg.isn_backend.model.User;
import com.jxg.isn_backend.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private UsersService userService;

    @PutMapping("/uploadImage")
    public ResponseEntity<String> uploadImageForUser(@RequestParam("file") MultipartFile file,
                                                     @RequestHeader("Authorization") String token) {
        return userService.uploadImageForUser(file, token);
    }

    @GetMapping("/image")
    public ResponseEntity<Map<String, String>> getUserImageUrl(@RequestHeader("Authorization") String token) {
        try {
            // Extract the JWT token from the Authorization header
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            String email = userService.getUserEmailFromJwtToken(jwtToken); // Extract email from token

            // Find the user by email
            Optional<User> optionalUser = userService.findUserByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                // Get image URL
                String imageUrl = user.getImageUrl();

                // Prepare response
                Map<String, String> response = new HashMap<>();
                response.put("imageUrl", imageUrl != null ? imageUrl : "");

                return ResponseEntity.ok(response); // Return the image URL as JSON
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // User not found
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Internal server error
        }
    }

    @PutMapping("/updateDetails")
    public ResponseEntity<String> updateUserDetails(@RequestParam("firstName") String firstName,
                                                    @RequestParam("lastName") String lastName,
                                                    @RequestParam("designation") Long designation,
                                                    @RequestHeader("Authorization") String token) {
        logger.info("Received request to update user details");
        logger.info("First name: {}", firstName);
        logger.info("Last name: {}", lastName);
        logger.info("designation: {}", designation);
        logger.info("Authorization token: {}", token);

        return userService.updateUserDetails(firstName, lastName,designation,token);
    }

//    @GetMapping("/profile")
//    public ResponseEntity<String> getProfile(@RequestHeader("Authorization") String token) {
//        logger.info("Received request to get user profile");
//        return userService.getProfile(token);
//    }

    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequestDTO request,
                                                 @RequestHeader("Authorization") String token) {
        logger.info("Received request to change password");
        logger.info("Authorization token: {}", token);

        return userService.changePassword(request, token);
    }

    @PutMapping("/deactivate")
    public ResponseEntity<?> deactivateAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the email of the currently logged-in user

        logger.info("Received request to deactivate account for user: {}", email);

        return userService.deactivateCurrentUser();
    }



    @GetMapping("/designation")
    public ResponseEntity<?> getLoggedInUserDesignation(@RequestHeader("Authorization") String token) {
        return userService.getLoggedInUserDesignation(token);
    }
}


//
//package com.jxg.isn_backend.controller.auth;
//
//import com.jxg.isn_backend.dto.auth.ChangePasswordRequestDTO;
//import com.jxg.isn_backend.dto.auth.UserMinDTO;
//import com.jxg.isn_backend.mapper.UserMapper;
//import com.jxg.isn_backend.model.FileBlob;
//import com.jxg.isn_backend.model.User;
//import com.jxg.isn_backend.service.UsersService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//        import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Optional;
//
//@RestController
//@CrossOrigin
//@RequestMapping("/api/users")
//public class UsersController {
//
//    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);
//
//    @Autowired
//    private UsersService userService;
//
//    @PutMapping("/uploadImage")
//    public ResponseEntity<String> uploadImageForUser(@RequestParam("file") MultipartFile file,
//                                                     @RequestHeader("Authorization") String token) {
//        return userService.uploadImageForUser(file, token);
//    }
//
//    @GetMapping("/image")
//    public ResponseEntity<FileBlob> getUserImage(@RequestHeader("Authorization") String token) {
//        try {
//            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
//            String email = userService.getUserEmailFromJwtToken(jwtToken); // Get email from token
//
//            Optional<User> optionalUser = userService.findUserByEmail(email);
//            if (optionalUser.isPresent()) {
//                User user = optionalUser.get();
//                FileBlob fileBlob = user.getImageBlob();
//
//                if (fileBlob != null) {
//                    return ResponseEntity.ok(fileBlob);
//                } else {
//                    return ResponseEntity.notFound().build();
//                }
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
//            logger.error("Failed to retrieve user image", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @PutMapping("/updateDetails")
//    public ResponseEntity<String> updateUserDetails(@RequestParam("firstName") String firstName,
//                                                    @RequestParam("lastName") String lastName,
//                                                    @RequestHeader("Authorization") String token) {
//        logger.info("Received request to update user details");
//        logger.info("First name: {}", firstName);
//        logger.info("Last name: {}", lastName);
//        logger.info("Authorization token: {}", token);
//
//        return userService.updateUserDetails(firstName, lastName,token);
//    }
//
////    @GetMapping("/profile")
////    public ResponseEntity<String> getProfile(@RequestHeader("Authorization") String token) {
////        logger.info("Received request to get user profile");
////        return userService.getProfile(token);
////    }
//
//    @PutMapping("/changePassword")
//    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequestDTO request,
//                                                 @RequestHeader("Authorization") String token) {
//        logger.info("Received request to change password");
//        logger.info("Authorization token: {}", token);
//
//        return userService.changePassword(request, token);
//    }
//
//    @PutMapping("/deactivate")
//    public ResponseEntity<?> deactivateAccount() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName(); // Get the email of the currently logged-in user
//
//        logger.info("Received request to deactivate account for user: {}", email);
//
//        return userService.deactivateCurrentUser();
//    }
//}
