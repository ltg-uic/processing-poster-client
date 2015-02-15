package ltg.evl;

import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import de.caluga.morphium.query.Query;
import ltg.evl.json.mongo.Poster;
import ltg.evl.json.mongo.PosterItem;
import ltg.evl.json.mongo.User;
import ltg.evl.util.DBHelper;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by aperritano on 2/11/15.
 */
public class MongoDBTest {

    DBHelper helper = DBHelper.helper();
    
    @BeforeClass
    public static void setUpClass()  {}

    @Test
    public void getUsers() {
        Query<User> u = helper.dbClient().createQueryFor(User.class);
        List<User> users = u.asList();
        
        if( users.isEmpty() ) {
            helper.createUser("DrBanner", "poster");
        }
        
        for (User user: users) {
            System.out.println("USER " + user);
        }
        
        
    } 
    
    @Ignore
    public void insertUser() {
        User user = new User();
        user.setName("HELLO");
        user.addPoster(getPoster());
        helper.dbClient().store(user);
    }
    
    @Ignore
    public void insertPoster() {
        Poster poster = new Poster();
        poster.setName("hello poster");
        poster.setHeight(300);
        poster.setWidth(500);
        poster.addPosterItems(getPosterItem());
        helper.dbClient().store(poster);
    }

    @Ignore
    public void insertPosterItem() {
        PosterItem pi = new PosterItem();
        pi.setName("PI");
        pi.setWidth(500);
        pi.setHeight(800);
        pi.setX(50);
        pi.setY(20);
        helper.dbClient().store(pi);
    }
    
    public PosterItem getPosterItem() {
        
        
        
        
        PosterItem pi = new PosterItem();
        pi.setName("PI");
        pi.setWidth(500);
        pi.setHeight(800);
        pi.setX(50);
        pi.setY(20);
        pi.setImageId("Dfdfd");
        try {
            pi.setImageId(saveImageIntoMongoDB(ClassLoader.getSystemResource("document6.png").getPath()));
            //pi.setImageFile(saveImageIntoMongoDB(ClassLoader.getSystemResource("document6.png").getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pi;
    }

    public Poster getPoster() {
        Poster pi = new Poster();
        pi.setName("PI");
        pi.setWidth(500);
        pi.setHeight(800);
        pi.addPosterItems(getPosterItem());
        return pi;
    }

    private static String saveImageIntoMongoDB(String path) throws IOException {
        String filename = UUID.randomUUID().toString()+".png";
        File imageFile = new File(path);
        GridFS gfsPhoto = new GridFS(DBHelper.helper().dbClient().getDatabase(), "image");
        GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);
        gfsFile.setFilename(filename);
        gfsFile.save();
        return filename;
    }

        @After
    public void tearDown() throws Exception {
        helper.dbClient().close();
    }
}
