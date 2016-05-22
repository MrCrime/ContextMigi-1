package com.github.migi_1.Context.model;

import java.util.LinkedList;
import java.util.Random;

import jmevr.app.VRApplication;
import obstacles.DamageDealer;
import obstacles.DamageDealerGenerator;

import com.github.migi_1.Context.Main;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;

/**
 * The Environment class handles all visual aspects of the world, excluding the characters and enemies etc.
 * @author Damian
 */
public class Environment extends AbstractAppState {
    private Main app;
    private ViewPort viewPort;
    private AssetManager assetManager;
    private Node rootNode;

    private Spatial vrObs;
    private Spatial flyObs;

    private static final ColorRGBA BACKGROUNDCOLOR = ColorRGBA.Blue;
    private static final Vector3f SUNVECTOR = new Vector3f(-.5f, -.5f, -.5f);
    private static final Vector3f SUNVECTOR_2 = new Vector3f(0, -1f, -.2f);

    private static final int SHADOWMAP_SIZE = 1024;
    private static final int SHADOW_SPLITS = 3;

    private static final Vector3f WORLD_LOCATION = new Vector3f(0, -20, 0);

    private static final Vector3f PLATFORM_LOCATION = new Vector3f(20, -18, -1);
    private static final Vector3f COMMANDER_LOCATION = new Vector3f(23, -14, -1f);

    private static final float COMMANDER_ROTATION = -1.5f;

    private static final float PLATFORM_SPEED = 0.2f;

    private static final int LEVEL_PIECES = 5;
    private static final int DIFFERENT_WORLDS = 5;

    private static final float STEERING_ANGLE = (float) (Math.sqrt(2.f) / 2.f);

    private Spatial testPlatform;
    private LinkedList<Spatial> testWorld;
    private Spatial testCommander;

    private DirectionalLight sun;
    private DirectionalLight sun2;

    private float steering;


    private CollisionResults results;

    private float decay;

    private DamageDealerGenerator obstacleGenerator;

    /**
     * First method that is called after the state has been created.
     * Handles all initialization of parameters needed for the Environment.
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        super.initialize(stateManager, app);
        this.app = (Main) app;
        this.testWorld = new LinkedList<Spatial>();
        assetManager = app.getAssetManager();
        viewPort = app.getViewPort();
        vrObs = new Node("VR");
        flyObs = new Node("FLY");
        rootNode = this.app.getRootNode();
        steering = 0.f;
        results = new CollisionResults();
        decay = 1;
        obstacleGenerator = DamageDealerGenerator.getInstance(this);
        //deprecated method, it does however makse it possible to load assets from a non default location
        assetManager.registerLocator("assets", FileLocator.class);

        viewPort.setBackgroundColor(BACKGROUNDCOLOR);

        //creates the lights
        initLights();

        //creates shadowmap and attach it to the sun
        initShadows();

        //initializes all spatials and set the positions
        initSpatials();

        //Init the camera
        initCameras();
    }

    /**
     * Update the entities and cameras.
     */
    @Override
    public void update(float tpf) {

        super.update(tpf);
        Vector3f loc = testCommander.getLocalTranslation();
        float xAxis = 1;
        float zAxis = 0;
        if (loc.z > 5f) {
            zAxis = 0.5f;
        } else if (loc.z < -5f) {
            zAxis = -0.5f;
        } else {
            zAxis = steering * STEERING_ANGLE;
            xAxis = (float) Math.sqrt(1 - Math.pow(zAxis, 2));
        }

        float adjustedSpeed = -PLATFORM_SPEED * decay;
        testPlatform.move(adjustedSpeed * xAxis, 0, adjustedSpeed * zAxis);
        testCommander.move(adjustedSpeed * xAxis, 0, adjustedSpeed * zAxis);
        vrObs.setLocalTranslation(testCommander.getLocalTranslation());
        updateTestWorld();

        for (DamageDealer obs : obstacleGenerator.getObstacles().values()){
            obs.collideWith(testPlatform, results);
        }

        // regain speed
        if (decay < 1.0f){
            decay = decay + 0.01f;
        }
        if(results.size() > 0){
            System.out.println(results.size());
//            rootNode.detachChild(obstacleGenerator.getObstacles().poll().getModel());
            rootNode.detachChild(results.getClosestCollision().getGeometry());
            obstacleGenerator.getObstacles().remove(results.getClosestCollision().getGeometry());
            results = new CollisionResults();
            decay = 0.0f;
        }
    }

    /**
     * Initializes all lights of the scene.
     */
    private void initLights() {
        sun = new DirectionalLight();
        sun2 = new DirectionalLight();

        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(SUNVECTOR).normalizeLocal());

        sun2.setColor(ColorRGBA.White);
        sun2.setDirection(new Vector3f(SUNVECTOR_2).normalizeLocal());

