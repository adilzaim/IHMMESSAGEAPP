package main.java.com.ubo.tp.message.ihm.messageComponent;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import main.java.com.ubo.tp.message.datamodel.User;

public class MessagePanel extends JPanel  {
    private JTextArea messageArea;
    private JTextField inputField;
    private JButton publishButton;
    private User currentUser;
    private MessageListener messageListener;

    public MessagePanel(User currentUser) {
        this.currentUser = currentUser;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 150)); // Taille pour être en bas à gauche

        // Zone d'affichage des messages
        messageArea = new JTextArea(5, 20);
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        add(scrollPane, BorderLayout.CENTER);

        // Zone de saisie + bouton publier
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        publishButton = new JButton("Publier");

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(publishButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // Action du bouton Publier
        publishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = inputField.getText().trim();
                if (!text.isEmpty() && messageListener != null) {
                    messageListener.onMessageSend(currentUser.getUserTag(), text);
                    inputField.setText("");
                }
            }
        });
    }

    // Méthode pour enregistrer un listener
    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }

}
