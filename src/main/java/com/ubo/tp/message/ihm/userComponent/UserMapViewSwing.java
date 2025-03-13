package com.ubo.tp.message.ihm.userComponent;

import com.ubo.tp.message.ihm.searchUser.SearchUserViewSwing;
import com.ubo.tp.message.datamodel.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class UserMapViewSwing extends JPanel {


    // Map to store user information
    private User user;

    // Listener instance
    private UserMapViewListener listener;
    private SearchUserViewSwing searchUserView;
    /**
     * Constructor for UserMapView
     * @param user  containing user information
     */
    public UserMapViewSwing(User user , SearchUserViewSwing searchUserView) {
        this.user = user;
        this.searchUserView = searchUserView;
        // Set up the layout
        setLayout(new BorderLayout());

        // Create main panel with user information
        JPanel userInfoPanel = createUserInfoPanel(user);
        add(userInfoPanel, BorderLayout.CENTER);

        // Create action buttons panel
        JPanel actionPanel = createActionPanel();
        add(actionPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates panel with user information
     * @return JPanel with user details
     */
    private JPanel createUserInfoPanel(User user) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Avatar
        if (user.getAvatarPath() != null && !user.getAvatarPath().isEmpty()) {
            ImageIcon icon = new ImageIcon(getCircularImage(user.getAvatarPath(), 80)); // Taille raisonnable (80px)
            JLabel avatarLabel = new JLabel(icon);
            avatarLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(avatarLabel);
        }

        // Informations utilisateur
        panel.add(createInfoLabel("UUID", user.getUuid().toString()));
        panel.add(createInfoLabel("Tag", user.getUserTag()));
        panel.add(createInfoLabel("Nom", user.getName()));
        panel.add(createInfoLabel("Mots de passe", user.getUserPassword()));
        panel.add(createInfoLabel("Tags suivis", String.join(", ", user.getFollows())));

        return panel;
    }

    private JLabel createInfoLabel(String key, String value) {
        JLabel label = new JLabel(key + ": " + value);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private Image getCircularImage(String imagePath, int size) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            Image resizedImage = originalImage.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            BufferedImage circleBuffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2 = circleBuffer.createGraphics();
            g2.setClip(new Ellipse2D.Float(0, 0, size, size));
            g2.drawImage(resizedImage, 0, 0, null);
            g2.dispose();

            return circleBuffer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
               // listener.onUserSearch(searchUserViewSwing);
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

    public void setListener(UserMapViewListener listener) {
        this.listener = listener;
    }
}
