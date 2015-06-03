package ltg.evl.uic.poster.widgets;

import ltg.evl.uic.poster.util.de.looksgood.ani.Ani;
import ltg.evl.uic.poster.widgets.button.ShareButton;
import ltg.evl.uic.poster.widgets.button.YesButton;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import vialab.SMT.ImageZone;
import vialab.SMT.SMT;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

import java.awt.*;


public class PresentationZone extends Zone {

    private static final int DIALOG_WIDTH = 120;
    public int color;
    private PFont font;
    private int bgAlpha;
    private boolean shouldDelete;
    private String okDialogText;
    private boolean shouldShowButton;

    public PresentationZone(String name, int x, int y, int width, int height) {
        super(name, x, y, width, height);
        this.initZone();
    }

    public PresentationZone(String name) {
        super(name, 0, 0, SMT.getApplet().getWidth(), SMT.getApplet().getHeight());
        this.initZone();
    }

    protected void initZone() {
        this.bgAlpha = 255;
        this.font = ZoneHelper.helveticaNeue18Font;
        this.color = ZoneHelper.darkBlueOutline;
    }

    @Override
    public boolean add(Zone zone) {
        return super.add(zone);
    }

    @Override
    public void touch() {
        doTouchAction();
    }

    public void doTouchAction() {
        delete();
    }

    @Override
    public void draw() {
        fill(color, bgAlpha);
        rect(0, 0, this.getWidth(), this.getHeight());
    }

    public void presentImageZone(PImage pImage, String posterItemUuid) {

        this.color = ZoneHelper.whiteOutline;
//        Dimension scaledDimension = ZoneHelper.resizeImage(pImage, this.getWidth(), this.getHeight());
        Dimension scaledDimension = ZoneHelper.resizeImageKRA(pImage, this.getWidth(), this.getHeight());

//        PImage newPImage = pImage.get();
//        if (newPImage.width > this.getWidth() && newPImage.height < this.getHeight()) {
//            newPImage.resize(this.getWidth(), 0);
//        } else if (newPImage.width < this.getWidth() && newPImage.height > this.getHeight()) {
//            newPImage.resize(0, this.getHeight() - 50);
//        } else if (newPImage.width <= this.getWidth() && newPImage.height <= this.getHeight()) {
//            newPImage.resize(0, this.getHeight() - 50);
//        }


//        System.out.println("New height: " + pImage.height + " New width: " + pImage.width);
//        Dimension scaledDimension = new Dimension(newPImage.width, newPImage.height);

        int x2 = (int) (PresentationZone.this.getHalfSize().getWidth() - (scaledDimension.getWidth() / 2));
        int y2 = (int) (PresentationZone.this.getHalfSize().getHeight() - (scaledDimension.getHeight() / 2));

        pImage.resize((int) scaledDimension.getWidth(), (int) scaledDimension.getHeight());

        final PVector targetPoint = new PVector(x2, y2);

        ImageZone imageZone = new ImageZone(pImage, x2, y2, (int) scaledDimension.getWidth(),
                                            (int) scaledDimension.getHeight()) {

            int initY = PresentationZone.this.getHeight();


            @Override
            public void touch() {
                rst(false, false, false);
            }


            @Override
            public void touchUp(Touch touch) {
                SMT.remove(PresentationZone.this);
            }

            public ImageZone doScaleAni(float speed, float delay, int alpha) {
                Ani diameterAni = new Ani(this, speed, delay, "initY", targetPoint.y, Ani.EXPO_IN_OUT,
                                          "onUpdate:update");
                diameterAni.start();
                return this;
            }

            public void update() {
                setY(initY);
                System.out.println("Y" + initY);
            }


        };//.doScaleAni(.5f, 0f, 255);

        //add share button

        int size = 100;

        int xa = this.getWidth() - (size * 2);
        ShareButton shareButton = new ShareButton("addme", 125, 75);
        shareButton.initButton();
        shareButton.setPosterItemUuid(posterItemUuid);
        shareButton.setVisible(true);



        this.add(imageZone);
        this.add(shareButton);
        shareButton.translate(xa, 50);
        //imageZone.putChildOnTop(shareButton);
    }

    public void presentVideoZone(PImage pImage, String posterItemUuid, int x, int y) {

        this.color = ZoneHelper.whiteOutline;
//        Dimension scaledDimension = ZoneHelper.resizeImage(pImage, this.getWidth(), this.getHeight());
//        Dimension scaledDimension = ZoneHelper.resizeImageKRA(pImage, this.getWidth(), this.getHeight());

//        int x2 = (int) (PresentationZone.this.getHalfSize().getWidth() - (scaledDimension.getWidth() / 2));
//        int y2 = (int) (PresentationZone.this.getHalfSize().getHeight() - (scaledDimension.getHeight() / 2));
//
//        pImage.resize((int) scaledDimension.getWidth(), (int) scaledDimension.getHeight());

        ImageZone imageZone = new ImageZone(pImage, x, y, pImage.width, pImage.height) {

            int initY = PresentationZone.this.getHeight();

            @Override
            public void touch() {
                rst(false, false, false);
            }

            @Override
            public void touchUp(Touch touch) {
                SMT.remove(PresentationZone.this);
            }
        };

        //add share button

        int size = 100;

        int xa = this.getWidth() - (size * 2);
        ShareButton shareButton = new ShareButton("addme", 45, 27);
        shareButton.initButton();
        shareButton.setPosterItemUuid(posterItemUuid);
        shareButton.setVisible(true);



        this.add(imageZone);
        this.add(shareButton);
        shareButton.translate(xa, 50);
        //imageZone.putChildOnTop(shareButton);
    }



