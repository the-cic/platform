package com.cic.platform;

import com.cic.platform.scene.Scene;
import com.cic.platform.map.BitmapObstacleMap;
import com.cic.platform.scene.AnimatedSprite;
import com.cic.platform.mob.GameCharacter;
import com.cic.platform.mob.CharacterDepiction;
import com.cic.platform.mob.FrameSequences;
import com.cic.platform.scene.Sprite;
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

    private GameCharacter guy;
    private Sprite guySprite;
    private AnimatedSprite testSprite;
    private Node mapNode;
    private Scene scene;

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

        inputManager.addMapping("MoveDown", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addListener(actionListener, "MoveDown");

        inputManager.addMapping("Run", new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputManager.addListener(actionListener, "Run");


        viewPort.setBackgroundColor(new ColorRGBA(0.3f, 0.5f, 0.8f, 1.0f));

        makeScene();
        
        guySprite = new Sprite(assetManager, "Textures/sprites.png", 8, 10, 10);
        CharacterDepiction cd = new CharacterDepiction(guySprite);
        cd.addFrameSequence("stop:", FrameSequences.stand);
        cd.addFrameSequence("stop:L", FrameSequences.stand);
        cd.addFrameSequence("stop:R", FrameSequences.stand);
        cd.addFrameSequence("walk:L", FrameSequences.walkRight);
        cd.addFrameSequence("walk:R", FrameSequences.walkRight);
        cd.addFrameSequence("run:L", FrameSequences.runRight);
        cd.addFrameSequence("run:R", FrameSequences.runRight);
        cd.addFrameSequence("jump:L", FrameSequences.jumpRight);
        cd.addFrameSequence("jump:R", FrameSequences.jumpRight);
        
        guy = new GameCharacter();
        guy.setDepiction(cd);

        BitmapObstacleMap map = new BitmapObstacleMap(assetManager.loadTexture("Textures/map.png").getImage());
        
        scene.setMap(map);
        
        guy.stop();
        guy.setPosition(map.getWidth() / 2, map.getHeight() / 2);

        scene.addCharacter(guy);
        
        /**/
        Sprite guy2Sprite = new Sprite(assetManager, "Textures/sprites.png", 8, 10, 10);
        cd = new CharacterDepiction(guy2Sprite);
        cd.addFrameSequence("stop:", FrameSequences.stand);
        cd.addFrameSequence("stop:L", FrameSequences.stand);
        cd.addFrameSequence("stop:R", FrameSequences.stand);
        cd.addFrameSequence("walk:L", FrameSequences.walkRight);
        cd.addFrameSequence("walk:R", FrameSequences.walkRight);
        cd.addFrameSequence("run:L", FrameSequences.runRight);
        cd.addFrameSequence("run:R", FrameSequences.runRight);
        cd.addFrameSequence("jump:L", FrameSequences.jumpRight);
        cd.addFrameSequence("jump:R", FrameSequences.jumpRight);
        
        GameCharacter guy2 = new GameCharacter();
        guy2.setDepiction(cd);
        guy2.jump(true);
        guy2.setPosition(map.getWidth() / 2, map.getHeight() / 2);

        scene.addCharacter(guy2);
        
    }

    private void makeScene() {
        scene = new Scene();

        Node allNode = scene.getNode(); //new Node("AllNode");

        Quad q = new Quad(200,100);
        Geometry g = new Geometry("lalala", q);
        mapNode = new Node("map");
        mapNode.attachChild(g);

        Texture mapTexture = assetManager.loadTexture("Textures/map.png");

        /*
        Image image = mapTexture.getImage();
        ByteBuffer data=image.getData(0);
        data.rewind();
        System.out.println(image);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                //data.put((byte) 255);
                data.put((byte) (Math.random() * 255));
                data.put((byte) (Math.random() * 255));
                data.put((byte) (Math.random() * 255));
            }
        }
        image.setUpdateNeeded();
        mapTexture.setImage(image);
        */

        Material mapMat = new Material(assetManager, "Common/MatDefs/Misc/ColoredTextured.j3md");
        mapMat.setTexture("ColorMap", mapTexture);

        mapNode.setMaterial(mapMat);
        //mapNode.scale(1/5f);
        mapNode.setLocalTranslation(0, 0, -1f);

        allNode.attachChild(mapNode);

        allNode.scale(0.05f);
        allNode.setLocalTranslation(-0.05f*100/1f, -0.05f*50/1f, 0);

        testSprite = new AnimatedSprite(assetManager, "Textures/sprites.png", 8, 10, 10);
        scene.addSprite(testSprite);
        
        rootNode.attachChild(allNode);
    }

    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            //log.info(name+" "+keyPressed);

            int direction = 0;

            if (name.equals("MoveLeft")) {
                keysDown.put("left", keyPressed);
            }
            if (name.equals("MoveRight")) {
                keysDown.put("right", keyPressed);
            }
            if (name.equals("MoveUp")) {
                keysDown.put("up", keyPressed);
            }
            if (name.equals("MoveDown")) {
                keysDown.put("down", keyPressed);
                testSprite.loop(30);
            }
            if (name.equals("Run")) {
                keysDown.put("run", keyPressed);
                testSprite.start(30);
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
        }
    };

    @Override
    public void simpleUpdate(float tpf) {
        if (guy != null) {
            notifications.put("a", "i:" + guy.depiction.getFrameSequence().frameIndex);
            notifications.put("b", "d:" + guy.depiction.getFrameSequence().frameDuration);
            notifications.put("c", "int:" + guy.depiction.getFrameSequence().canBeInterrupted);

            notifications.put("g0", "sqc:" + guy.depiction.currentSequenceKey);
            notifications.put("g1", "sqn:" + guy.depiction.nextSequenceKey);

            //notifications.put("h1", "st:" + guy.walking);
            notifications.put("h2", "di:" + guy.direction);
        }

        notifications.put("p", "" + keysDown.toString());

        notifications.update();

        scene.update(tpf);
    }
}
