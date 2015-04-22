package ltg.evl.util;


import com.google.common.io.Resources;
import javaxt.io.File;

import java.util.ArrayList;
import java.util.EventListener;

public class DownloadHelper {

    private final String backpackPath;
    private ArrayList<NewFileAddedEventListener> eventListeners = new ArrayList<>();

    public DownloadHelper(String backpackPath) {
        this.backpackPath = backpackPath;
        //startDirectoryWatch();
    }

//    public static void saveImageIntoMongoDB(String fileName, String filePath) throws IOException {
//        java.io.File imageFile = new java.io.File(filePath);
//        GridFS gfsPhoto = new GridFS(DBHelper.helper().dbClient().getDatabase(), "image");
//        GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);
//        gfsFile.setFilename(fileName);
//        gfsFile.save();
//    }

    public static void downloadFileToBackpack(final String url, final String savePath) {
        Thread download = new Thread() {
            public void run() {
                javaxt.io.Image image = new javaxt.http.Request(url).getResponse().getImage();
                image.saveAs(savePath);
            }
        };
        download.start();//start the thread
    }

    public static byte[] getImageBytes(String filename) {
        return new javaxt.io.Image(Resources.getResource(filename).getPath()).getByteArray();
    }

    public static javaxt.io.Image getImage(byte[] bytes) {
        return new javaxt.io.Image(bytes);

    }

    public File[] getAllBackpackFiles() {
        javaxt.io.Directory directory = new javaxt.io.Directory(backpackPath);
        return directory.getFiles();
    }

    public void writeJSONFile(Object obj) {
    }

//    public void startDirectoryWatch() {
//
//        Thread filePoll = new Thread() {
//
//            public void run() {
//
//                javaxt.io.Directory directory = new javaxt.io.Directory(backpackPath);
//                List<Directory.Event> events = null;
//                try {
//                    events = directory.getEvents();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                while (true) {
//
//                    javaxt.io.Directory.Event event;
//                    synchronized (events) {
//                        while (events.isEmpty()) {
//                            try {
//                                events.wait();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        event = events.remove(0);
//                    }
//
//                    if (event != null) {
//
//                        System.out.println(event.toString());
//
//                        //new file
//                        if (event.getEventID() == Directory.Event.MODIFY) {
//                            File newFile = new File(event.getFile());
//                            System.out.println("File content getType:" + newFile.getContentType());
//
//
//                            for (NewFileAddedEventListener listener : eventListeners) {
//                                listener.newFileAdded(new FileWatchEvent(newFile));
//                            }
//
//
//                        }
//                        //Compare files before/after the event
//                        if (event.getEventID() == Directory.Event.RENAME) {
//                            System.out.println(
//                                    event.getOriginalFile() + " vs " + event.getFile()
//                                              );
//                        }
//                    }
//
//                }
//
//            }
//        };
//        filePoll.start();//start the thread
//    }

    public void addFileWatchEventListener(NewFileAddedEventListener listener) {
        eventListeners.add(listener);
    }

    public interface NewFileAddedEventListener extends EventListener {
        void newFileAdded(FileWatchEvent evt);
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

}
