package jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws SQLException {
        Logger logger = Logger.getLogger(Main.class.getName());
        SimpleJDBCRepository repo = new SimpleJDBCRepository();

        // Create a new user
        User user = new User(0l,"John", "Doe", 23);
        Long userId = repo.createUser(user);
        logger.log(Level.INFO,"Created new user with ID: {userId}");

        // Find the user by ID
        User foundUser = repo.findUserById(1L);
        logger.log(Level.INFO,"Found user by ID: {foundUser}");

        // Find the user by name
        User users = repo.findUserByName("John");
        logger.log(Level.INFO,"Found {users.size()} users with name 'John Doe': {users}");

        // Find all users
        List<User> allUsers = repo.findAllUser();
        logger.log(Level.INFO,"Found {allUsers.size()} users: {allUsers}");

        // Update the user
        foundUser.setAge(24);
        User updatedUser = repo.updateUser(foundUser);
        logger.log(Level.INFO,"Updated user: {updatedUser}");

        // Delete the user
//        repo.deleteUser(userId);
        logger.log(Level.INFO,"Deleted user with ID: {userId}");
    }
}
