package ltg.evl;

import ltg.evl.JSON.Location;
import ltg.evl.JSON.PosterItem;
import ltg.evl.JSON.Size;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.ektorp.Attachment;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.UUID;

/**
 * Created by aperritano on 2/11/15.
 */
public class CouchDBTest {

    static HttpClient authenticatedHttpClient;
    static CouchDbConnector db;
    @BeforeClass
    public static void setUpClass() throws MalformedURLException {
        authenticatedHttpClient = new StdHttpClient.Builder().build();
        CouchDbInstance dbInstance = new StdCouchDbInstance(authenticatedHttpClient);
        db = dbInstance.createConnector("chicago_poster", true);
    }
    
    @Test
    public void inserts() throws IOException {
//        Poster p = new Poster(UUID.randomUUID().toString());
//        p.setResolution(new Dimension(1200, 800));


        PosterItem pi = new PosterItem();
        pi.setSize(new Size(500,500));
        pi.setLocation(new Location(300,300));


        File img = new File(ClassLoader.getSystemResource("document6.png").getPath());
        BufferedImage bimg = ImageIO.read(img);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( bimg, "png", baos );
        
        
        String data = Base64.encodeBase64String(baos.toByteArray());
        baos.flush();
        Attachment inline = new Attachment(UUID.randomUUID().toString(), data, "image/png");
        pi.addInlineAttachment(inline);
        
        db.create(pi);

//
//        Attachment a  = DBHelper.getSingleton().saveAttachment(pi,bimg);
//        pi.addAttachment("image.png", a);
//
//
//        DBHelper.getSingleton().getDBClient().save(pi);
//
//
//
//
//
//
//
//
//
//       //
//
//
////        String[] pis = new String[] { pi.getId() };
////
////        p.setPosterItems(pis);
////
////
////        Response response = DBHelper.getSingleton().getDBClient().save(p);
//
//        getAttachment(testId);
        
        //System.out.println(response.toString());
    }
    
//    public void getAttachment(String testId) throws IOException {
//        PosterItem pi = DBHelper.getSingleton().getDBClient().find(PosterItem.class,testId, new Params().attachments());
//
//        Attachment attachment = pi.getAttachments().get("image.png");
//
//
//        byte[] bytes = pi.getAttachments().get("image.png").getData().getBytes();
//
//        convert("/Users/aperritano/dev/research/testing/backpack/saved2222.png", bytes);
//        }

    public void convert(String fileName , byte[] data) throws IOException {
        File myFile = new File(fileName);
        System.out.println("filename is " + myFile);
        OutputStream out = new FileOutputStream(myFile);
        try {
            out.write(data); // Just dump the database content to disk
        } finally {
            out.close();
        }
        System.out.println("Image file written successfully");
    }
}
