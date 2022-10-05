package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();

        userService.saveUser("Oleg","Ivanov", (byte) 14);
        userService.saveUser("Ramis","Davidov", (byte) 98);
        userService.saveUser("Kirill","Smirnov", (byte) 34);
        userService.saveUser("Artur","King", (byte) 3);


        for (User user : userService.getAllUsers()) {
            System.out.println(user.toString());
        }

        userService.cleanUsersTable();

        userService.dropUsersTable();

    }
}
