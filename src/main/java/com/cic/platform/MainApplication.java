package com.cic.platform;

import com.cic.platform.util.CommonMaterials;
import com.cic.platform.scene.Scene;
import com.cic.platform.map.BitmapObstacleMap;
import com.cic.platform.mob.GameCharacter;
import com.cic.platform.mob.CharacterDepiction;
import com.cic.platform.mob.FrameSequences;
import com.cic.platform.scene.SceneView;
import com.cic.platform.scene.Sprite;
import com.cic.platform.util.Transition;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import java.util.HashMap;
import java.util.logging.Logger;

public class MainApplication extends SimpleApplication {

    private static final Logger log = Logger.getLogger(MainApplication.class.getName());

    private NotificationsPanel notifications;

    private GameCharacter guy;
    private Sprite guySprite;
    //private AnimatedSprite testSprite;
    private Scene scene;
    private SceneView sceneView;
    private Transition viewTransition = null;

    private HashMap<String, Boolean> keysDown = new HashMap<String, Boolean>();
    private HashMap<String, Long> keysLastDown = new HashMap<String, Long>();

    public static void main(String[] args) {
        //JinputDriverExtractor.extract();

        MainApplication app = new MainApplication();

        app.setShowSettings(false);
        AppSettings mySettings = new AppSettings(true);
        mySettings.setVSync(true);
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
        cam.setParallelProjection(true);

        CommonMaterials.create(assetManager);

        notifications = new NotificationsPanel(assetManager);
        notifications.getNode().setLocalTranslation(0, cam.getHeight(), 0);
        guiNode.attachChild(notifications.getNode());


        /* Bind space to reset simulation */

        keysDown.put("left", false);
        keysDown.put("right", false);
        keysDown.put("up", false);
        keysDown.put("down", false);
        keysDown.put("run", false);
        keysDown.put("slow", false);
        keysLastDown.put("left", 0l);
        keysLastDown.put("right", 0l);

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

        inputManager.addMapping("Slow", new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputManager.addListener(actionListener, "Slow");


        viewPort.setBackgroundColor(new ColorRGBA(0.3f, 0.5f, 0.8f, 1.0f));

        makeScene();

        guySprite = new Sprite(assetManager, "Textures/sprites.png", 8, 2, 2);
        CharacterDepiction cd = new CharacterDepiction(guySprite, -1f, 0);
        cd.addFrameSequence("stop:", FrameSequences.stand);
        cd.addFrameSequence("stop:L", FrameSequences.stand);
        cd.addFrameSequence("stop:R", FrameSequences.stand);
        cd.addFrameSequence("walk:L", FrameSequences.walkLeft);
        cd.addFrameSequence("walk:R", FrameSequences.walkLeft);
        cd.addFrameSequence("sneak:L", FrameSequences.sneakLeft);
        cd.addFrameSequence("sneak:R", FrameSequences.sneakLeft);
        cd.addFrameSequence("run:L", FrameSequences.runRight);
        cd.addFrameSequence("run:R", FrameSequences.runRight);
        cd.addFrameSequence("jump:L", FrameSequences.jumpRight);
        cd.addFrameSequence("jump:R", FrameSequences.jumpRight);
        cd.addFrameSequence("crouch:", FrameSequences.crouch);
        cd.addFrameSequence("crouch:L", FrameSequences.crouch);
        cd.addFrameSequence("crouch:R", FrameSequences.crouch);

        guy = new GameCharacter(0.9f, 1.8f);
        guy.setDepiction(cd);
        guy.depiction.addAnchorBox(assetManager);

        guy.setStop();
        resetGuyPosition();

        scene.addCharacter(guy);

        sceneView = new SceneView(cam, scene, 16f/19f);
        sceneView.setSceneViewWidth(30);
        rootNode.attachChild(sceneView.getNode());

        /** /
        Sprite guy2Sprite = new Sprite(assetManager, "Textures/sprites.png", 8, 2, 2);
        cd = new CharacterDepiction(guy2Sprite, -1, 0);
        cd.addFrameSequence("stop:", FrameSequences.stand);
        cd.addFrameSequence("stop:L", FrameSequences.stand);
        cd.addFrameSequence("stop:R", FrameSequences.stand);
        cd.addFrameSequence("walk:L", FrameSequences.walkRight);
        cd.addFrameSequence("walk:R", FrameSequences.walkRight);
        cd.addFrameSequence("run:L", FrameSequences.runRight);
        cd.addFrameSequence("run:R", FrameSequences.runRight);
        cd.addFrameSequence("jump:L", FrameSequences.jumpRight);
        cd.addFrameSequence("jump:R", FrameSequences.jumpRight);

        GameCharacter guy2 = new GameCharacter(1, 2);
        guy2.setDepiction(cd);
        guy2.setJump(true);
        guy2.setPosition(map.getWidth()*1.1f / 2, map.getHeight() / 2);
        guy2.depiction.addAnchorBox(assetManager);

        scene.addCharacter(guy2);*/
        /*
        System.out.println(cam);
        System.out.println(cam.getViewMatrix());
        System.out.println(cam.getViewProjectionMatrix());
        System.out.println(cam.getViewPortLeft() + " " + cam.getViewPortRight()+ ", " + cam.getViewPortTop()+ " " + cam.getViewPortBottom());
        System.out.println(cam.getFrustumLeft() + " " + cam.getFrustumRight() + ", " + cam.getFrustumTop()+ " " + cam.getFrustumBottom());
        */
    }

    private void makeScene() {
        scene = new Scene();

        BitmapObstacleMap map = new BitmapObstacleMap(assetManager.loadTexture("Textures/map-small.png").getImage());

        scene.setMap(map);

        Node allNode = scene.getNode();

        Texture mapTexture = assetManager.loadTexture("Textures/map-small.png");
        mapTexture.setMagFilter(Texture.MagFilter.Nearest);

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

        scene.makeBackgroundPlate(mapMat);

        //testSprite = new AnimatedSprite(assetManager, "Textures/sprites.png", 8, 10, 8);
        //scene.addSprite(testSprite);

        rootNode.attachChild(allNode);
    }

    private void resetGuyPosition(){
        guy.setPosition(10, 10);
    }

    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("ResetPosition")) {
                resetGuyPosition();
            }

