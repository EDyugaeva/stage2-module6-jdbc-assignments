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

    public Long createUser(User user) throws SQLException {
        Long result = null;

        Connection connection = CustomDataSource.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(createUserSQL);

        preparedStatement.setString(1, user.getFirstName());
        preparedStatement.setString(2, user.getLastName());
        preparedStatement.setInt(3, user.getAge());

        preparedStatement.executeUpdate();
        ResultSet rs = preparedStatement.getGeneratedKeys();
        if (rs.next()) {
            result = rs.getLong(1);
            System.out.println(result);
        }
        return result;

    }

    public User findUserById(Long userId) throws SQLException {
        Connection connection = CustomDataSource.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(findUserByIdSQL);
        preparedStatement.setLong(1, userId);
        ResultSet rs = preparedStatement.executeQuery();
        String firstName = rs.getString("firstname");
        String lastName = rs.getString("lsatname");
        int age = rs.getInt("age");

        return new User(userId, firstName, lastName, age);
    }

    public User findUserByName(String userName) throws SQLException {
        Connection connection = CustomDataSource.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(findUserByNameSQL);
        preparedStatement.setString(1, userName);
        ResultSet rs = preparedStatement.executeQuery();
        long userId = Long.parseLong(rs.getString("id"));
        String lastName = rs.getString("lsatname");
        int age = rs.getInt("age");

        return new User(userId, userName, lastName, age);


    }

    public List<User> findAllUser() throws SQLException {
        Connection connection = CustomDataSource.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(findAllUserSQL);
        ResultSet rs = preparedStatement.executeQuery();
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            Long id = Long.parseLong(rs.getString("id"));
            String firstName = rs.getString("firstname");
            String lastName = rs.getString("lastname");
            int age = rs.getInt("age");
            users.add(new User(id, firstName, lastName, age));
        }
        return users;
    }

    public User updateUser(User user) throws SQLException {
        Connection connection = CustomDataSource.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(updateUserSQL);

        preparedStatement.setString(1, user.getFirstName());
        preparedStatement.setString(2, user.getLastName());
        preparedStatement.setInt(3, user.getAge());
        preparedStatement.setLong(4, user.getId());

        return user;


    }

    public void deleteUser(Long userId) throws SQLException {
        Connection connection = CustomDataSource.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(deleteUser);

    }
}
