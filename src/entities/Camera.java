package entities;

import engine.DisplayManager;
import engine.Keyboard;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static engine.Main.*;

/**
 * Created by Carter Milch on 4/29/2016.
 */
@SuppressWarnings("Duplicates")
public class Camera {

    private Vector3f position = new Vector3f(0, 1, 0);
    private static final double MOVE_SPEED = 20D;

    private static final double TERRAIN_HEIGHT = 0F;

    private double currentSpeedUpwards = 0F;

    private float pitch;
    private float yaw = 0.0F;

    private double prevX;
    private double prevY;

    /*private Keyboard keyboard;

    public Camera(){
        keyboard = GameLoopMain.getDisplayManager().getKeyboard();
    }*/

    public void move(){
        checkInput();
        changeAngle();
        if(position.y + currentSpeedUpwards < TERRAIN_HEIGHT){
            position.y = 0.1F;
        }else{
            position.y += currentSpeedUpwards;
        }
    }

    private void checkInput(){
        boolean upPress = false;
        double dt = DisplayManager.getFrameTime();
        if(Keyboard.isKeyDown(GLFW_KEY_W)){
            moveBackwards(MOVE_SPEED * dt);
        }else if(Keyboard.isKeyDown(GLFW_KEY_S)){
            moveForwards(MOVE_SPEED * dt);
        }

        if(Keyboard.isKeyDown(GLFW_KEY_A)){
            moveRight(MOVE_SPEED * dt);
        }else if(Keyboard.isKeyDown(GLFW_KEY_D)){
            moveLeft(MOVE_SPEED * dt);
        }

        if(Keyboard.isKeyDown(GLFW_KEY_SPACE)){
            currentSpeedUpwards = 0.22D;
            upPress = true;
        }else if(Keyboard.isKeyDown(GLFW_KEY_LEFT_SHIFT)){
            currentSpeedUpwards = -0.22D;
            upPress = true;
        }

        currentSpeedUpwards = upPress ? currentSpeedUpwards : 0;
    }

    private void changeAngle(){
        long window = getWindow();
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(getWindow(), x, y);
        x.rewind();
        y.rewind();

        double newX = x.get();
        double newY = y.get();

        double deltaX = newX - 400;
        double deltaY = newY - 300;

        boolean rotX = newX != prevX;
        boolean rotY = newY != prevY;

        if(rotY) {
            pitch += deltaY;
        }
        if(rotX) {
            yaw += deltaX;
        }

        prevX = newX;
        prevY = newY;

        glfwSetCursorPos(getWindow(), 800/2, 600/2);
    }

    public void moveForwards(double distance){
        position.x -= distance * (float)Math.sin(Math.toRadians(yaw));
        position.z += distance * (float)Math.cos(Math.toRadians(yaw));
    }

    public void moveBackwards(double distance){
        position.x += distance * (float)Math.sin(Math.toRadians(yaw));
        position.z -= distance * (float)Math.cos(Math.toRadians(yaw));
    }

    public void moveLeft(double distance){
        position.x -= distance * (float)Math.sin(Math.toRadians(yaw-90));
        position.z += distance * (float)Math.cos(Math.toRadians(yaw-90));
    }

    public void moveRight(double distance) {
        position.x -= distance * (float)Math.sin(Math.toRadians(yaw+90));
        position.z += distance * (float)Math.cos(Math.toRadians(yaw+90));
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


}
