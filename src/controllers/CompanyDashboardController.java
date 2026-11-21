package controllers;

import models.User;

public class CompanyDashboardController {

    private User user;

    @javafx.fxml.FXML
    private void logout() { app.Main.logout(); }

    public void setUser(User user) {
        this.user = user;
    }
}
