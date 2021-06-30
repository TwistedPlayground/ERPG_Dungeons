package com.ERPG_Dungeons;


import com.ERPG_Dungeons.Dungeons.Dungeon12;
import com.ERPG_Dungeons.Server_Management.Commands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class ERPG_Dungeons {

    GatewayIntent[] gatewayIntents = new GatewayIntent[]{GatewayIntent.GUILD_MEMBERS};
    ListenerAdapter[] listenerAdapters = new ListenerAdapter[]{new WelcomeMSG(), new Dungeon12(), new Commands()};
    public static JDA jda;


    public void startBot() {
        JDABuilder jdaBuilder = JDABuilder.createDefault(DiscordConstants.token);

        jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS, gatewayIntents);

        jdaBuilder.addEventListeners(listenerAdapters);


        jdaBuilder.setActivity(Activity.playing("Playing ERPG com.com.ERPG_Dungeons.ERPG_Dungeons.Dungeons.Dungeons"));

        try {
            jda = jdaBuilder.build();
            jda.awaitReady();
            jda.getTextChannelById(DiscordConstants.onOnlineStatusChannelId).sendMessage("Dungeon Tester Is Online").queue();
        } catch (LoginException | InterruptedException e){
            e.printStackTrace();
        }

    }
}
