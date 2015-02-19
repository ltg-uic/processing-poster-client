package ltg.evl.uic.poster.json.parse;

import org.parse4j.ParseClassName;
import org.parse4j.ParseObject;

/**
 * Created by aperritano on 2/13/15.
 */
@ParseClassName("PItem")
public class PItem extends ParseObject {


    private int x;
    private int y;
    private int width;
    private int height;
    private String fileUUID;


    public PItem() {
    }

    public String getType() {
        return getString("type");
    }

    public void setType(String type) {
        put("type", type);
    }

//    public void setImage(String name, Image image) {
//
//
//        fileUUID = UUID.randomUUID().toString() + ".png";
//        put("imageID",fileUUID);
//        final ParseFile file = new ParseFile(fileUUID,  image.getByteArray());
//        try {
//            file.save(new SaveCallback() {
//                @Override
//                public void done(ParseException parseException) {
//                    try {
//
//                        ParseObject imageFile = new ParseObject("ImageFile");
//                        imageFile.put("imageID", fileUUID);
//                        imageFile.put("imageFile", file);
//                        imageFile.save();
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            file.save();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
//
//
//
//
//
//    }

    public String getImageFileId() {
        return getString("imageFileId");

    }

    public void setImageFileId(String imageId) {
        put("imageFileId", imageId);

    }

    public int getHeight() {
        return getInt("height");
    }

    public void setHeight(int height) {
        put("height", height);
    }

    public int getWidth() {
        return getInt("width");
    }

    public void setWidth(int width) {
        put("width", width);
    }

    public int getY() {
        return getInt("y");
    }

    public void setY(int y) {
        put("y", y);
    }

    public int getX() {
        return getInt("x");
    }

    public void setX(int x) {
        put("x", x);
    }

//    public String getFileUUID() {
//        return getString("fileUUID");
//    }
//
//    public void setFileUUID(String fileUUID) {
//        put("fileUUID", fileUUID);
//    }
}
