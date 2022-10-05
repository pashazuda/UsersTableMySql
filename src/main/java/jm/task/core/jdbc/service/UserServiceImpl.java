package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;

import java.util.List;

public class UserServiceImpl implements UserService {
    public void createUsersTable() {
        UserDaoJDBCImpl.getInstance().createUsersTable();
    }

    public void dropUsersTable() {
        UserDaoJDBCImpl.getInstance().dropUsersTable();
    }

    public void saveUser(String name, String lastName, byte age) {
        UserDaoJDBCImpl.getInstance().saveUser(name, lastName, age);
    }

    public void removeUserById(long id) {
        UserDaoJDBCImpl.getInstance().removeUserById(id);
    }

    public List<User> getAllUsers() {
        return UserDaoJDBCImpl.getInstance().getAllUsers();
    }

    public void cleanUsersTable() {
        UserDaoJDBCImpl.getInstance().cleanUsersTable();
    }
}