        //adds the lights to the root pane
        rootNode.addLight(sun);
        rootNode.addLight(sun2);
    }

    /**
     * Initializes all shadows of the scene.
     */
    private void initShadows() {
        DirectionalLightShadowRenderer dlsr =
        		new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, SHADOW_SPLITS);
        dlsr.setLight(sun);
        viewPort.addProcessor(dlsr);

        //adds shadow filter and attaches it to the viewport
        DirectionalLightShadowFilter dlsf =
        		new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, SHADOW_SPLITS);
        dlsf.setLight(sun);
        dlsf.setEnabled(true);
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(dlsf);
        viewPort.addProcessor(fpp);
    }

    /**
     * Initializes all objects and translations/rotations of the scene.
     */
    private void initSpatials() {
        //initialize the given number of level pieces
        while (testWorld.size() < LEVEL_PIECES) {
            Spatial levelPiece = chooseLevelPiece(new Random().nextInt(DIFFERENT_WORLDS));
            levelPiece.move(WORLD_LOCATION.setX(WORLD_LOCATION.x));
            testWorld.add(levelPiece);
            BoundingBox bb = (BoundingBox) levelPiece.getWorldBound();

            //shift orientation to where the next level piece should spawn
            WORLD_LOCATION.x -= 2 * bb.getXExtent() - 2.0f;
        }

        testPlatform = assetManager.loadModel("Models/testPlatform.j3o");
        testPlatform.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        testPlatform.move(PLATFORM_LOCATION);
        testCommander = assetManager.loadModel("Models/ninja.j3o");
        testCommander.rotate(0, COMMANDER_ROTATION, 0);
        testCommander.move(COMMANDER_LOCATION);
        testCommander.addControl(new RigidBodyControl());
        for (DamageDealer obs : obstacleGenerator.getObstacles().values()) {
            rootNode.attachChild(obs.getModel());
        }
        //attach all objects to the root pane
        for (Spatial sp : testWorld) {
            rootNode.attachChild(sp);
        }

        rootNode.attachChild(testPlatform);
        rootNode.attachChild(testCommander);
    }

    /**
     * Initializes the cameras and sets its location and rotation.
     * Starts with the VR camera.
     */
    private void initCameras() {
        vrObs.setLocalTranslation(new Vector3f(0f, 0f, 0f));
        vrObs.rotate(0f, COMMANDER_ROTATION, 0f);
        flyObs.setLocalTranslation(new Vector3f(-12f, 0f, -16f));
        flyObs.setLocalRotation(new Quaternion(0f, 0f, 0f, 1f));

        VRApplication.setObserver(vrObs);
        rootNode.attachChild(vrObs);
        rootNode.attachChild(flyObs);
    }

    /**
     * Renders the entities.
     * @param rm manager of the renderengine
     */
    @Override
    public void render(RenderManager rm) { }

    /**
     * Moves the flycam.
     * @param move a vector representation of the movement of the flyCamera.
     */
    public void moveCam(Vector3f move) {
        flyObs.move(move);
    }

    /**
     * Rotates the flycam.
     * @param x rotation value on the x-axis
     * @param y rotation value on the y-axis
     * @param z rotation value on the z-axis
     */
    public void rotateCam(float x, float y, float z) {
        flyObs.rotate(x, y, z);
    }

    /**
     * Returns the current camera (which is a observer).
     * @return A string representation of the current camera:
     * "VR (Node)" or "FLY (Node)".
     */
    public String getCamera() {
        return VRApplication.getObserver().toString();
    }

    /**
     * Swap the cameras in the environment.
     * VRCam <-> FlyCam.
     */
    public void swapCamera() {
        Spatial obs = (Spatial) VRApplication.getObserver();
        if (obs.getName().equals("VR")) {
            VRApplication.setObserver(flyObs);
        } else if (obs.getName().equals("FLY")) {
            VRApplication.setObserver(vrObs);
        }
    }

    /**
     * Updates the test world.
     */
    private void updateTestWorld() {

        //delete level piece when it too far back
        if (testWorld.size() > 0) {
             Spatial check = testWorld.peek();
             BoundingBox bb1 = (BoundingBox) check.getWorldBound();
             BoundingBox bb2 = (BoundingBox) this.testCommander.getWorldBound();
             Vector2f v1 = new Vector2f(bb1.getCenter().x, bb1.getCenter().y);
             Vector2f v2 = new Vector2f(bb2.getCenter().x, bb2.getCenter().y);
             if (v1.distance(v2) > 100) {
                testWorld.poll();
                rootNode.detachChild(check);
             }
        }

        //add level pieces until the number of level pieces is correct
        while (testWorld.size() < LEVEL_PIECES) {
            Spatial levelPiece = chooseLevelPiece(new Random().nextInt(DIFFERENT_WORLDS));
            levelPiece.move(WORLD_LOCATION.setX(WORLD_LOCATION.getX() + 0.2f));
            testWorld.add(levelPiece);
            BoundingBox bb = (BoundingBox) levelPiece.getWorldBound();
            WORLD_LOCATION.x -= 2 * bb.getXExtent() - 2.0f;
            rootNode.attachChild(levelPiece);
        }

    }

    public Vector3f getCommanderLocation() {
        return COMMANDER_LOCATION;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    /**
     * Update the steering direction.
     * @param orientation This translates to the steering ange.
     */
    public void steer(float orientation) {
        steering = orientation;
    }

    /**
     * Handles everything that happens when the Envrironment state is detached from the main application.
     */
    @Override
    public void cleanup() {
        super.cleanup();
    }

    ////////////////////////////////////Below methods might be used later on when the pause screen is introduced.
    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean isInitialized() {
        return false;
    }

    @Override
    public void setEnabled(boolean arg0) { }

    /**
     * Chooses a levelPiece from the randomly generated number rnd.
     * @param rnd the random variable created by Random.nextInt(the amount of different worlds)
     */
    private Spatial chooseLevelPiece(int rnd) {
        StringBuilder sb = new StringBuilder();
        sb.append("Models/world");
        sb.append(rnd + 1);
        sb.append(".j3o");
        return assetManager.loadModel(sb.toString());
    }
}
