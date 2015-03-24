package tmp;

import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by aperritano on 3/22/15.
 */
public class GsonTest {

    public static void main(String[] args) throws IOException {

        InputStream is = GsonTest.class.getResourceAsStream("/test.json");

        Reader reader = new InputStreamReader(is);
        String text = new javaxt.io.File("test.json").getText();

        String targetString = CharStreams.toString(reader);
//        PosterMessage p;
//
//            Gson gson = new GsonBuilder().create();
//            p = gson.fromJson(reader, PosterMessage.class);

        //PosterItem pp = gson.fromJson(p.getContent(), PosterItem.class);


    }
}
