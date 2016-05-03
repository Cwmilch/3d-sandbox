package entities;

import engine.Keyboard;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Carter Milch on 4/29/2016.
 */
public class Camera {

    private Vector3f position = new Vector3f(0, 1, 0);
    private float pitch;
    private float yaw;
    private float roll;
    /*private Keyboard keyboard;

    public Camera(){
        keyboard = GameLoopMain.getDisplayManager().getKeyboard();
    }*/

    public void move(){
        if(Keyboard.isKeyDown(GLFW_KEY_W)){
            position.z -= 0.1f;
        }
        if(Keyboard.isKeyDown(GLFW_KEY_S)){
            position.z += 0.1f;
        }
        if(Keyboard.isKeyDown(GLFW_KEY_A)){
            position.x -= 0.04f;
        }
        if(Keyboard.isKeyDown(GLFW_KEY_D)){
            position.x += 0.04f;
        }
        if(Keyboard.isKeyDown(GLFW_KEY_LEFT_SHIFT)){
            position.y -= 0.04f;
        }
        if(Keyboard.isKeyDown(GLFW_KEY_SPACE)){
            position.y += 0.04f;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
