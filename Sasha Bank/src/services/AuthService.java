package services;

import dao.UserDAO;
import models.User;

public class AuthService {

    public static User login(String username, String password) {
        UserDAO dao = new UserDAO();
        return dao.findUser(username, password);
    }
}
