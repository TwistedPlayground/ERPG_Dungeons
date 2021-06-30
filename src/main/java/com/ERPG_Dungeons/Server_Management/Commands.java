package com.ERPG_Dungeons.Server_Management;

import com.ERPG_Dungeons.DiscordConstants;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Commands extends ListenerAdapter {

    int num;

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (!event.getMember().getUser().isBot()) {
            String[] args = event.getMessage().getContentRaw().split(" ");
            if (args[0].equalsIgnoreCase(DiscordConstants.prefix + "purge")) {

                try {
                    if (args.length != 2 || Integer.parseInt(args[1]) > 100 || Integer.parseInt(args[1]) < 2) {
                        event.getChannel().sendMessage("Usage: " + DiscordConstants.prefix + "purge [2-100]").queue((message -> {
                            message.delete().queueAfter(2L, TimeUnit.SECONDS);
                        }));
                    } else {
                        try {
                            if (event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                                List<Message> messages = event.getChannel().getHistory().retrievePast(Integer.parseInt(args[1])).complete();
                                event.getChannel().deleteMessages(messages).queue();
                            }
                        } catch (IllegalArgumentException E) {
                            //event.getChannel().sendMessage("Make sure the messages aren't 2 weeks old, and there are messages for me to purge.").queue();
                        }
                    }
                } catch (NumberFormatException E) {
                    event.getChannel().sendMessage("Please provide an Integer!").queue((message ->  {
                        message.delete().queueAfter(2L, TimeUnit.SECONDS);
                    }));
                }
            }
        }
    }


}
