package com.tama.chat.utils.listeners;

public interface SearchListener {

    void prepareSearch();

    void search(String searchQuery);

    void cancelSearch();
}