package AuthorizationFlow.Controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.*;

class AuthenticationService {
    private static Logger logger = LogManager.getLogger(AuthenticationService.class.getName());

    private static volatile AuthenticationService authService;
    private UserRepository userRepo;
    Map<String, User> userTokens;

    private AuthenticationService() {
        this.userTokens = new HashMap<>();
        userRepo = UserRepository.getInstance();
    }

    public static AuthenticationService getInstance() {

        AuthenticationService result = authService;

        if (result == null) {
            synchronized (AuthenticationService.class) {
                result = authService;
                if (result == null) {
                    authService = result = new AuthenticationService();
                }
            }
        }
        return result;
    }

    void register(String email, String name, String password) {
        logger.info("Currently in AuthenticationService - register");

        if (!checkIfUserExists(email)) {
            User user = new User(UUID.randomUUID().hashCode(), email, name, password);
            try {
                logger.warn("write to file");
                userRepo.writeToFile(user.getEmail() + ".json", user);
            } catch (IOException e) {
                logger.fatal(e);
                System.out.println("Couldn't write to file");
                throw new RuntimeException(e);
            }
        }
    }

    User validate(String token) {
        if (!userTokens.containsKey(token)) {
            throw new InvalidParameterException("Token incorrect");
        }
        return userTokens.get(token);
    }


    String login(String email, String password) {
        logger.info("Currently in AuthenticationService - login");

        User cachedUser = userRepo.readFromCache(email);
        if (cachedUser == null) {
            throw new IllegalArgumentException("user doesn\"t exist");
        } else if (!Objects.equals(cachedUser.getPassword(), password)) {
            throw new IllegalArgumentException("wrong password");
        }
        return createToken(cachedUser);
    }

    private String createToken(User user) {
        logger.info("Currently in AuthenticationService - createToken");
        String token = UUID.randomUUID().toString();
        userTokens.put(token, user);
        return token;
    }

    void reloadUser(String email, String token) {
        logger.info("Currently in AuthenticationService - reloadUser");
        User updatedUser = userRepo.readFromCache(email);
        userTokens.put(token, updatedUser);
    }

    void removeToken(String token) {
        logger.info("Currently in AuthenticationService - removeToken");
        userTokens.remove(token);
    }


    private static boolean checkIfUserExists(String email) {
        logger.info("Currently in AuthenticationService - checkIfUserExists");

        try (FileReader fr = new FileReader(email + ".json")) {
            logger.warn("read from file");
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}

