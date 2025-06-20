package at.boot.controller;

import at.boot.models.User;
import at.boot.responses.UserProfileDto;
import at.boot.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/profile")
    public UserProfileDto getUserProfile(@AuthenticationPrincipal User user,
                                         HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Date issuedAt = (Date) request.getAttribute("jwtIssuedAt");
        Date expiration = (Date) request.getAttribute("jwtExpiration");

        return userService.getUserProfileByUsername(username, issuedAt, expiration);
    }
}
