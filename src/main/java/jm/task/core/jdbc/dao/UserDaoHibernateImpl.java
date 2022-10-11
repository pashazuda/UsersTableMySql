package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private static final UserDaoHibernateImpl INSTANCE;


    static {
        INSTANCE = new UserDaoHibernateImpl();
    }


    private static final String CREATE_USERS_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(128),
                last_name VARCHAR(128),
                age TINYINT
            )""";

    private static final String DROP_USERS_TABLE_SQL = """
            DROP TABLE IF EXISTS users;""";

    private static final String CLEAN_USERS = """
            DELETE FROM User""";
    public UserDaoHibernateImpl() {

    }

    public void ddlOperation(String ddlOperation){
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createSQLQuery(ddlOperation).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void createUsersTable() {
        ddlOperation(CREATE_USERS_TABLE_SQL);
    }

    @Override
    public void dropUsersTable() {
        ddlOperation(DROP_USERS_TABLE_SQL);
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            System.out.format("User с именем – %s добавлен в базу данных \n", name);
            session.getTransaction().commit();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            List<User> users = session.createCriteria(User.class).list();
            session.getTransaction().commit();
            return users;
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createQuery(CLEAN_USERS).executeUpdate();
            session.getTransaction().commit();
        }
    }

    public static UserDaoHibernateImpl getInstance() {
        return INSTANCE;
    }
}
