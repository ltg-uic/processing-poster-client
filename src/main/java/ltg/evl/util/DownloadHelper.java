package ltg.evl.util;


import javaxt.io.Directory;
import javaxt.io.File;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * Created by aperritano on 10/1/14.
 */
public class DownloadHelper {

    private final String backpackPath;
    private ArrayList<NewFileAddedEventListener> eventListeners = new ArrayList<>();

    public DownloadHelper(String backpackPath) {
        this.backpackPath = backpackPath;
        startDirectoryWatch();
    }

    public File[] getAllBackpackFiles() {
        javaxt.io.Directory directory = new javaxt.io.Directory(backpackPath);
        return directory.getFiles();
    }

    public void writeJSONFile(Object obj) {
        
        
    }

    public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
    
    public void startDirectoryWatch() {

        Thread filePoll = new Thread() {

            public void run() {

                javaxt.io.Directory directory = new javaxt.io.Directory(backpackPath);
                List<Directory.Event> events = null;
                try {
                    events = directory.getEvents();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                while (true) {

                    javaxt.io.Directory.Event event;
                    synchronized (events) {
                        while (events.isEmpty()) {
                            try {
                                events.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        event = events.remove(0);
                    }

                    if (event != null) {

                        System.out.println(event.toString());

                        //new file
                        if( event.getEventID() == event.MODIFY ) {
                            File newFile = new File(event.getFile());
                            System.out.println("File content type:" + newFile.getContentType());


                            for (NewFileAddedEventListener listener : eventListeners) {
                                listener.newFileAdded(new FileWatchEvent(newFile));
                            }


                        }
                        //Compare files before/after the event
                        if (event.getEventID() == event.RENAME) {
                            System.out.println(
                                    event.getOriginalFile() + " vs " + event.getFile()
                            );
                        }
                    }

                }

            }
        };
        filePoll.start();//start the thread
    }

    public void addFileWatchEventListener(NewFileAddedEventListener listener) {
        eventListeners.add(listener);
    }

    public class FileWatchEvent {

        private String contentType;
        private String filePath;

        public FileWatchEvent(File newFile) {
            this.contentType = newFile.getContentType();
            this.filePath = newFile.getPath() + newFile.getName();
        }

        public String getFilePath() {
            return filePath;
        }

        public String getContentType() {
            return contentType;
        }
    }

    public interface NewFileAddedEventListener extends EventListener {
        public void newFileAdded(FileWatchEvent evt);
    }

    public static void downloadFileToBackpack(final String url, final String savePath) {
        Thread download = new Thread() {
            public void run() {
                javaxt.io.Image image = new javaxt.http.Request(url).getResponse().getImage();
                image.saveAs(savePath);
            }
        };
        download.start();//start the thread
    }



}
