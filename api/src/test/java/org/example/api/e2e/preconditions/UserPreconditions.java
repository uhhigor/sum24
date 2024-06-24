package org.example.api.e2e.preconditions;


import org.example.api.e2e.steps.UserSteps;

public class UserPreconditions {

    UserSteps userSteps = new UserSteps();

    public void userIsRegistered(String email, String password) {
        userSteps.userCanRegister(email, password);

    }

    public String userIsLoggedIn(String email, String password) {
        userSteps.userCanRegister(email, password);
        String token = userSteps.userLogsIn(email, password);
        return token;
    }
}