            int direction = 0;
            long now = System.currentTimeMillis();

            if (name.equals("MoveLeft")) {
                keysDown.put("left", keyPressed);
                if (keyPressed) {
                    if (now - keysLastDown.get("left") < 200) {
                        keysDown.put("run", true);
                    }
                    keysLastDown.put("left", now);
                }
            }
            if (name.equals("MoveRight")) {
                keysDown.put("right", keyPressed);
                if (keyPressed) {
                    if (now - keysLastDown.get("right") < 200) {
                        keysDown.put("run", true);
                    }
                    keysLastDown.put("right", now);
                }
            }
            if (name.equals("MoveUp")) {
                keysDown.put("up", keyPressed);
            }
            if (name.equals("MoveDown")) {
                keysDown.put("down", keyPressed);
            }
            if (name.equals("Slow")) {
                keysDown.put("slow", keyPressed);
                keysDown.put("run", false);
            }

            // exclude both left and right
            if (keysDown.get("left") && !keysDown.get("right")) {
                direction = -1;
            } else if (!keysDown.get("left") && keysDown.get("right")) {
                direction = 1;
            }

            if (direction == 0) {
                keysDown.put("run", false);
            }

            boolean run = keysDown.get("run") && !keysDown.get("slow");
            boolean slow = keysDown.get("slow");
            boolean jump = keysDown.get("up");
            boolean crouch = keysDown.get("down");

            if (direction == 0) {
                guy.setStop();
                guy.setJump(jump);
                guy.setCrouch(crouch);
            } else {
                if (direction > 0) {
                    guy.setLookRight();
                } else {
                    guy.setLookLeft();
                }
                if (run) {
                    guy.setRun();
                } else if (slow) {
                    guy.setSneak();
                } else {
                    guy.setWalk();
                }
                guy.setJump(jump);
                guy.setCrouch(crouch);
            }
        }
    };

    @Override
    public void simpleUpdate(float tpf) {
        if (guy != null) {
            notifications.put("a", "sq   :" + guy.depiction.currentSequenceKey);
            notifications.put("b", "f.i  :" + guy.depiction.getFrameSequence().frameIndex);
            notifications.put("c", "f.d  :" + guy.depiction.getFrameSequence().frameDuration);
            notifications.put("d", "f.int:" + guy.depiction.getFrameSequence().canBeInterrupted);

            notifications.put("h2", "g.d :" + guy.direction);
        }

        notifications.put("p", "keys: " + keysDown.toString());

        notifications.update();

        // Compensate for longer frames
        // If slower than half fps do multiple micro updates

        final float targetTpf = 0.01667f;
        if (tpf <= targetTpf * 2) {
            scene.update(tpf);
        } else {
            int frames = (int)(tpf / targetTpf); // more than 2
            float subTpf = tpf / frames;
            for (int i = 0; i < frames; i++) {
                scene.update(subTpf);
            }
        }

        // Follow player's position and move sceneView
        
        float guyPercent = sceneView.getPositionSceneViewPercent(guy.xPos);

        float framePerc = sceneView.getFramePercent(guy.xPos);
        if (framePerc < 0.2 || framePerc > 0.8) {
            if (viewTransition == null || viewTransition.isFinished()) {
                viewTransition = new Transition(sceneView.getSceneViewPercent(), guyPercent, 5f, 0.1f, 0.8f);
            }
        }
        if (viewTransition != null && !viewTransition.isFinished()) {
            viewTransition.update(tpf);
            sceneView.setSceneViewPercent(viewTransition.getValue());
        }
    }
}
