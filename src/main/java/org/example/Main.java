package org.example;

import ca.tristan.easycommands.EasyCommands;
import ca.tristan.easycommands.commands.defaults.HelpCmd;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main extends ListenerAdapter{

    private static JDA jda;

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {

        EasyCommands ec = new EasyCommands();
        Main main = new Main();
        jda = ec.registerListeners(main) // Register the instance as an event listener
                .addExecutor(new HelpCmd(ec))
                .addEnabledCacheFlags()
                .addGatewayIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .buildJDA();
    }
    // Event listener for when a message is received
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // Ignore messages sent by the bot itself to avoid potential infinite loops
        if (event.getAuthor().isBot()) {
            return;
        }
        //Check if the message content is "hello"
        // Check if the message content is "hello" or "hi" (case in-sensitive)
        String messageContent = event.getMessage().getContentRaw().toLowerCase();
        if (messageContent.equals("hello") || messageContent.equals("hi")) {
            // Respond to the user
            event.getChannel().sendMessage("Hello, " + event.getAuthor().getAsMention() + "!").queue();
        }
        if (messageContent.equals("menu")) {
            // Respond with the menu options
            String menu = "Menu:\n";
            menu += "1. Start Trivia\n";
            menu += "2. Stop Trivia\n";
            menu += "3. View Scoreboard\n";
            menu += "4. Choose Trivia Category\n";
            menu += "5. Set Difficulty Level\n";
            menu += "6. Request Hint\n";
            menu += "7. View Leaderboard\n";
            menu += "8. Start Multiplayer Mode\n";
            menu += "9. Random Trivia\n";
            menu += "10. Custom Settings\n";
            menu += "11. Achievements and Rewards\n";
            menu += "12. Trivia Tournaments\n";
            menu += "13. Challenge Friends\n";
            menu += "14. Trivia Statistics\n";

            event.getChannel().sendMessage(menu).queue();
        }
    }

}