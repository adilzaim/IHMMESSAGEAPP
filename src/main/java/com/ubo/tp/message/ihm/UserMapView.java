package main.java.com.ubo.tp.message.ihm;

import main.java.com.ubo.tp.message.ihm.listener.UserMapViewListener;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class UserMapView extends JPanel {
    // Map to store user information
    private Map<String, Object> userMap;

    // Listener instance
    private UserMapViewListener listener;

    /**
     * Constructor for UserMapView
     * @param userMap Map containing user information
     */
    public UserMapView(Map<String, Object> userMap) {
        this.userMap = userMap;

        // Set up the layout
        setLayout(new BorderLayout());

        // Create main panel with user information
        JPanel userInfoPanel = createUserInfoPanel();
        add(userInfoPanel, BorderLayout.CENTER);

        // Create action buttons panel
        JPanel actionPanel = createActionPanel();
        add(actionPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates panel with user information
     * @return JPanel with user details
     */
    private JPanel createUserInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Add user information labels
        for (Map.Entry<String, Object> entry : userMap.entrySet()) {
            JLabel label = new JLabel(entry.getKey() + ": " + entry.getValue());
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(label);
        }

        return panel;
    }

    /**
     * Creates panel with action buttons
     * @return JPanel with user action buttons
     */
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        // Profile View Button
        JButton profileButton = new JButton("Voir Profil");
        profileButton.addActionListener(e -> {
            if (listener != null) {
                listener.onUserProfileView();
            }
        });
        panel.add(profileButton);

        // User List Button
        JButton userListButton = new JButton("Liste Utilisateurs");
        userListButton.addActionListener(e -> {
            if (listener != null) {
                listener.onUserListView();
            }
        });
        panel.add(userListButton);

        // Search Button
        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> {
            if (listener != null) {
                listener.onUserSearch();
            }
        });
        panel.add(searchButton);

        // Logout Button
        JButton logoutButton = new JButton("Déconnexion");
        logoutButton.addActionListener(e -> {
            if (listener != null) {
                listener.onLogout();
            }
        });
        panel.add(logoutButton);

        return panel;
    }

    /**
     * Sets the listener for user interactions
     * @param listener UserMapViewListener to handle user actions
     */
    public void setUserMapViewListener(UserMapViewListener listener) {
        this.listener = listener;
    }
}
