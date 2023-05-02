package org.timolisa.springsecurityclient.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.timolisa.springsecurityclient.entity.User;
import org.timolisa.springsecurityclient.event.RegistrationCompleteEvent;
import org.timolisa.springsecurityclient.service.UserService;

import java.util.UUID;

@Slf4j
@Component
public class RegistrationCompleteEventListener implements
        ApplicationListener<RegistrationCompleteEvent> {

    private final UserService userService;

    @Autowired
    public RegistrationCompleteEventListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // create the verification token for the user which will be attached
        // to the link for the user to click;
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(user, token);
        // send mail, once link is clicked
        String url =
                event.getApplicationUrl()
                        + "/verifyRegistration?token="
                        + token;


        // sendVerificationEmail() can be implemented at this point.
        log.info("Click the link to verify your account:: {}", url);

    }
}
