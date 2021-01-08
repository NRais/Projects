package org.lwjgljava.game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import org.lwjgljava.engine.IGameLogic;
import org.lwjgljava.engine.Window;

public class DummyGame implements IGameLogic {

    private int direction = 0;
    private int otherDirection = 0;

    private float color = 0.0f;
    private float otherColor = 0.0f;

    private final Renderer renderer;

    public DummyGame() {
        renderer = new Renderer();
    }

    @Override
    public void init() throws Exception {
        renderer.init();
    }

    @Override
    public void input(Window window) {
        if ( window.isKeyPressed(GLFW_KEY_UP) ) {
            direction = 1;
        } else if ( window.isKeyPressed(GLFW_KEY_DOWN) ) {
            direction = -1;
        } else if ( window.isKeyPressed(GLFW_KEY_RIGHT) ) {
            otherDirection = 1;
        } else if ( window.isKeyPressed(GLFW_KEY_LEFT) ) {
            otherDirection = -1;
        } else {
            direction = 0;
            otherDirection = 0;
        }
    }

    /**
     * This method acts every so often updating the game based upon its state
     * @param interval
     */
    @Override
    public void update(float interval) {
        color += direction * 0.01f;
        otherColor += otherDirection * 0.01f;

        // check for boundary cases
        if (color > 1) {
            color = 1.0f;
        } else if ( color < 0 ) {
            color = 0.0f;
        }
        if (otherColor > 1) {
            otherColor = 1.0f;
        } else if ( otherColor < 0 ) {
            otherColor = 0.0f;
        }
    }

    @Override
    public void render(Window window) {
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        window.setClearColor(color, otherColor, color, 0.0f);
        renderer.clear();
    }
}
