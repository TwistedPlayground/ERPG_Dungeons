package com.ERPG_Dungeons.Dungeons;

import com.ERPG_Dungeons.DiscordConstants;
import com.ERPG_Dungeons.ERPG_Dungeons;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.awt.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Dungeon12 extends Dungeons {

    //0 is ⬛, +1orb, -25hp
    //1 is 🔳, -500hp
    //2 is 🔲, -5orb, +5hp
    //3 is ⬜, do nothing
    //4 is <:ULTRAEDGY_ARMOR:857540908907626556>, armor
    //🔥, fire
    //🔅, orb

    int orbs;
    int maxOrbs = 10;

    public Dungeon12() {
        corBlock.put(0, "⬛");
        corBlock.put(1, "\uD83D\uDD33");
        corBlock.put(2, "\uD83D\uDD32");
        corBlock.put(3, "⬜");
        corBlock.put(4, DiscordConstants.ultraEdgyArmor);
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (!event.getMember().getUser().isBot()) {
            String[] args = event.getMessage().getContentRaw().split(" ");
            if (event.getChannel().getIdLong() == DiscordConstants.dungeon12ChannelId) {
                if (args[0].equalsIgnoreCase(DiscordConstants.prefix + "d12")) {
                    if (isAvailable) {
                        authorName = event.getMember().getUser().getName();
                        memberIdInGame = event.getMember().getIdLong();
                        event.getMessage().delete().queue();
                        if (args.length != 2) {
                            event.getChannel().sendMessage("Usage: " + DiscordConstants.prefix + "d12 " + "[life]").queue((message -> {
                                message.delete().queueAfter(2L, TimeUnit.SECONDS);
                            }));
                        } else {
                            try {
                                if (Integer.parseInt(args[1]) < 10000 && Integer.parseInt(args[1]) > 1) {
                                    initializeDungeon(Integer.parseInt(args[1]), 9999999999999999L, 9999999999999999L);
                                    startGame();
                                    startTimer();

                                    event.getChannel().sendMessage(makeEmbed().build()).queue((message -> {
                                        tempMessageId = message.getIdLong();

                                        message.addReaction(DiscordConstants.up).queue();
                                        message.addReaction(DiscordConstants.left).queue();
                                        message.addReaction(DiscordConstants.right).queue();
                                        message.addReaction(DiscordConstants.down).queue();
                                        message.addReaction(DiscordConstants.attack).queue();
                                        message.addReaction(DiscordConstants.cancel).queue();
                                    }));
                                } else {
                                    event.getChannel().sendMessage("<@!" + event.getMember().getIdLong() + ">, please provide an Integer smaller than 10000 and larger than 1.").queue((message -> {
                                        message.delete().queueAfter(2L, TimeUnit.SECONDS);
                                    }));
                                }
                            } catch (NumberFormatException E) {
                                event.getChannel().sendMessage("<@!" + event.getMember().getIdLong() + ">, please provide an Integer smaller than 10000 and larger than 1.").queue((message -> {
                                    message.delete().queueAfter(2L, TimeUnit.SECONDS);
                                }));
                            }
                        }
                    } else {
                        event.getChannel().sendMessage("<@!" + event.getMember().getIdLong() + ">, someone is player here already! Wait till they've finished!").queue((message -> {
                            message.delete().queueAfter(2L, TimeUnit.SECONDS);
                        }));
                    }
                }
            }
        }
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (!event.getMember().getUser().isBot() && event.getMessageIdLong() == tempMessageId) {
            event.getReaction().removeReaction(event.getJDA().retrieveUserById(memberIdInGame).complete()).complete();
            if (event.getMember().getIdLong() != memberIdInGame) {
                event.getChannel().sendMessage("<@!" + event.getMember().getIdLong() + ">, please don't react other people's dungeon.").queue((message -> {
                    message.delete().queueAfter(2L, TimeUnit.SECONDS);
                }));
            } else {
                reactionEmote = event.getReactionEmote().getEmoji();
                if (reactionEmote.equals(DiscordConstants.up)) {
                    if(!move("y",1,2,0)) {
                        event.getChannel().sendMessage("<@!" + event.getMember().getIdLong() + ">, you are at the top of the board already.").queue((message -> {
                            message.delete().queueAfter(2L,TimeUnit.SECONDS);
                        }));
                    } else {
                        setMemberTimer(30);
                        changeLife(-30);
                        changingTask();
                        if (detectLife()) {
                            editEmbed(tempMessageId,event,editedEmbed(),DiscordConstants.dungeon12ChannelId);
                        } else {
                            editEmbed(tempMessageId,event,deadEmbed(),DiscordConstants.dungeon12ChannelId);
                            leaveGame();
                        }
                    }
                } else if (reactionEmote.equals(DiscordConstants.left)) {
                    if (!move("x",-1,2,0)) {
                        event.getChannel().sendMessage("<@!" + event.getMember().getIdLong() + ">, you are at the leftmost of the board already.").queue((message -> {
                            message.delete().queueAfter(2L,TimeUnit.SECONDS);
                        }));
                    } else {
                        setMemberTimer(30);
                        changeLife(-30);
                        changingTask();
                        if (detectLife()) {
                            editEmbed(tempMessageId,event,editedEmbed(),DiscordConstants.dungeon12ChannelId);
                        } else {
                            editEmbed(tempMessageId,event,deadEmbed(),DiscordConstants.dungeon12ChannelId);
                            leaveGame();
                        }
                    }
                } else if (reactionEmote.equals(DiscordConstants.right)) {
                    if (!move("x",1,2,0)) {
                        event.getChannel().sendMessage("<@!" + event.getMember().getIdLong() + ">, you are at the rightmost of the board already.").queue((message -> {
                            message.delete().queueAfter(2L,TimeUnit.SECONDS);
                        }));
                    } else {
                        setMemberTimer(30);
                        changeLife(-30);
                        changingTask();
                        if (detectLife()) {
                            editEmbed(tempMessageId,event,editedEmbed(),DiscordConstants.dungeon12ChannelId);
                        } else {
                            editEmbed(tempMessageId,event,deadEmbed(),DiscordConstants.dungeon12ChannelId);
                            leaveGame();
                        }
                    }
                } else if (reactionEmote.equals(DiscordConstants.down)) {
                    if (!move("y",-1,2,0)) {
                        event.getChannel().sendMessage("<@!" + event.getMember().getIdLong() + ">, you are at the bottom of the board already.").queue((message -> {
                            message.delete().queueAfter(2L,TimeUnit.SECONDS);
                        }));
                    } else {
                        setMemberTimer(30);
                        changeLife(-30);
                        changingTask();
                        if (detectLife()) {
                            editEmbed(tempMessageId,event,editedEmbed(),DiscordConstants.dungeon12ChannelId);
                        } else {
                            editEmbed(tempMessageId,event,deadEmbed(),DiscordConstants.dungeon12ChannelId);
                            leaveGame();
                        }
                    }
                } else if (reactionEmote.equals(DiscordConstants.attack)) {
                    if (orbs == 10 && currentBlockNumber == 3) {
                        dragonHealth = dragonHealth - swordAt;
                        editEmbed(tempMessageId,event,winEmbed(),DiscordConstants.dungeon12ChannelId);
                        leaveGame();
                    } else {
                        event.getChannel().sendMessage("<@!" + event.getMember().getIdLong() + ">, you need to have 10 orbs and stand on " + corBlock.get(3) + ".").queue((message -> {
                            message.delete().queueAfter(2L,TimeUnit.SECONDS);
                        }));
                    }
                } else if (reactionEmote.equals(DiscordConstants.cancel)) {
                    inactiveEmbed(tempMessageId,tempEmbed,DiscordConstants.dungeon12ChannelId);
                    leaveGame();
                }
            }
        }
    }


    public void startGame() {
        Random blockGenerator = new Random();
        for (int count = 0; count < 3;count++) {
            for (int count2 = 0; count2 < 3;count2++) {
                columns.add(blockGenerator.nextInt(4));
            }
            rows.add(new ArrayList<>(columns));
            columns.clear();
        }
        rows.get(1).set(1,3);

        memberBoard = new ArrayList<>(rows);
        currentBlock = corBlock.get(memberBoard.get(Math.abs((int)corPoint.getY()-2)).get((int)corPoint.getX()));
        currentBlockNumber = memberBoard.get(Math.abs((int)corPoint.getY()-2)).get((int)corPoint.getX());

        memberBoard.get(1).set(1,4);
    }

    @Override
    public void initializeDungeon(int lifeNum, long dragonHealthNum, long at) {
        super.initializeDungeon(lifeNum,dragonHealthNum,at);
        orbs = 0;
        corPoint = new Point(1,1);
    }

    @Override
    public void leaveGame() {
        super.leaveGame();
        orbs = 0;
    }

    private void startTimer() {
        setMemberTimer(30);
        if (!isAvailable) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (getMemberTimer() == 0) {
                        inactiveEmbed(tempMessageId,tempEmbed, DiscordConstants.dungeon12ChannelId);
                        leaveGame();
                        timer.cancel();
                    } else if (isAvailable){
                        timer.cancel();
                    } else {
                        setMemberTimer(getMemberTimer() - 1);
                        System.out.println(getMemberTimer());
                    }
                }
            };
            timer.schedule(task,1000,1000);
        }
    }

    private EmbedBuilder makeEmbed() {
        EmbedBuilder layout = new EmbedBuilder();
        layout.clear();
        layout.setTitle("YOU HAVE ENCOUNTERED THE OMEGA DRAGON");
        layout.setColor(0x3fd15c);
        layout.setDescription(DiscordConstants.dragon12 +
                "** THE OMEGA DRAGON IS PREPARING HIS FIRST ATTACK**\n" +
                getTopTwoLines() +
                make3x3());
        layout.addField("What will you do, " + authorName + "?","```" +
                DiscordConstants.up + " - Move up\n" +
                DiscordConstants.left + " - Move to the left\n" +
                DiscordConstants.right + " - Move to the right\n" +
                DiscordConstants.down + " - Move down\n" +
                DiscordConstants.attack + " - Must be standing on ⬜ and have 10 " +
                DiscordConstants.orb + " orbs\n" +
                DiscordConstants.cancel + " - Exit the game```",false);
        layout.addField("Squares","Currently on " + currentBlock +
                "\n\uD83D\uDD05 Energy orbs: " + orbs + "/" + maxOrbs + "\n\n" +
                "\uD83D\uDD32 -> +5 HP, -5 \uD83D\uDD05 orb\n" +
                "\uD83D\uDD33 -> -500 HP\n" +
                "⬛ -> +1 \uD83D\uDD05 orb, -25 HP\n" +
                "⬜ -> nothing", false);
        layout.setFooter("You are in DUNGEON12! React below! This message will be inactive after 30 seconds.");
        tempEmbed = layout;
        return layout;
    }

    private EmbedBuilder editedEmbed() {
        EmbedBuilder layout = new EmbedBuilder();
        layout.clear();
        layout.setAuthor(authorName + "'s dungeon");
        layout.setDescription(DiscordConstants.dragon12 +
                "** THE OMEGA DRAGON**\n" + "**" +
                authorName +
                "**" + " MOVED TO " +
                effect +
                "\n**THE OMEGA DRAGON** HITS " + "**" +
                authorName + "** BY 30 POINTS OF LIFE\n" +
                "—————————————————————\n" +
                "**THE OMEGA DRAGON** — " +
                DiscordConstants.purpleLife +
                "\n" +
                dragonHealth + "/" + dragonMaxHealth +
                "\n" +
                "**" + authorName + "**" + " — " +
                DiscordConstants.redLife + " " +
                life + "/" + maxLife +
                "\n—————————————————————");
        layout.setColor(0x3fd15c);
        layout.addField("",getTopTwoLines() + make3x3(),false);
        layout.addField("What will you do, " + authorName + "?","```" +
                DiscordConstants.up + " - Move up\n" +
                DiscordConstants.left + " - Move to the left\n" +
                DiscordConstants.right + " - Move to the right\n" +
                DiscordConstants.down + " - Move down\n" +
                DiscordConstants.attack + " - Must be standing on ⬜ and have 10 " +
                DiscordConstants.orb + " orbs\n" +
                DiscordConstants.cancel + " - Exit the game```",false);
        layout.addField("Squares","Currently on " + currentBlock +
                "\n\uD83D\uDD05 Energy orbs: " + orbs + "/" + maxOrbs + "\n\n" +
                "\uD83D\uDD32 -> +5 HP, -5 \uD83D\uDD05 orb\n" +
                "\uD83D\uDD33 -> -500 HP\n" +
                "⬛ -> +1 \uD83D\uDD05 orb, -25 HP\n" +
                "⬜ -> nothing", false);
        layout.setFooter("You are in DUNGEON12! React below! This message will be inactive after 30 seconds.");
        tempEmbed = layout;
        return layout;
    }

    private EmbedBuilder deadEmbed() {
        EmbedBuilder layout = new EmbedBuilder();
        layout.clear();
        layout.setAuthor(authorName + "'s dungeon");
        layout.setDescription(DiscordConstants.dragon12 +
                "** THE OMEGA DRAGON**\n**THE OMEGA DRAGON** HITS " +
                "**" + authorName + "**" +
                " BY 30 POINTS OF LIFE\n" +
                "—————————————————————\n" +
                "**THE OMEGA DRAGON** — " + DiscordConstants.purpleLife + " " +
                dragonHealth + "/" + dragonMaxHealth +
                "\n" +
                "**" + authorName + "** — " +
                DiscordConstants.redLife +
                " 0/" + maxLife +
                "\n—————————————————————\n");
        layout.setColor(0x3fd15c);
        layout.addField("",getTopTwoLines() + make3x3(),false);
        layout.addField("","**" + authorName + "**" + " is dead!\nBetter luck next time!",false);
        return layout;
    }

    private EmbedBuilder winEmbed() {
        EmbedBuilder layout = new EmbedBuilder();
        layout.clear();
        layout.setAuthor(authorName + "'s dungeon");
        layout.setDescription(DiscordConstants.dragon12 + "** THE OMEGA DRAGON**\n**" + authorName + "**" + " HAS KILLED THE OMEGA DRAGON\n" +
                "—————————————————————\n" + "**THE OMEGA DRAGON** — " + DiscordConstants.purpleLife + " " +
                dragonHealth + "/" + dragonMaxHealth +
                "\n" +
                "**" + authorName + "** — " +
                DiscordConstants.redLife + " " +
                life + "/" + maxLife +
                "\n—————————————————————\n");
        layout.setColor(0x3fd15c);
        layout.addField("",getTopTwoLines() + make3x3(),false);
        layout.addField("","**THE OMEGA DRAGON** is dead!",false);
        return layout;
    }

    private String getTopTwoLines() {
        return corBlock.get(3) +
                DiscordConstants.dragon12 +
                corBlock.get(3) +
                "\n" +
                DiscordConstants.fire +
                DiscordConstants.fire +
                DiscordConstants.fire +
                "\n";
    }

    private String make3x3() {
        String temp = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                temp += corBlock.get(memberBoard.get(i).get(j));
            }
            temp += "\n";
        }
        return temp;
    }

    private void changingTask() {
        //replace armor
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (memberBoard.get(i).get(j) == 4) {
                    memberBoard.get(i).set(j,currentBlockNumber);

                }
            }
        }

        //place armor and get currentBlock
        currentBlock = corBlock.get(memberBoard.get(Math.abs((int)corPoint.getY()-2)).get((int)corPoint.getX()));
        currentBlockNumber = memberBoard.get(Math.abs((int)corPoint.getY()-2)).get((int)corPoint.getX());
        memberBoard.get(Math.abs((int)corPoint.getY()-2)).set((int)corPoint.getX(),4);
        //change blocks order

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                switch (memberBoard.get(i).get(j)) {
                    case 0:
                        memberBoard.get(i).set(j,3);
                        break;
                    case 1:
                        memberBoard.get(i).set(j,0);
                        break;
                    case 2:
                        memberBoard.get(i).set(j,1);
                        break;
                    case 3:
                        memberBoard.get(i).set(j,2);
                        break;
                    default:
                        break;
                }
            }
        }
        effect = getEffectName();
    }

    private String getEffectName() {
        String effect = "";
        switch (currentBlockNumber) {
            case 0:
                if (orbs == 10) {
                    effect = corBlock.get(0) + ": -25HP";
                } else {
                    effect = corBlock.get(0) + ": -25 HP, +1 orb";
                }
                changeLife(-25);
                changeOrbs(1);
                break;
            case 1:
                effect = corBlock.get(1) + ": -500 HP";
                changeLife(-500);
                break;
            case 2:
                changeLife(+5);
                if (orbs > 4) {
                    effect = corBlock.get(2) + ": +5 HP, -5 orb";
                    changeOrbs(-5);
                } else if (orbs != 0) {
                    effect = corBlock.get(2) + ": +5 HP, -" + orbs + " orb";
                    changeOrbs(orbs*-1);
                } else {
                    effect = corBlock.get(2) + ": +5 HP";
                }
                break;
            case 3:
                effect = corBlock.get(3);
                break;
        }
        return effect;
    }

    private void changeOrbs(int num) {
        orbs += num;
        if (orbs > 10) {
            orbs = 10;
        }
    }

}
