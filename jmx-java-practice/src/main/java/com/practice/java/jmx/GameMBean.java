package com.practice.java.jmx;

/**
 * @author Luo Bao Ding
 * @since 2019/1/30
 */
public interface GameMBean {


    void play(String game);

    String getPlayerName();

    void setPlayerName(String playerName);

    String getCommand();

    void setCommand(String command);
}
