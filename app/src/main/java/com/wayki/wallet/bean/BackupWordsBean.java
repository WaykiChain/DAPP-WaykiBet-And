package com.wayki.wallet.bean;

public class BackupWordsBean {
    private String words;
    private boolean isSelected;

    public BackupWordsBean(String words, boolean isSelected) {
        this.words = words;
        this.isSelected = isSelected;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
