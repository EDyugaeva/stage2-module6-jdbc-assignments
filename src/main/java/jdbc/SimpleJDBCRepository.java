package jdbc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SimpleJDBCRepository {

    private static final String createUserSQL = "INSERT INTO  myusers (firstname, lastname, age) VALUES (?, ?, ?)";
    private static final String updateUserSQL = "UPDATE myusers set firstname = ?, lastname = ?, age = ?, where ID = ?";
    private static final String deleteUser = "DELETE from myusers where id = ?";
    private static final String findUserByIdSQL = "SELECT * from myusers where ID = ?";
    private static final String findUserByNameSQL = "SELECT * from myusers where firstname = ?";
    private static final String findAllUserSQL = "SELECT * from myusers";

    public Long createUser(User user) {
        Long result = null;

        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(createUserSQL, Statement.RETURN_GENERATED_KEYS);) {

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setInt(3, user.getAge());

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                result = rs.getLong(1);
                System.out.println(result);
            }

        }
        catch (SQLException e) {
            throwSQLException(e);

        }
        return result;

    }

    public User findUserById(Long userId)  {
        User user = new User();
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findUserByIdSQL);) {
            preparedStatement.setLong(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            int age = 0;
            String lastName = null;
            String firstName = null;
            if (rs.next()) {
                age = rs.getInt("age");
                 firstName = rs.getString("firstname");
                 lastName = rs.getString("lastname");
            }

            user.setFirstName(firstName);
            user.setAge(age);
            user.setId(userId);
            user.setLastName(lastName);
        } catch (SQLException e) {
            throwSQLException(e);
        }
        return user;
    }

    public User findUserByName(String userName)  {
        User user = new User();
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findUserByNameSQL);) {
            preparedStatement.setString(1, userName);
            ResultSet rs = preparedStatement.executeQuery();

            int age = 0;
            String lastName = null;
            String firstName = null;
            long userId = 0;
            if (rs.next()) {
                userId = Long.parseLong(rs.getString("id"));
                age = rs.getInt("age");
                lastName = rs.getString("lastname");
            }
            user.setFirstName(userName);
            user.setAge(age);
            user.setId(userId);
            user.setLastName(lastName);
        }
        catch (SQLException e) {
            throwSQLException(e);
        }

        return user;


    }

    public List<User> findAllUser()   {
        List<User> users = new ArrayList<>();
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAllUserSQL);) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Long id = Long.parseLong(rs.getString("id"));
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                int age = rs.getInt("age");
                users.add(new User(id, firstName, lastName, age));
            }
        }
        catch (SQLException e) {
            throwSQLException(e);
        }
        return users;
    }

    public User updateUser(User user)  {
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateUserSQL);) {
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setInt(3, user.getAge());
            preparedStatement.setLong(4, user.getId());
        }
        catch (SQLException e) {
            throwSQLException(e);
        }

        return user;


    }

    public void deleteUser(Long userId)  {
        try (Connection connection = CustomDataSource.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(deleteUser);) {
            preparedStatement.setLong(1, userId);
            if (preparedStatement.executeUpdate() == 0) throw new SQLException("No such user exists");
        }
        catch (SQLException e) {
            throwSQLException(e);
        }

    }

    public void throwSQLException(Exception e) {
        String message = String.format("%s: %s", e.getClass().getName(), e.getMessage());
        if (e.getCause() != null) {
            message += String.format("\nCause: %s: %s", e.getCause().getClass().getName(), e.getCause().getMessage());
        }
        throw new RuntimeException(message);
    }
}
