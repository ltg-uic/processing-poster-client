package ltg.evl;

import javaxt.io.Image;
import ltg.evl.uic.poster.json.parse.PImageFile;
import ltg.evl.uic.poster.json.parse.PItem;
import ltg.evl.uic.poster.json.parse.PPoster;
import ltg.evl.uic.poster.json.parse.PUser;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.parse4j.Parse;
import org.parse4j.ParseException;
import org.parse4j.ParseFile;
import org.parse4j.callback.SaveCallback;
import org.parse4j.util.ParseRegistry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by aperritano on 2/11/15.
 */


public class ParseTest {


    PUser user = new PUser();

    String imageFileId = UUID.randomUUID().toString();
    ParseFile file;

    @BeforeClass
    public static void setUpClass() {


        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new SystemConfiguration());

        try {
            config.addConfiguration(new PropertiesConfiguration("parse.properties"));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        ParseRegistry.registerSubclass(PUser.class);
        ParseRegistry.registerSubclass(PPoster.class);
        ParseRegistry.registerSubclass(PItem.class);
        ParseRegistry.registerSubclass(PImageFile.class);
        Parse.initialize(config.getString("parse.appid"), config.getString("parse.clientkey"));

    }

    @Test
    public void uploadImage() {
        final Image img = new Image(ClassLoader.getSystemResource("document6.png").getPath());


        file = new ParseFile("image.png", img.getByteArray());
        try {
            file.save(new SaveCallback() {
                @Override
                public void done(ParseException parseException) {
                    PImageFile photoFile = new PImageFile();
                    photoFile.setImageFileId(imageFileId);
                    photoFile.setImageFile(file);
                    try {
                        photoFile.save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Ignore
    public void insertPosterItem() throws IOException, ParseException {

        final PItem pi = new PItem();
        pi.setType("image");
        pi.setHeight(256);
        pi.setWidth(256);
        pi.setX(500);
        pi.setY(500);

        final Image img = new Image(ClassLoader.getSystemResource("document6.png").getPath());

        // pi.setImage("document6.png", img);
        pi.setImageFileId(UUID.randomUUID().toString());
        pi.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException parseException) {
                final PImageFile pim = new PImageFile();
                pim.setImageFileId(pi.getImageFileId());
//                    pim.saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException parseException) {
//                            final ParseFile file = new ParseFile(pi.getImageFileId(),  img.getByteArray());
//                            try {
//                                file.save(new SaveCallback() {
//                                    @Override
//                                    public void done(ParseException parseException) {
//                                        pim.put("imageFile",file);
//                                    }
//                                });
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
                try {
                    pim.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        pi.save();
    }

//    @Test
//    public void getPosterItem() {
//        ParseQuery<PItem> query = ParseQuery.getQuery(PItem.class);
//      //  query.whereLessThanOrEqualTo("getType", "image");
//
//        final ArrayList<String> ids = new ArrayList<>();
//        query.findInBackground(new FindCallback<PItem>() {
//            @Override
//            public void done(List<PItem> results, ParseException parseException) {
//
//                System.out.println("");
//
//                for (PItem pItem : results) {
//
//                   ids.add(pItem.getImageID());
//                   // pItem.getImage(id);
//                    System.out.println("here");
//                }
//
//            }
//        });
//        try {
//            query.find();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        ParseQuery<ParseObject> iq = ParseQuery.getQuery("ImageFile");
//        iq.whereEqualTo("imageFile", ids.get(0));
//        iq.findInBackground(new FindCallback<ParseObject>() {
//            public void done(List<ParseObject> scoreList, ParseException e) {
//                if (e == null) {
//                    //Log.d("score", "Retrieved " + scoreList.size() + " scores");
//                } else {
//                    // Log.d("score", "Error: " + e.getMessage());
//                }
//            }
//        });
//        try {
//            iq.find();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//    }

    @Ignore
    public void insertPosterPosterItem() throws IOException, ParseException {

        final PPoster poster = new PPoster();
        poster.setHeight(1140);
        poster.setWidth(2560);
        poster.setName("new poster");
        poster.save();


//        poster.addPosterItems(pi);
//        poster.saveInBackground();
//        poster.save();
//
//        user.setName("Banner");
//        user.save();
//
//        user.addPoster(poster);
//        user.save();

    }

    public void convert(String fileName, byte[] data) throws IOException {
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
