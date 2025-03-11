package com.ubo.tp.message.ihm.searchUser;
import main.java.com.ubo.tp.message.datamodel.User;
/**
 * Interface définissant les méthodes de callback pour les événements de recherche.
 * Cette interface sert de pont entre la vue et le contrôleur.
 */
public interface SearchListener {
    /**
     * Appelé lorsqu'une recherche est lancée par l'utilisateur.
     *
     * @param searchTerm Le terme de recherche saisi par l'utilisateur
     */
    void onSearch(String searchTerm);

    void reset();

    void backTo();
}
