package com.cic.platform;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import java.util.HashMap;
import java.util.logging.Logger;

public class MainApplication extends SimpleApplication {

    private static final Logger log = Logger.getLogger(MainApplication.class.getName());

    private NotificationsPanel notifications;

    private Character guy = new Character();
    private Node guyNode;
    private Node mapNode;
    Material mat;

    private HashMap<String, Boolean> keysDown = new HashMap<String, Boolean>();

    public static void main(String[] args) {
        //JinputDriverExtractor.extract();

        MainApplication app = new MainApplication();

        app.setShowSettings(false);
        AppSettings mySettings = new AppSettings(true);
        mySettings.setVSync(false);
        mySettings.setSamples(2);
        mySettings.setResolution(1024, 768);
        mySettings.setTitle("Platform");
        //mySettings.setSettingsDialogImage("Textures/splash.png");

        app.setSettings(mySettings);

        app.start();
    }

    public MainApplication() {
        super();

    }

    @Override
    public void simpleInitApp() {
        /* Setup camera */
        flyCam.setEnabled(false);

        notifications = new NotificationsPanel(assetManager);
        notifications.getNode().setLocalTranslation(0, cam.getHeight(), 0);
        guiNode.attachChild(notifications.getNode());


        /* Bind space to reset simulation */

        keysDown.put("left", false);
        keysDown.put("right", false);
        keysDown.put("up", false);
        keysDown.put("down", false);
        keysDown.put("run", false);

        inputManager.addMapping("ResetPosition", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(actionListener, "ResetPosition");

        inputManager.addMapping("MoveLeft", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addListener(actionListener, "MoveLeft");

        inputManager.addMapping("MoveRight", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addListener(actionListener, "MoveRight");

        inputManager.addMapping("MoveUp", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addListener(actionListener, "MoveUp");

        inputManager.addMapping("Run", new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputManager.addListener(actionListener, "Run");


        viewPort.setBackgroundColor(new ColorRGBA(0.3f, 0.5f, 0.8f, 1.0f));

        makeHud();


        CharacterDepiction cd = new CharacterDepiction();
        cd.addFrameSequence("stop:", FrameSequences.stand);
        cd.addFrameSequence("stop:L", FrameSequences.stand);
        cd.addFrameSequence("stop:R", FrameSequences.stand);
        cd.addFrameSequence("walk:L", FrameSequences.walkRight);
        cd.addFrameSequence("walk:R", FrameSequences.walkRight);
        cd.addFrameSequence("run:L", FrameSequences.runRight);
        cd.addFrameSequence("run:R", FrameSequences.runRight);
        cd.addFrameSequence("jump:L", FrameSequences.jumpRight);
        cd.addFrameSequence("jump:R", FrameSequences.jumpRight);

        guy.depiction = cd;
        cd.character = guy;
        guy.stop();

        //seq.start();
    }

    private void makeHud() {
        /*
        batteryIcon = new Picture("HUD Battery Icon");
        batteryIcon.setImage(assetManager, "Textures/battery-warning.png", true);

        batteryIcon.setWidth(6 * 7);
        batteryIcon.setHeight(10 * 7);
        batteryIcon.setPosition(settings.getWidth() - (6 * 7) - 10, 10);
        guiNode.attachChild(batteryIcon);    */
        
        Node allNode = new Node("AllNode");

        Quad q = new Quad(1,1);
        Geometry g = new Geometry("lalala", q);
        guyNode = new Node("guy");
        guyNode.attachChild(g);

        Texture spritesTexture = assetManager.loadTexture("Textures/sprites.png");
        
        //Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat = new Material(assetManager, "Materials/ColoredTexturedSprite.j3md");
        mat.setColor("Color", new ColorRGBA(108f / 255f, 117f / 255f, 67f / 255f, 1));
        mat.setTexture("ColorMap", spritesTexture);
        mat.setInt("Index", 1);

        guyNode.setMaterial(mat);

        allNode.attachChild(guyNode);
        
        q = new Quad(20,10);
        g = new Geometry("lalala", q);
        mapNode = new Node("map");
        mapNode.attachChild(g);
        
        Texture mapTexture = assetManager.loadTexture("Textures/map.png");
        Material mapMat = new Material(assetManager, "Common/MatDefs/Misc/ColoredTextured.j3md");
        mapMat.setTexture("ColorMap", mapTexture);
        
        
        
        mapNode.setMaterial(mapMat);
        mapNode.setLocalTranslation(-10, -5, -0.1f);
        
        allNode.attachChild(mapNode);
        
        allNode.scale(0.5f);
        
        rootNode.attachChild(allNode);
    }

    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            //log.info(name+" "+keyPressed);

            boolean wasRun = keysDown.get("run");
            boolean wasJump = keysDown.get("up");
            int direction = 0;
            boolean turn = false;

            boolean leftRight = false;
            if (name.equals("MoveLeft")) {
                keysDown.put("left", keyPressed);
                leftRight = true;
            }
            if (name.equals("MoveRight")) {
                keysDown.put("right", keyPressed);
                leftRight = true;
            }
            if (name.equals("MoveUp")) {
                keysDown.put("up", keyPressed);
            }
            if (name.equals("Run")) {
                keysDown.put("run", keyPressed);
            }

            if (keysDown.get("left") && !keysDown.get("right")) {
                direction = -1;
            } else
            if (!keysDown.get("left") && keysDown.get("right")) {
                direction = 1;
            }

            boolean run = keysDown.get("run");
            boolean jump = keysDown.get("up");

            if (direction == 0) {
                guy.stop();
                guy.jump(jump);
            } else {
                if (direction > 0) {
                    guy.lookRight();
                } else {
                    guy.lookLeft();
                }
                if (run) {
                    guy.run();
                } else {
                    guy.walk();
                }
                guy.jump(jump);
            }


            /*
            if (keysDown.get("up")){
                guy.jump();
            } else {
                if (leftRight) {
                    if (keysDown.get("left") && !keysDown.get("right")) {
                        guy.walkLeft();
                    } else
                    if (!keysDown.get("left") && keysDown.get("right")) {
                        guy.walkRight();
                    } else
                    if (!keysDown.get("left") && !keysDown.get("right")) {
                        guy.stop();
                    }
                }
            }*/
        }
    };

    @Override
    public void simpleUpdate(float tpf) {
        notifications.put("a", "i:" + guy.depiction.getFrameSequence().frameIndex);
        notifications.put("b", "d:" + guy.depiction.getFrameSequence().frameDuration);
        notifications.put("c", "int:" + guy.depiction.getFrameSequence().canBeInterrupted);

        notifications.put("g0", "sqc:" + guy.depiction.currentSequenceKey);
        notifications.put("g1", "sqn:" + guy.depiction.nextSequenceKey);

        //notifications.put("h1", "st:" + guy.walking);
        notifications.put("h2", "di:" + guy.direction);

        notifications.put("p", "" + keysDown.toString());

        notifications.update();
        
        mat.setInt("Index", guy.depiction.getFrameSequence().frameIndex);

        guy.update(tpf);
        guy.depiction.update(tpf);
        guyNode.setLocalTranslation(guy.xPos, guy.yPos, 0);
    }
}
