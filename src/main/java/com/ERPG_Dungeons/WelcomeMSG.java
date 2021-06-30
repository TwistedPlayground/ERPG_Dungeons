package com.ERPG_Dungeons;

import com.ERPG_Dungeons.DiscordConstants;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class WelcomeMSG extends ListenerAdapter {


    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Member member = event.getMember();
        event.getGuild().getTextChannelById(DiscordConstants.onMemberJoinedChannelId).sendMessage("Hello " + member.getAsMention() + ", hope you enjoy your stay here!").queue();



    }
}
