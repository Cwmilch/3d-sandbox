package engine;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Carter Milch on 4/29/2016.
 */
public class Keyboard extends GLFWKeyCallback {

    public static boolean[] keys = new boolean[65536];

    @Override
    public void invoke(long window, int key, int keycode, int action, int mods){
        keys[key] = action != GLFW_RELEASE;
    }

    public static boolean isKeyDown(int keycode){
        return keys[keycode];
    }
}
