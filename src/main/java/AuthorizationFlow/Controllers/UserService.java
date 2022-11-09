package AuthorizationFlow.Controllers;


import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

class UserService {
    private static Logger logger = LogManager.getLogger(UserService.class.getName());

    private static volatile UserService userService;
    private UserRepository userRepo;

    private UserService() {
        userRepo = UserRepository.getInstance();
    }

    public static UserService getInstance() {

        UserService result = userService;

        if (result == null) {
            synchronized (UserService.class) {
                result = userService;
                if (result == null) {
                    userService = result = new UserService();
                }
            }
        }
        return result;
    }

    boolean createUser(User user) {
        return true;
    }

    boolean updateEmail(User user, String updatedEmail) throws IOException {
        logger.info("Currently in UserService - updateEmail func");

        userRepo.deleteFile(user);
        User newUser = new User(user.getId(), updatedEmail, user.getName(), user.getPassword());
        updateData(newUser);
        return true;
    }

    boolean updateName(User user, String updatedName) throws IOException {
        logger.info("Currently in UserService - updateName func");

        userRepo.deleteFile(user);
        User newUser = new User(user.getId(), user.getEmail(), updatedName, user.getPassword());
        updateData(newUser);
        return true;
    }

    boolean updatePassword(User user, String updatedPassword) throws IOException {
        logger.info("Currently in UserService - updatePassword func");

        userRepo.deleteFile(user);
        User newUser = new User(user.getId(), user.getEmail(), user.getName(), updatedPassword);
        updateData(newUser);
        return true;
    }

    boolean deleteUser(User user) {
        logger.info("Currently in UserService - deleteUser func");

        userRepo.deleteFile(user);
        return true;
    }


    void updateData(User user) throws IOException {
        logger.info("Currently in UserService - updateData func");

        userRepo.writeToFile(user.getEmail() + ".json", user);
    }
}
