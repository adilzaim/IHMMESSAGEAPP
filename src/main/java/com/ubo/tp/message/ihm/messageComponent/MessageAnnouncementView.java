package main.java.com.ubo.tp.message.ihm.messageComponent;

import main.java.com.ubo.tp.message.datamodel.Message;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageAnnouncementView extends JPanel {
    private JTextPane messagePane;
    private DefaultStyledDocument document;
    private StyleContext styleContext;
    private Style baseStyle, tagStyle, timestampStyle, messageStyle;

    private List<Message> originalMessageList;
    private List<Message> filteredMessageList;

    private JTextField searchField;
    private JButton searchButton, resetButton;

    private FilterListener filterListener;

    public MessageAnnouncementView(List<Message> messageList) {
        this.originalMessageList = messageList;
        this.filteredMessageList = new ArrayList<>(messageList);

        setLayout(new BorderLayout());
        styleContext = StyleContext.getDefaultStyleContext();
        createStyles();
        document = new DefaultStyledDocument();

        messagePane = new JTextPane(document);
        messagePane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(messagePane);
        add(scrollPane, BorderLayout.CENTER);

        addSearchBar();
        displayMessages();
    }

    private void addSearchBar() {
        searchField = new JTextField(20);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(33, 150, 243), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        searchButton = new JButton("Rechercher");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (filterListener != null) {
                    filterListener.onFilter(searchField.getText().trim());
                }
            }
        });

        resetButton = new JButton("Réinitialiser");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchField.setText(""); // Efface la barre de recherche
                if(filterListener != null) {
                    filterListener.onFilterRemove();
                }
            }
        });

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        searchPanel.setBackground(Color.WHITE);

        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchContainer.add(searchField);
        searchContainer.add(searchButton);
        searchContainer.add(resetButton);
        searchContainer.setBackground(Color.WHITE);

        searchPanel.add(searchContainer, BorderLayout.WEST);
        add(searchPanel, BorderLayout.NORTH);
    }

    public void setFilterListener(FilterListener filterListener) {
        this.filterListener = filterListener;
    }

    public void updateFilteredMessages(List<Message> newFilteredMessages) {
        this.filteredMessageList = newFilteredMessages;
        repaintMessages();
    }

    public void unsetMessageList(List<Message> newMessageList) {
        this.originalMessageList = newMessageList;
        this.filteredMessageList = new ArrayList<>(newMessageList);
        repaintMessages();
    }

    private void repaintMessages() {
        clearMessages();
        displayMessages();
    }

    private void createStyles() {
        baseStyle = styleContext.addStyle("base", null);
        StyleConstants.setFontFamily(baseStyle, "Arial");
        StyleConstants.setFontSize(baseStyle, 14);

        tagStyle = styleContext.addStyle("tag", baseStyle);
        StyleConstants.setForeground(tagStyle, new Color(33, 150, 243));
        StyleConstants.setBold(tagStyle, true);

        timestampStyle = styleContext.addStyle("timestamp", baseStyle);
        StyleConstants.setForeground(timestampStyle, Color.GRAY);
        StyleConstants.setItalic(timestampStyle, true);

        messageStyle = styleContext.addStyle("message", baseStyle);
        StyleConstants.setForeground(messageStyle, Color.BLACK);
    }

    private void displayMessages() {
        try {
            for (Message msg : filteredMessageList) {
                Date date = new Date(msg.getEmissionDate());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String timestamp = sdf.format(date);

                document.insertString(document.getLength(), timestamp + " ", timestampStyle);
                document.insertString(document.getLength(), "@" + msg.getSender().getUserTag() + " ", tagStyle);
                document.insertString(document.getLength(), msg.getText() + "\n", messageStyle);
            }
            messagePane.setCaretPosition(document.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void clearMessages() {
        try {
            document.remove(0, document.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
