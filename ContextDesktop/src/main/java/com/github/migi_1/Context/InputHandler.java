package com.github.migi_1.Context;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;

import jmevr.app.VRApplication;

public final class InputHandler {

    private String[] actions = {"exit", "cam_switch", "forwards", "backwards", "left", "right", "up", "down", "steer_left", "steer_right"};
    private int[] keyInputs = {KeyInput.KEY_ESCAPE, KeyInput.KEY_C, KeyInput.KEY_W, KeyInput.KEY_S, KeyInput.KEY_A, KeyInput.KEY_D,
                               KeyInput.KEY_SPACE, KeyInput.KEY_LSHIFT, KeyInput.KEY_LEFT, KeyInput.KEY_RIGHT};
    private boolean forwards, back, left, right, up, down = false;
    private Main main;

    public InputHandler(Main main) {
        this.main = main;
    }

    /**
     * Initialises the game inputs (keys).
     *
     * Key bindings:
     * Escape key: Exit the game
     * c: switches camera
     * When in flycam:
     * w: move forwards
     * s: move backwars
     * a: move left
     * d: move right
     * Lshift: move down
     * space: move up
     * ---MORE CAN BE ADDED IF NEEDED---
     */
    public void initInputs(Main main) {
        InputManager inputManager = main.getInputManager();
        addMappings(inputManager);
        ActionListener acl = new ActionListener() {

            @Override
            public void onAction(String name, boolean keyPressed, float tpf) {
                if (name.equals("exit") && keyPressed) {
                    System.exit(0);
                } else if (name.equals("cam_switch") && keyPressed) {
                    main.getEnv().swapCamera();
                }

                //Controls that only work with flycam.
                if (main.getEnv().getFlyCamActive()) {
                    switch (name) {
                        case "forwards":
                            forwards = keyPressed;
                            break;
                        case "backwards":
                            back = keyPressed;
                            break;
                        case "left":
                            left = keyPressed;
                            break;
                        case "right":
                            right = keyPressed;
                            break;
                        case "up":
                            up = keyPressed;
                            break;
                        case "down":
                            down = keyPressed;
                            break;
                        default: //Do nothing when an unknown button is pressed.
                    }
                }
                checkSteering(name, keyPressed);

            }

        };
        addListeners(inputManager, acl);
    }

    /**
     * Adds all the mappings for the different function names to the different keys.
     * @param inputManager the inputmanager for which these keymappings are set.
     */
    private void addMappings(InputManager inputManager) {
        for(int i = 0; i < actions.length; i++) {
            inputManager.addMapping(actions[i], new KeyTrigger(keyInputs[i]));
        }
    }

    private void addListeners(InputManager inputManager, ActionListener acl) {
        for(String action : actions) {
            inputManager.addListener(acl, action);
        }
    }

    /**
     * Key binding for steering.
     * left: move left
     * right: move right
     * @param name Name of key action.
     * @param keyPressed True when pressed, false when released.
     */
    private void checkSteering(String name, boolean keyPressed) {
        if (keyPressed) {
            if (name.equals("steer_left")) {
                main.getEnv().steer(-1.f);
            }
            else if (name.equals("steer_right")) {
               main.getEnv().steer(1.f);
           }
       }
       if (!keyPressed && (name.equals("steer_left") || name.equals("steer_right"))) {
           main.getEnv().steer(0.f);
       }
    }

    public void moveCamera(float tpf) {
        if (forwards) {
            System.out.println(VRApplication.getFinalObserverRotation().getRotationColumn(2).mult(tpf * 8f));
            main.getEnv().moveCam(VRApplication.getFinalObserverRotation().getRotationColumn(2).mult(tpf * 8f));
        }
        if (back) {
            main.getEnv().moveCam(VRApplication.getFinalObserverRotation().getRotationColumn(2).mult(-tpf * 8f));
        }
        if (left) {
            main.getEnv().rotateCam(new Vector3f(0f, 0.75f * tpf, 0f));
        }
        if (right) {
            main.getEnv().rotateCam(new Vector3f(0, -0.75f * tpf, 0));
        }
        if (up) {
            main.getEnv().moveCam(VRApplication.getFinalObserverRotation().getRotationColumn(1).mult(tpf * 8f));
        }
        if (down) {
            main.getEnv().moveCam(VRApplication.getFinalObserverRotation().getRotationColumn(1).mult(-tpf * 8f));
        }
    }
}
