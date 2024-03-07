import java.io.*;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

/**
 * Creates the structures and methods necessary to have questions for the discord bot
 */
public class Questions {
    private HashMap<String, String> questionBank;
    Random picker;
    int numQs;

    /**
     * Constructs a question bank to be populated with as many trivia questions as we need.
     * Made of a hashmap where the Question is the key and the Answer is the value.
     */
    public Questions() {
        questionBank = new HashMap<String, String>();
        picker = new Random();
        numQs = 0;
    }

    /**
     * //TODO
     * Picks a question out of the question bank to ask.
     *
     * @return The question String.
     */
//    public String pickQuestion() {
//        int pickedNum = picker.nextInt(0, numQs);
//        Set<String> possibleQs = questionBank.keySet();
//        String[] questions = (String[]) possibleQs.toArray();
//        String pickedQuestion = questions[pickedNum];
//
//        if (pickedQuestion.equals(null)) {
//            return "fail";
//        }
//        return pickedQuestion;
//    }

    /**
     * //TODO
     * Finds the answer to a given trivia question
     *
     * @param question the trivia question that requires an answer
     * @return the answer to the question
     */
    public String getAnswer(String question) {
        String answer = questionBank.get(question);
        if (answer.equals(null)) {
            return question + " is not a question";
        }
        return answer;
    }

    /**
     * Automates the process of adding trivia questions to the question bank by
     * allowing us to parse csv's with questions in first column and answers in next.
     *
     * @param csvFilePath the filepath to parse questions from to add to the question bank.
     * @return questionBank.
     */
    protected HashMap<String, String> readCSV(String csvFilePath) {
        questionBank = new HashMap<>(); // clear questionBank

        // Create new BufferedReader for csv file of questions and answers
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length >= 2) {
                    String question = columns[0].trim(); // note questions should be in 1st column
                    String answer = columns[1].trim(); // note answers should be in 2nd column
                    questionBank.put(question, answer);
                }
                else {
                    System.out.println("Invalid record: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return questionBank;
    }

    /**
     * A function to add trivia questions to the bank.
     *
     * @param question The question to add to the question bank
     * @param answer The answer of the trivia question
     */
    private void addQuestion(String question, String answer) {
        String prevAnswer = questionBank.put(question, answer);
        if (prevAnswer != null && !prevAnswer.equals(answer)) {
            System.out.println("Warning: Duplicate question added with different answer.");
        } else {
            numQs += 1;
        }
    }
}