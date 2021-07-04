package com.ERPG_Dungeons.Dungeons;

import com.ERPG_Dungeons.DiscordConstants;
import com.ERPG_Dungeons.ERPG_Dungeons;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Dungeons extends ListenerAdapter {

    List<Integer> columns = new ArrayList<>();
    List<List<Integer>> rows = new ArrayList<>();

    List<List<Integer>> memberBoard = new ArrayList<>();

    Map<Integer, String> corBlock = new HashMap<>();


    int life;
    int maxLife;
    long dragonHealth;
    long dragonMaxHealth;
    long swordAt;

    long tempMessageId;
    EmbedBuilder tempEmbed;
    long channelId;
    String authorName;
    String reactionEmote;
    long memberIdInGame;
    int memberTimer;
    Point corPoint;

    boolean isAvailable = true;
    String effect;
    String currentBlock;
    int currentBlockNumber;

    public Dungeons() {

    }

    public void initializeDungeon(int lifeNum, long dragonHealthNum, long at) {
        life = lifeNum;
        maxLife = lifeNum;

        dragonHealth = dragonHealthNum;
        dragonMaxHealth = dragonHealthNum;

        swordAt = at;

        rows.clear();
        columns.clear();
        memberBoard.clear();

        isAvailable = false;
    }

    public int getMemberTimer() {
        return memberTimer;
    }

    public void setMemberTimer(int number) {
        memberTimer = number;
    }

    public void leaveGame() {
        columns.clear();
        rows.clear();
        memberBoard.clear();
        memberIdInGame = 0L;
        tempMessageId = 0L;

        isAvailable = true;
    }

    public boolean move(String direction, int steps, int upLimit, int lowLimit) {
        boolean temp = false;
        int tempX = (int)corPoint.getX();
        int tempY = (int)corPoint.getY();
        if (direction.equals("x")) {
            if (tempX + steps < lowLimit || tempX + steps > upLimit) {
                temp = false;
            } else {
                corPoint = new Point(tempX + steps, tempY);
                System.out.println(corPoint);
                temp = true;
            }
        } else if (direction.equals("y")) {
            if (tempY + steps < lowLimit || tempY + steps > upLimit) {
                temp = false;
            } else {
                corPoint = new Point(tempX, tempY + steps);
                System.out.println(corPoint);
                temp = true;
            }
        }
        return temp;

    }

    public void changeLife(int num) {
        life += num;
    }

    public boolean detectLife() {
        boolean temp = false;
        if (life > 0) {
            temp = true;
        }
        return temp;
    }

    public void editEmbed(long messageId, Event event, EmbedBuilder embed, long channelId) {
        event.getJDA().getTextChannelById(channelId).editMessageById(messageId, embed.build()).queue();
    }

    public void inactiveEmbed(long messageId, EmbedBuilder embed, long channelId) {
        ERPG_Dungeons.jda.getTextChannelById(channelId).editMessageById(messageId, embed.setColor(0x2e2e2e).setFooter("Inactive").build()).queue();
    }
}
