package com.ubo.tp.message.ihm.messageComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import com.ubo.tp.message.datamodel.User;

public class MessagePanel extends JPanel implements MessageModelObserver {
    private JTextArea messageArea;
    private JTextField inputField;
    private JButton publishButton;
    private User currentUser;
    private MessageListener messageListener;
    private MessageModel model;
    private JScrollPane scrollPane;

    public MessagePanel(User currentUser, MessageModel model) {
        this.model = model;
        this.model.addObserver(this);
        this.currentUser = currentUser;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500, 300)); // Augmenté pour mieux voir les messages

        // Zone d'affichage des messages avec bordure rouge
        messageArea = new JTextArea(10, 30);
        messageArea.setEditable(false);
        // Assurer une bonne visibilité du texte
        messageArea.setBackground(Color.WHITE);
        messageArea.setForeground(Color.GRAY);
        messageArea.setFont(new Font("SansSerif", Font.ITALIC, 14));


        scrollPane = new JScrollPane(messageArea);
        // Assurons-nous que le JScrollPane est visible
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(480, 200));
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

        // Assurons-nous que le composant est visible
        setVisible(true);

        // Initialisation des messages au démarrage
        initializeMessages();
    }

    // Méthode pour enregistrer un listener
    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }

    /**
     * Fonction d'initialisation qui charge les messages existants lors de la création du composant
     * Cette méthode est appelée une seule fois dans le constructeur
     */
    private void initializeMessages() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Récupérer la liste initiale des messages depuis le modèle
                List<String> initialMessages = model.getCurrentList();

                // Vider la zone de texte
                messageArea.setText("");

                System.out.println("Initialisation des messages - Nombre: " +
                        (initialMessages != null ? initialMessages.size() : 0));

                // Afficher chaque message dans la zone de texte
                if (initialMessages != null && !initialMessages.isEmpty()) {
                    for (String message : initialMessages) {
                        System.out.println("Initialisation - Ajout du message: " + message);
                        messageArea.append(message + "\n");
                    }

                    // Scroller vers le bas pour voir les derniers messages
                    messageArea.setCaretPosition(messageArea.getDocument().getLength());
                }

                // Forcer le rafraîchissement de l'interface
                messageArea.revalidate();
                messageArea.repaint();
                scrollPane.revalidate();
                scrollPane.repaint();
                revalidate();
                repaint();
            }
        });
    }

    @Override
    public void onMessageListUpdated(List<String> updatedList) {
        // Utiliser invokeLater pour garantir que les mises à jour de l'IHM se font sur l'EDT
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Effacer d'abord le contenu actuel
                messageArea.setText("");

                // Ajouter chaque message comme une nouvelle ligne
                for (String message : updatedList) {
                    System.out.println("Ajout du message: " + message); // Debug
                    messageArea.append(message + "\n");
                }

                // Scroller vers le bas pour voir les derniers messages
                messageArea.setCaretPosition(messageArea.getDocument().getLength());

                // Forcer le rafraîchissement de l'interface
                messageArea.revalidate();
                messageArea.repaint();
                scrollPane.revalidate();
                scrollPane.repaint();
            }
        });
    }
}