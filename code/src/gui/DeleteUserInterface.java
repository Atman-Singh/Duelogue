package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Team Project Phase 3 -- DeleteUserInterface
 * This interface outlines the methods that should be implemented for a DeleteUserPage object
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 18000
 *
 * @version December 08, 2024
 *
 */

public interface DeleteUserInterface {
    // Getter Methods
    JTextField getUsernameField();
    JPasswordField getPasswordField();
    JLabel getStatusLabel();

    // General Methods
    void deleteUser();
}
