package ltg.evl;

import java.util.Random;

/**
 * Created by aperritano on 3/31/15.
 */

public class RandomSentences {


    static Random r = new Random();

    public static String generateRandomSentent(int NO_WORDS) {

        // These constants must be static
        if (NO_WORDS < 0 || NO_WORDS > 5)
            NO_WORDS = 5;

        int NO_SENTS = 1;    // if they are going to be used in
        String SPACE = " ";    // a static method like main().
        String PERIOD = ".";

        String article[] = {"the", "a", "one", "some", "any", "go", "good", "make", "put", "up", "every"};
        String noun[] = {"boy", "robot", "dog", "town", "car"};
        String verb[] = {"drove", "jumped", "ran", "walked", "skipped", "hailed", "booed", "trashed"};
        String preposition[] = {"to", "from", "over", "under", "on", "through", "beneath", "from"};

        String sentence = null;
        for (int i = 0; i < NO_SENTS; i++) {
            sentence = article[rand(NO_WORDS)];
            char c = sentence.charAt(0);
            sentence = sentence.replace(c, Character.toUpperCase(c));
            sentence += SPACE + noun[rand(NO_WORDS)] + SPACE;
            sentence += (verb[rand(NO_WORDS)] + SPACE + preposition[rand(NO_WORDS)]);
            sentence += (SPACE + article[rand(NO_WORDS)] + SPACE + noun[rand(NO_WORDS)]);
            sentence += PERIOD;
            System.out.println(sentence);
            sentence = "";
        }

        return sentence;
    }

    static int rand(int NO_WORDS) {
        int ri = r.nextInt() % NO_WORDS;
        if (ri < 0)
            ri += NO_WORDS;
        return ri;
    }
}
