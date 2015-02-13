package ltg.evl.util;

import org.apache.commons.codec.binary.Base64;
import org.lightcouch.Attachment;
import org.lightcouch.CouchDbClient;
import org.lightcouch.Document;
import org.lightcouch.Response;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.util.UUID;

/**
 * Created by aperritano on 2/11/15.
 */
public class DBHelper {
    
    private CouchDbClient dbClient;
   
    private static class StaticHolder {
        static final DBHelper INSTANCE = new DBHelper();
    }
    
    public static DBHelper getSingleton() {
        return StaticHolder.INSTANCE;
    }

    private DBHelper() {
        dbClient = new CouchDbClient();
    }

    public void savePOJO(Object obj) {
        Response response = dbClient.save(obj);
        response.toString();

    }
    
    public CouchDbClient getDBClient() {
        return  dbClient;
    }
    
    public Attachment saveAttachment(Document document, BufferedImage image) {
        byte[] buffer = ((DataBufferByte)(image).getRaster().getDataBuffer()).getData();

        String data = Base64.encodeBase64String(buffer);

        Attachment attachment = new Attachment(data, "image/png");
        
        return attachment;
    }

    
}
