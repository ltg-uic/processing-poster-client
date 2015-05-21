package tmp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ltg.evl.uic.poster.json.mongo.PosterGrab;

import java.io.IOException;

public class GsonTest {

    public static void main(String[] args) throws IOException {

//        InputStream is = GsonTest.class.getResourceAsStream("/test.json");
//
//        Reader reader = new InputStreamReader(is);
//        String text = new javaxt.io.File("test.json").getText();
//
//        String targetString = CharStreams.toString(reader);




        Gson gson = new GsonBuilder().create();
        PosterGrab posterGrab = new PosterGrab();
//        posterGrab.setGrabbingUserName("552be82e5f740e70d9000000-gruser");
//        posterGrab.setPosterDestinationUuid("5546d35eede3d929be000000-poster");
//        posterGrab.setPosterItemGrabbedUuid("5546ddc8ede3d90d7b00000a-txtitem");

        String s = gson.toJson(posterGrab);

        System.out.println(s);
//        PosterMessage p;
//
//            Gson gson = new GsonBuilder().create();
//            p = gson.fromJson(reader, PosterMessage.class);

        //PosterItem pp = gson.fromJson(p.getContent(), PosterItem.class);


    }
}
