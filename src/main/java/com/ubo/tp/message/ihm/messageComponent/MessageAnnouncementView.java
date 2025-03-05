package main.java.com.ubo.tp.message.ihm.messageComponent;

import main.java.com.ubo.tp.message.datamodel.Message;
import main.java.com.ubo.tp.message.datamodel.User;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageAnnouncementView extends JPanel {
    private JTextPane messagePane;
    private DefaultStyledDocument document;
    private StyleContext styleContext;
    private Style baseStyle, tagStyle, timestampStyle, messageStyle;

    private List<Message> messageList;

    public MessageAnnouncementView(List<Message> messageList) {
        this.messageList = messageList;
        setLayout(new BorderLayout());

        // Initialiser le style
        styleContext = StyleContext.getDefaultStyleContext();

        // Créer les styles
        createStyles();

        // Créer le document stylé
        document = new DefaultStyledDocument();

        // Créer le TextPane
        messagePane = new JTextPane(document);
        messagePane.setEditable(false);

        // Ajouter une barre de défilement
        JScrollPane scrollPane = new JScrollPane(messagePane);
        add(scrollPane, BorderLayout.CENTER);

        // Afficher les messages
        displayMessages();
    }

    // Fonction pour réinitialiser la liste des messages et mettre à jour l'affichage
    public void unsetMessageList(List<Message> newMessageList) {
        this.messageList = newMessageList;
        repaintMessages();
    }

    // Fonction pour réafficher les messages avec la nouvelle liste
    private void repaintMessages() {
        clearMessages(); // Efface les anciens messages
        displayMessages(); // Affiche les nouveaux messages
    }

    private void createStyles() {
        // Style de base
        baseStyle = styleContext.addStyle("base", null);
        StyleConstants.setFontFamily(baseStyle, "Arial");
        StyleConstants.setFontSize(baseStyle, 14);

        // Style pour les tags
        tagStyle = styleContext.addStyle("tag", baseStyle);
        StyleConstants.setForeground(tagStyle, new Color(33, 150, 243)); // Bleu Material Design
        StyleConstants.setBold(tagStyle, true);

        // Style pour les timestamps
        timestampStyle = styleContext.addStyle("timestamp", baseStyle);
        StyleConstants.setForeground(timestampStyle, Color.GRAY);
        StyleConstants.setItalic(timestampStyle, true);

        // Style pour les messages
        messageStyle = styleContext.addStyle("message", baseStyle);
        StyleConstants.setForeground(messageStyle, Color.BLACK);
    }

    private void displayMessages() {
        try {
            // Pour chaque message dans la liste, formater et afficher
            for (Message msg : messageList) {
                // Convertir le long en Date
                Date date = new Date(msg.getEmissionDate());

                // Formatter la date
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");  // Correction du format
                String timestamp = sdf.format(date);

                // Ajouter le timestamp
                document.insertString(document.getLength(),
                        timestamp + " ",
                        timestampStyle
                );

                // Ajouter le tag de l'utilisateur
                User sender = msg.getSender();
                document.insertString(document.getLength(),
                        "@" + sender.getUserTag() + " ",
                        tagStyle
                );

                // Ajouter le message
                document.insertString(document.getLength(),
                        msg.getText() + "\n",
                        messageStyle
                );
            }

            // Faire défiler jusqu'au dernier message
            messagePane.setCaretPosition(document.getLength());

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }


    // Fonction pour effacer tous les messages actuellement affichés
    private void clearMessages() {
        try {
            document.remove(0, document.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
