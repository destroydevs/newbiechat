package ru.spigotmc.destroy.newbiechat.database;

public class Data {
    int time;
    boolean block;

    public Data(int time, boolean block) {
        this.time = time;
        this.block = block;
    }

    public int getTime() {
        return time;
    }

    public boolean isBlocked() {
        return block;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setBlocked(boolean block) {
        this.block = block;
    }
}
