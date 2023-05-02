package org.timolisa.springsecurityclient.service;

import org.springframework.stereotype.Service;
import org.timolisa.springsecurityclient.entity.User;
import org.timolisa.springsecurityclient.entity.VerificationToken;
import org.timolisa.springsecurityclient.model.UserModel;

import java.util.Optional;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(User user, String token);

    String validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String oldToken);

    User findUserByEmail(String email);

    void createPasswordResetTokenForUser(User user, String token);

    String validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void changePassword(User user, String newPassword);

    boolean checkIfValidOldPassword(String oldPassword, User user);
}
