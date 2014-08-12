package ltg.poster;

import com.phloc.commons.charset.CCharset;
import com.phloc.css.ECSSVersion;
import com.phloc.css.decl.CascadingStyleSheet;
import com.phloc.css.reader.CSSReader;

import java.io.File;
import java.util.Random;

/**
 * Created by aperritano on 8/11/14.
 */
public class Util {

    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static CascadingStyleSheet readCSS30() {

//        // UTF-8 is the fallback if neither a BOM nor @charset rule is present
//        aCSS = CSSReader.readFromFile(new File("interface.css"), CCharset.CHARSET_UTF_8_OBJ, ECSSVersion.CSS30);
//        if (aCSS == null) {
//            // Most probably a syntax error
//
//            println("Failed to read CSS - please see previous logging entries!");
//            return null;
//        }
//        return aCSS;
        return null;
    }
}
