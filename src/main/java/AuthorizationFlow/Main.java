package AuthorizationFlow;

import AuthorizationFlow.Controllers.AuthenticationController;
import AuthorizationFlow.Controllers.UserController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.security.InvalidParameterException;

public class Main {
    private static Logger logger = LogManager.getLogger(Main.class.getName());



    public static void main(String[] args) {
        logger.info("Start main Func");
        AuthenticationController authController = new AuthenticationController();
        UserController userController = new UserController();

        authController.register("gideon@gmail.com", "Gideon", "Figglophobia");
        String gideonToken = authController.login("gideon@gmail.com", "Figglophobia");
        try {
            System.out.println("-------trying to change email:");
            if (userController.updateEmail("gideon2@gmail.com", gideonToken)) {
                System.out.println("change email successful!");
            }

            try {
                logger.warn("trying to login with previous email when no mail");
                System.out.println("-------trying to login with previous email:");
                authController.login("gideon@gmail.com", "Figglophobia");
            } catch (IllegalArgumentException e) {
                logger.fatal(e);
                System.out.println(e + " - test successful!");
            }

            System.out.println("-------trying to change name:");
            if (userController.updateName("Gideoniii", gideonToken)) {
                System.out.println("change name successful!");
            }
            System.out.println("-------trying to change password with invalid token:");
            try {
                userController.updatePassword("Dvir2213", "ooblah");
                System.out.println("change password successful!");
            } catch (InvalidParameterException e) {
                logger.fatal(e);
                System.out.println(e + " - test successful!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}