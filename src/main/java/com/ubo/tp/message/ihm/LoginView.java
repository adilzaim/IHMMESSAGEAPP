package main.java.com.ubo.tp.message.ihm;

import main.java.com.ubo.tp.message.ihm.listener.LoginListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
public class LoginView extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton createAccountButton;
    private Map<String, String> userCredentials;
    private LoginListener loginListener;



    public LoginView() {
        // Initialize user credentials storage
        userCredentials = new HashMap<>();

        // Set up the layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Nom d'utilisateur:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(20);
        add(usernameField, gbc);

        // Password label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Tag de l'utilisateur:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        add(passwordField, gbc);

        // Login button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginButton = new JButton("Connexion");
        add(loginButton, gbc);

        // Create Account button
        gbc.gridy = 3;
        createAccountButton = new JButton("Créer un compte");
        add(createAccountButton, gbc);

        // Add action listeners
        setupListeners();
    }

    private void setupListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String tag = new String(passwordField.getPassword());

                if (loginListener != null) {
                    loginListener.loginVerify(username, tag);
                }


            }
        });

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCreateAccountDialog();
            }
        });
    }

    private void showCreateAccountDialog() {
        JTextField newUsernameField = new JTextField(20);
        JPasswordField newPasswordField = new JPasswordField(20);
        JPasswordField confirmPasswordField = new JPasswordField(20);
        JTextField tag = new JTextField(20);
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Nom d'utilisateur:"));
        panel.add(newUsernameField);
        panel.add(new JLabel("Mot de passe:"));
        panel.add(newPasswordField);
        panel.add(new JLabel("Confirmer le mot de passe:"));
        panel.add(confirmPasswordField);
        panel.add(new JLabel("Tag:"));
        panel.add(tag);


        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Créer un compte",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            String newUsername = newUsernameField.getText();
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String tagString = new String(tag.getText());

            // Validate input
            if (newUsername.isEmpty() || newPassword.isEmpty() || tagString.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "nom utilisateur , mot de passe et tag sont obligatoires",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this,
                        "Les mots de passe ne correspondent pas",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }


            // Add new user
           this.loginListener.createUser(newUsername , newUsername ,newUsername, newPassword);
        }
    }

    private boolean authenticateUser(String username, String password) {
        // Simple authentication - in a real app, you'd use more secure methods
        return userCredentials.containsKey(username) &&
                userCredentials.get(username).equals(password);
    }

    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }
    //pop up

    public static void showPopup(String message, boolean isNormal) {
        if (isNormal) {
            JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, message, "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
