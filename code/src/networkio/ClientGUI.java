package networkio;

import gui.LoginPage;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Team Project Phase 3 -- ClientGUI
 * GUI based client
 *
 * @author Raghav Gangatirkar, Atman Singh, Aditya Pachpande, CS 180
 *
 * @version December 08, 2024
 *
 */

public class ClientGUI {
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;

    public static void main(String[] args) {
        try {Socket socket = new Socket("localhost", 4201);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            SwingUtilities.invokeLater(() -> new LoginPage(oos, ois));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to connect to server.");
        }
    }
}