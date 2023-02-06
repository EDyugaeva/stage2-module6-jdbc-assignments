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
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {

    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;

    private static final String createUserSQL = "INSERT INTO  myusers (firstname, lastname, age) VALUES (?, ?, ?)";
    private static final String updateUserSQL = "UPDATE myusers set firstname = ?, lastname = ?, age = ?, where ID = ?";
    private static final String deleteUser = "DELETE from myusers where id = ?";
    private static final String findUserByIdSQL = "SELECT * from myusers where ID = ?";
    private static final String findUserByNameSQL = "SELECT * from myusers where firstname = ?";
    private static final String findAllUserSQL = "SELECT * from myusers";

    public Long createUser(User user) throws SQLException {
        Connection connection = CustomDataSource.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(createUserSQL, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, user.getFirstName());
        ps.setString(2, user.getLastName());
        ps.setInt(3, user.getAge());

        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        return rs.getLong(1);

    }

    public User findUserById(Long userId) throws SQLException {
        Connection connection = CustomDataSource.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(findUserByIdSQL);
        ps.setLong(1, userId);
        ResultSet rs = ps.executeQuery();
        String firstName = rs.getString("firstname");
        String lastName = rs.getString("lsatname");
        int age = rs.getInt("age");

        return new User(userId, firstName, lastName, age);
    }

    public User findUserByName(String userName) throws SQLException {
        Connection connection = CustomDataSource.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(findUserByNameSQL);
        ps.setString(1, userName);
        ResultSet rs = ps.executeQuery();
        long userId = Long.parseLong(rs.getString("id"));
        String lastName = rs.getString("lsatname");
        int age = rs.getInt("age");

        return new User(userId, userName, lastName, age);


    }

    public List<User> findAllUser() throws SQLException {
        Connection connection = CustomDataSource.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(findAllUserSQL);
        ResultSet rs = ps.executeQuery();
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
        PreparedStatement ps = connection.prepareStatement(updateUserSQL);

        ps.setString(1, user.getFirstName());
        ps.setString(2, user.getLastName());
        ps.setInt(3, user.getAge());
        ps.setLong(4, user.getId());

        return user;


    }

    private void deleteUser(Long userId) throws SQLException {
        Connection connection = CustomDataSource.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement(deleteUser);
        ps.execute();

    }
}
