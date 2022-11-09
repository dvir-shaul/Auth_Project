package AuthorizationFlow.Controllers;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

class UserRepository {
    private static volatile UserRepository userRepo;
    Map<String, User> usersCache;
    private static Logger logger = LogManager.getLogger(UserRepository.class.getName());

    static UserRepository getInstance() {
        logger.info("Currently in UserRepository - get instance");
        UserRepository result = userRepo;

        if (result == null) {
            synchronized (UserRepository.class) {
                result = userRepo;
                if (result == null) {
                    userRepo = result = new UserRepository();
                }
            }
        }
        return result;
    }

    private UserRepository() {
        logger.info("Currently in UserRepository - Constructor func");
        usersCache = new HashMap<>();
        loadAllUsersToCache(new File("src\\main\\java\\AuthorizationFlow\\Controllers\\users"));
    }

    User readFromCache(String email){
        logger.info("Currently in UserRepository - readFromCache func");
        return usersCache.get(email);
    }

    private void loadAllUsersToCache(File folder) {
        logger.info("Currently in UserRepository - loadAllUsersToCache func");
        for (File fileEntry: folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                if (Utils.isJsonFile(fileEntry)) {
                    User user = readFromFile(fileEntry.getAbsolutePath());
                    logger.debug("Currently in UserRepository - loadAllUsersToCache func");
                    Debugger.log(user);
                    usersCache.put(user.getEmail(),user);
                }
            }
        }
    }

    void deleteFile(String path) {
        logger.info("Currently in UserRepository - deleteFile by path func");
        File file = new File("src\\main\\java\\AuthorizationFlow\\Controllers\\users\\" + path);
        boolean b= file.delete();
    }

    void deleteFile(User user) {
        logger.info("Currently in UserRepository - deleteFile by user func");
        if (!usersCache.containsKey(user.getEmail())) {
            logger.warn("read from file");
            throw new IllegalArgumentException("cant remove user that doesnt exist");
        }
        usersCache.remove(user.getEmail());
        deleteFile(user.getEmail() + ".json");
    }

    void writeToFile(String fileName, User user) throws IOException {
        logger.info("Currently in UserRepository - writeToFile with fileName func");

        Gson gson = new Gson();
        try (FileWriter fw = new FileWriter("src\\main\\java\\AuthorizationFlow\\Controllers\\users\\" + fileName)) {
            logger.warn("write to file");
            String userJson = gson.toJson(user);
            fw.write(userJson);
            usersCache.put(user.getEmail(), user);
        } catch (IOException e) {
            throw new IOException("cant write to new file to update");
        }
    }

    private User readFromFile(String fileName) {

        User readUser = null;
        try (FileReader fr = new FileReader(fileName)) {
            logger.warn("read from file");
            Gson gson = new Gson();
            readUser = gson.fromJson(fr, User.class);
            usersCache.put(readUser.getEmail(), readUser);
        } catch(FileNotFoundException e) {
            throw new RuntimeException("file not found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return readUser;
    }
}
