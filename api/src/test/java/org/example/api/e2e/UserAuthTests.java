package org.example.api.e2e;

import com.github.javafaker.Faker;
import org.example.api.e2e.preconditions.UserPreconditions;
import org.example.api.users.repositories.UserRepository;
import org.example.api.users.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.example.api.e2e.steps.UserSteps;


public class UserAuthTests {

    private final Faker faker = new Faker();

    UserSteps userSteps = new UserSteps();
    UserPreconditions userPreconditions = new UserPreconditions();


    @Test
    public void userCanLogin() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();

        userPreconditions.userIsRegistered(email, password);
        userSteps.userLogsIn(email, password);
        userSteps.deleteUser(email);
    }

    @Test
    public void userCanRegister() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();

        userSteps.userCanRegister(email, password);
        userSteps.deleteUser(email);
    }


}
