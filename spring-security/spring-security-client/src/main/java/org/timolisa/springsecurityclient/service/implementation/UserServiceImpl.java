package org.timolisa.springsecurityclient.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.timolisa.springsecurityclient.entity.PasswordResetToken;
import org.timolisa.springsecurityclient.entity.User;
import org.timolisa.springsecurityclient.entity.VerificationToken;
import org.timolisa.springsecurityclient.model.UserModel;
import org.timolisa.springsecurityclient.repository.PasswordResetTokenRepository;
import org.timolisa.springsecurityclient.repository.UserRepository;
import org.timolisa.springsecurityclient.repository.VerificationTokenRepository;
import org.timolisa.springsecurityclient.service.UserService;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           VerificationTokenRepository verificationTokenRepository,
                           PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public User registerUser(UserModel userModel) {
        User user = userModelToUser(userModel);
        userRepository.save(user);
        return user;
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken =
                verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return "invalid token";
        }

        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();

        if ((verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            verificationTokenRepository.delete(verificationToken);
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        // get the old token, generate a new one and return it.
        VerificationToken verificationToken =
                verificationTokenRepository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken =
                new PasswordResetToken(user, token);
                passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken =
                passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken == null) {
            return "invalid token";
        }

        User user = passwordResetToken.getUser();
        log.info("User from verification token: {}", user);

        Calendar calendar = Calendar.getInstance();

        if ((passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            passwordResetTokenRepository.delete(passwordResetToken);
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository
                .findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(String oldPassword, User user) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public void saveVerificationTokenForUser(User user, String token) {
        VerificationToken verificationToken =
                new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    private User userModelToUser(UserModel userModel) {
        return User.builder()
                .firstName(userModel.getFirstName())
                .lastName(userModel.getLastName())
                .password(passwordEncoder
                        .encode(userModel.getPassword()))
                .email(userModel.getEmail())
                .role("USER")
                .build();
    }
}
