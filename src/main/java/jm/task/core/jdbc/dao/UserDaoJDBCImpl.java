package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final UserDaoJDBCImpl INSTANCE;
    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement prepareStatement = null;

    static {
        INSTANCE = new UserDaoJDBCImpl();
    }


    private static final String CREATE_USERS_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(128),
                last_name VARCHAR(128),
                age TINYINT
            );""";

    private static final String DROP_USERS_TABLE_SQL = """
            DROP TABLE IF EXISTS users;""";

    private static final String SAVE_USER_SQL = """
            INSERT INTO users (name, last_name, age) VALUES (?,?,?);
            """;

    private static final String GET_ALL_SQL = """
            SELECT id,
            name,
            last_name,
            age
            FROM users
            """;
    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM users
            WHERE id =?;
            """;

    private static final String TRUNCATE_TABLE_SQL = """
            TRUNCATE TABLE users;
            """;

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        ddlOperation(CREATE_USERS_TABLE_SQL);
    }

    public void dropUsersTable() {
        ddlOperation(DROP_USERS_TABLE_SQL);
    }

    public void cleanUsersTable() {
        ddlOperation(TRUNCATE_TABLE_SQL);
    }

    public void ddlOperation(String ddlOperation){
        try {
            connection = Util.open();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            statement.execute(ddlOperation);
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new DAOException(ex);
                }
            }
            throw new DAOException(e);
        } finally {
            if (connection!= null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new DAOException(e);
                }
            } if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new DAOException(e);
                }
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            connection = Util.open();
            connection.setAutoCommit(false);
            prepareStatement = connection.prepareStatement(SAVE_USER_SQL, Statement.RETURN_GENERATED_KEYS);
            prepareStatement.setString(1, name);
            prepareStatement.setString(2, lastName);
            prepareStatement.setByte(3, age);
            prepareStatement.executeUpdate();
            ResultSet generatedKeys = prepareStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                System.out.format("User с именем – %s добавлен в базу данных \n", name);
            }
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new DAOException(ex);
                }
            }
            throw new DAOException(e);
        } finally {
            if (connection!= null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new DAOException(e);
                }
            } if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new DAOException(e);
                }
            }
        }
    }

    public void removeUserById(long id) {
        try {
            connection = Util.open();
            connection.setAutoCommit(false);
            prepareStatement = connection.prepareStatement(DELETE_BY_ID_SQL);
            prepareStatement.setLong(1, id);
            prepareStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new DAOException(ex);
                }
            }
            throw new DAOException(e);
        } finally {
            if (connection!= null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new DAOException(e);
                }
            } if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new DAOException(e);
                }
            }
        }
    }

    public List<User> getAllUsers() {
        try {
            connection = Util.open();
            connection.setAutoCommit(false);
            prepareStatement = connection.prepareStatement(GET_ALL_SQL);
            ResultSet resultSet = prepareStatement.executeQuery();
            List<User> Users = new ArrayList<>();
            while (resultSet.next()) {
                Users.add(buildUser(resultSet));
            }
            connection.commit();
            return Users;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new DAOException(ex);
                }
            }
            throw new DAOException(e);
        } finally {
            if (connection!= null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new DAOException(e);
                }
            } if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new DAOException(e);
                }
            }
        }
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        User user = new User(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("last_name"),
                resultSet.getByte("age")
        );
        return user;
    }



    public static UserDaoJDBCImpl getInstance() {
        return INSTANCE;
    }
}
