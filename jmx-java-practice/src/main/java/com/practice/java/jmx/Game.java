package com.practice.java.jmx;

/**
 * @author Luo Bao Ding
 * @since 2019/1/30
 */
public class Game implements GameMBean {
    private String playerName;

    private String command = "initial";

    public Game(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void play(String game) {
        System.out.println(this.playerName + " play game: " + game);

    }

    @Override
    public String getPlayerName() {
        System.out.println("Game.getPlayerName: " + this.playerName);
        return this.playerName;
    }

    @Override
    public void setPlayerName(String playerName) {
        System.out.println("Game.setPlayerName: " + playerName);
        this.playerName = playerName;

    }

    @Override
    public String getCommand() {
        System.out.println("Game.getCommand: " + this.command);
        return this.command;
    }

    @Override
    public void setCommand(String command) {
        System.out.println("command = [" + command + "]");
        this.command = command;

    }
}