    public void showDialog(final String text, int alpha) {


        int num_per_col = 2;
        int reminder = 2 % num_per_col;

        int rows = ((2 - reminder) / 2) + reminder;

        int total_width = (num_per_col * 25) + (num_per_col * DIALOG_WIDTH) + 25;
        int total_height = (rows * 25) + (rows * DIALOG_WIDTH) + 25;

        int x2 = (this.getWidth() / 2) - (total_width / 2);
        int y2 = (this.getHeight() / 2) - (total_height / 2);


        final int heading_height = 50;

        Zone heading = new Zone("heading", x2, y2, total_width, heading_height + total_height) {
            @Override
            public void draw() {
                stroke(97, 97, 97);
                strokeWeight(3);
                fill(255);
                rect(0, 0, this.getWidth(), this.getHeight(), ZoneHelper.ROUND_CORNER);

                stroke(224, 224, 224);
                strokeWeight(3);
                fill(255);
                rect(1, 1, this.getWidth() - 2, this.getHeight() - 2, ZoneHelper.ROUND_CORNER);

                textFont(font, 20);
                textAlign(CENTER, CENTER);
                textSize(20);
                fill(0);
                text(text, getWidth() / 2 - 2, heading_height / 2);
            }

            @Override
            public void touch() {
                super.touch();
            }
        };

        Zone frame = new Zone("frame", 0, heading_height, total_width, total_height) {
            @Override
            public void draw() {
                stroke(255);
                strokeWeight(0);
                fill(255);
                rect(3, 0, getWidth() - 6, getHeight() - 3, ZoneHelper.ROUND_CORNER);
            }

            @Override
            public void touch() {
                super.touch();
            }
        };


        YesButton yesButton = new YesButton("Yes", "Yes", DIALOG_WIDTH, DIALOG_WIDTH, ZoneHelper.redOutline, 24) {
            @Override
            public void touchUp(Touch touch) {
                super.touchUp(touch);
                doYesAction();
            }
        };

        yesButton.initButton();

        YesButton noButton = new YesButton("No", "No", DIALOG_WIDTH, DIALOG_WIDTH, ZoneHelper.blueOutline, 24) {
            @Override
            public void touchUp(Touch touch) {
                super.touchUp(touch);
                doCancelAction();
            }
        };
        noButton.initButton();
        frame.add(noButton, yesButton);

        SMT.grid(25, 25, frame.getWidth(), 25, 25, frame.getChildren());


        heading.add(frame);
        this.add(heading);
        this.fade(3f, 0, alpha, false);
    }

    public void showOKDialog(final String text, int alpha) {
        okDialogText = text;
        int num_per_col = 1;
        int reminder = 2 % num_per_col;

        int rows = ((2 - reminder) / 2) + reminder;

        int total_width = (num_per_col * 25) + (num_per_col * DIALOG_WIDTH) + 25;
        int total_height = (rows * 25) + (rows * DIALOG_WIDTH) + 25;

        int x2 = (this.getWidth() / 2) - (total_width / 2);
        int y2 = (this.getHeight() / 2) - (total_height / 2);


        final int heading_height = 50;

        Zone heading = new Zone("heading", x2, y2, total_width, heading_height + total_height) {
            @Override
            public void draw() {
                stroke(97, 97, 97);
                strokeWeight(3);
                fill(255);
                rect(0, 0, this.getWidth(), this.getHeight(), ZoneHelper.ROUND_CORNER);

                stroke(224, 224, 224);
                strokeWeight(3);
                fill(255);
                rect(1, 1, this.getWidth() - 2, this.getHeight() - 2, ZoneHelper.ROUND_CORNER);

                textFont(font, 20);
                textAlign(CENTER, CENTER);
                textSize(20);
                fill(0);
                text(okDialogText, getWidth() / 2 - 2, heading_height / 2);
            }

            @Override
            public void touch() {
                super.touch();
            }
        };

        Zone frame = new Zone("frame", 0, heading_height, total_width, total_height) {
            @Override
            public void draw() {
                stroke(255);
                strokeWeight(0);
                fill(255);
                rect(3, 0, getWidth() - 6, getHeight() - 3, ZoneHelper.ROUND_CORNER);
            }

            @Override
            public void touch() {
                super.touch();
            }
        };

        YesButton cancelButton = new YesButton("OK", "OK", DIALOG_WIDTH, DIALOG_WIDTH, ZoneHelper.blueOutline, 24) {
            @Override
            public void touchUp(Touch touch) {
                super.touchUp(touch);
                doCancelAction();
            }
        };
        cancelButton.initButton();
//        if(shouldShowButton) {
//            frame.add(cancelButton);
//        }

        //SMT.grid(25, 25, frame.getWidth(), 25, 25, frame.getChildren());

        //heading.add(frame);
        this.add(heading);
        this.fade(3f, 0, alpha, false);
    }

    public void doYesAction() {

    }

    public void doCancelAction() {
        SMT.remove(this.getName());
    }


    public void fade(float speed, float delay, int alpha, boolean shouldDelete) {
        this.shouldDelete = shouldDelete;


        Ani diameterAni = new Ani(this, speed, delay, "bgAlpha", alpha, Ani.LINEAR, "onEnd:done");
        diameterAni.start();
    }

    public void done() {
        if (shouldDelete) {
            System.out.println("PresentationZone.done DELETE");
            SMT.remove(this.getName());
        } else {
            System.out.println("PresentationZone.done NO DELETE");
        }
    }

    public void delete() {
        for (Zone zone : this.getChildren()) {
            this.remove(zone);
        }
        SMT.remove(this);
    }

    public void setBgAlpha(int bgAlpha) {
        this.bgAlpha = bgAlpha;
    }

    public void setOkDialogText(String okDialogText) {
        this.okDialogText = okDialogText;
    }
}
