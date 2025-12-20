package com.game.backend.model;

public class Settings {

    private int masterVolume;
    private int musicVolume;
    private int sfxVolume;

    public Settings() {
        this.masterVolume = 100;
        this.musicVolume = 80;
        this.sfxVolume = 80;
    }

    public int getMasterVolume() {
        return masterVolume;
    }

    public void setMasterVolume(int masterVolume) {
        this.masterVolume = masterVolume;
    }

    public int getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(int musicVolume) {
        this.musicVolume = musicVolume;
    }

    public int getSfxVolume() {
        return sfxVolume;
    }

    public void setSfxVolume(int sfxVolume) {
        this.sfxVolume = sfxVolume;
    }
}
