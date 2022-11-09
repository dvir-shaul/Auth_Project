package AuthorizationFlow.Controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthenticationController {
     AuthenticationService authService;
    private static Logger logger = LogManager.getLogger(AuthenticationController.class.getName());

    public AuthenticationController() {
        this.authService = AuthenticationService.getInstance();
    }

    public String login(String email, String password) {
        logger.info("Currently in AuthenticationController - login");

        Utils.checkEmail(email);
        Utils.checkPassword(password);
        return authService.login(email, password);
    }

    public void register(String email, String name, String password) {
        logger.info("Currently in AuthenticationController - Register with fileName func");

        Utils.checkEmail(email);
        Utils.checkName(name);
        Utils.checkPassword(password);
        authService.register(email, name, password);
    }
}

