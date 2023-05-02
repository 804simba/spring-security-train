package org.timolisa.springsecurityclient.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import org.timolisa.springsecurityclient.entity.User;
import org.timolisa.springsecurityclient.entity.VerificationToken;
import org.timolisa.springsecurityclient.event.RegistrationCompleteEvent;
import org.timolisa.springsecurityclient.model.PasswordModel;
import org.timolisa.springsecurityclient.model.UserModel;
import org.timolisa.springsecurityclient.service.UserService;

import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class RegistrationController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    @Autowired
    public RegistrationController(UserService userService,
                                  ApplicationEventPublisher publisher) {
        this.userService = userService;
        this.publisher = publisher;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel,
                               final HttpServletRequest request) {
        User user = userService.registerUser(userModel);
        // once the user is saved to the database now we can create the event
        publisher.publishEvent(new RegistrationCompleteEvent(user,
                applicationUrl(request)));
        return "Success";
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam String token) {
        String result = userService.validateVerificationToken(token);
        if (result.equalsIgnoreCase("valid")) {
            return "User verification successful";
        }
        return "Bad user";
    }

    @GetMapping("/resendVerificationToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken,
                                          HttpServletRequest request) {
        VerificationToken verificationToken =
                userService.generateNewVerificationToken(oldToken);

        User user = verificationToken.getUser();
        resendVerificationEmail(user, applicationUrl(request), verificationToken);
        return "Verification link sent";
    }

    private void resendVerificationEmail(User user,
                                         String applicationUrl,
                                         VerificationToken verificationToken) {
        String url =
                applicationUrl
                        + "/verifyRegistration?token="
                        + verificationToken.getToken();


        // sendVerificationEmail() can be implemented at this point.
        log.info("Click the link to verify your account:: {}", url);
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody PasswordModel passwordModel,
                                HttpServletRequest request) {
        log.info("Reset password endpoint");
        User user = userService.findUserByEmail(
                passwordModel.getEmail());

        String url = "";

        if (user != null) {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            url = passwordResetTokenMail(user, applicationUrl(request), token);
        }
        return url;
    }

    @PostMapping("/save-password")
    public String savePassword(@RequestParam("token") String token,
                               @RequestBody PasswordModel passwordModel) {
        String result = userService.validatePasswordResetToken(token);

        if (!result.equalsIgnoreCase("valid")) {
            return "Invalid token";
        }
        Optional<User> user =
                userService.getUserByPasswordResetToken(token);

        if (user.isPresent()) {
            userService.changePassword(user.get(), passwordModel.getNewPassword());
            return "Password reset successful.";
        }
        return "Invalid token";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestBody PasswordModel passwordModel) {
        User user = userService.findUserByEmail(passwordModel.getEmail());
        if (!userService.checkIfValidOldPassword(passwordModel.getOldPassword(), user)) {
            return "Invalid old password";
        }
        // save new password
        userService.changePassword(user, passwordModel.getNewPassword());
        return "Password changed successfully";
    }

    private String passwordResetTokenMail(User user, String applicationUrl, String token) {
        String url =
                applicationUrl
                        + "/save-password?token="
                        + token;


        // sendVerificationEmail() can be implemented at this point.
        log.info("Click the link to reset your password:: {}", url);
        return url;
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://"
                + request.getServerName() +
                ":" +
                request.getServerPort() +
                request.getContextPath();
    }
}
