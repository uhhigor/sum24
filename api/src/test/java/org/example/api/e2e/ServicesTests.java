package org.example.api.e2e;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.example.api.e2e.steps.UserSteps;
import org.example.api.e2e.steps.ServiceSteps;
import org.example.api.e2e.preconditions.UserPreconditions;


public class ServicesTests {

    UserSteps userSteps = new UserSteps();
    ServiceSteps serviceSteps = new ServiceSteps();

    UserPreconditions userPreconditions = new UserPreconditions();

    private final Faker faker = new Faker();


    @Test
    public void userCanAddService() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        userPreconditions.userIsLoggedIn(email, password);
        String userId = userSteps.getUserId(email);
        serviceSteps.userAddsService(userId, "name", faker.internet().url());
        userSteps.deleteUser(email);
    }

    @Test
    public void userCanDeleteService() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        userPreconditions.userIsLoggedIn(email, password);
        String userid = userSteps.getUserId(email);
        int serviceId = serviceSteps.userAddsService(userid, "name", faker.internet().url());
        serviceSteps.userDeletesService(serviceId);
        userSteps.deleteUser(email);

    }

    @Test
    public void userCanUpdateService() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String token = userPreconditions.userIsLoggedIn(email, password);
        String userid = userSteps.getUserId(email);
        int serviceId = serviceSteps.userAddsService(userid, "name", faker.internet().url());
        serviceSteps.userUpdatesService(serviceId, token);
        userSteps.deleteUser(email);
    }

    @Test
    public void userCanGetAllOfTheirServices() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String token = userPreconditions.userIsLoggedIn(email, password);
        String userid = userSteps.getUserId(email);
        int serviceId = serviceSteps.userAddsService(userid, "name", faker.internet().url());
        int serviceId2 = serviceSteps.userAddsService(userid, "name2", faker.internet().url());
        serviceSteps.userGetsAllOfTheirServices(userid, token);
        userSteps.deleteUser(email);
    }

    @Test
    public void userCanGetLastServiceStatus() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        userPreconditions.userIsLoggedIn(email, password);
        String userid = userSteps.getUserId(email);
        int serviceId = serviceSteps.userAddsService(userid, "name", faker.internet().url());
        serviceSteps.userGetsLastServiceStatus(serviceId);
        userSteps.deleteUser(email);
    }
}
