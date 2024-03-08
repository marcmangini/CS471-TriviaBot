import ca.tristan.easycommands.EasyCommands;
import ca.tristan.easycommands.commands.defaults.HelpCmd;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class Main extends ListenerAdapter {

    private static JDA jda;
    private String selectedCategory;

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
        // Check if the message content is "hello" or "hi" (case insensitive)
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
        if (messageContent.equals("choose trivia category")) {
            // Respond with the available trivia categories
            String categories = "Available trivia categories:\n";
            categories += "1. Animals\n";
            categories += "2. Celebrities\n";
            categories += "3. Brain Teasers\n";

            event.getChannel().sendMessage(categories).queue();
        } else if (messageContent.equals("animals") || messageContent.equals("celebrities") || messageContent.equals("brain teasers")) {
            // Load questions based on the user's selection
            String category = messageContent.toLowerCase().replace(" ", "-"); // Remove the additional ".txt"
            try {
                List<Questions> questions = Questions.loadQuestionsFromFile(category);
                // Now you have the list of questions for the selected category
                // You can implement further logic to use these questions in your trivia game
                // For example, you can send a message with the first question to start the game
                if (!questions.isEmpty()) {
                    Questions firstQuestion = questions.get(0);
                    String questionText = firstQuestion.getQuestion();
                    List<String> options = firstQuestion.getOptions();
                    String optionsText = String.join("\n", options);
                    event.getChannel().sendMessage(questionText + "\nOptions:\n" + optionsText).queue();
                } else {
                    event.getChannel().sendMessage("No questions available for this category.").queue();
                }
            } catch (IOException e) {
                e.printStackTrace();
                event.getChannel().sendMessage("Error loading questions for the selected category.").queue();
            }
        }
        if (messageContent.equals("start trivia")) {
            // Check if the user has already chosen a category
            if (selectedCategory != null) {
                try {
                    List<Questions> questions = Questions.loadQuestionsFromFile(selectedCategory);
                    if (!questions.isEmpty()) {
                        // Start asking questions one by one
                        for (Questions question : questions) {
                            String questionText = question.getQuestion();
                            List<String> options = question.getOptions();
                            String optionsText = String.join("\n", options);
                            event.getChannel().sendMessage(questionText + "\nOptions:\n" + optionsText).queue();
                            // Wait for some time before asking the next question (e.g., 30 seconds)
                            Thread.sleep(30000);
                        }
                    } else {
                        event.getChannel().sendMessage("No questions available for this category.").queue();
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    event.getChannel().sendMessage("Error loading questions for the selected category.").queue();
                }
            } else {
                event.getChannel().sendMessage("Please choose a trivia category first.").queue();
            }
        }
        if (messageContent.equals("animals.txt") || messageContent.equals("celebrities") || messageContent.equals("brain teasers")) {
            // Load questions based on the user's selection
            selectedCategory = messageContent.toLowerCase().replace(" ", "-"); // Remove the additional ".txt"
            event.getChannel().sendMessage("Trivia category selected: " + selectedCategory).queue();
        }
    }
}
