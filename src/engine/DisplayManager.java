package engine;

import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Created by Carter Milch on 4/23/2016.
 */
public class DisplayManager {

    private static long window;
    private GLFWKeyCallback keyCallback;

    private static double lastFrameTime;
    private static double timeChange;

    public void init(){
        GLFWErrorCallback.createPrint(System.err).set();
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        //Configure window
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); //Window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); //Window will be resizable

        //Default dimensions
        int width = 1280;
        int height = 720;

        //Create window
        window = glfwCreateWindow(width, height, "Test", NULL, NULL);
        if(window == NULL){
            throw new RuntimeException("Failed to create GLFW window");
        }

        //Key callback - Action triggered by keystroke
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){
                glfwSetWindowShouldClose(window, true); //detected in rendering loop
            }
        });
        glfwSetKeyCallback(window, keyCallback = new Keyboard());

        //Get resolution of primary monitor
        GLFWVidMode vm = glfwGetVideoMode(glfwGetPrimaryMonitor());
        //Center window
        glfwSetWindowPos(window, (vm.width() - width) / 2, (vm.height() - height) / 2);

        glfwMakeContextCurrent(window); //Make context of window current on calling thread
        glfwSwapInterval(1); //Enable v-sync - Syncs FPS to monitor refresh rate

        //Make window visible
        glfwShowWindow(window);
        lastFrameTime = getCurrentTime();
    }


    public long getWindow(){
        return window;
    }

    public static void update(){
        double currentFrameTime = getCurrentTime();
        timeChange  = (currentFrameTime - lastFrameTime) / 1000f;
        lastFrameTime = currentFrameTime;
    }

    public static double getFrameTime(){
        return timeChange;
    }

    public Keyboard getKeyboard(){
        return (Keyboard) keyCallback;
    }

    private static double getCurrentTime(){
        return GLFW.glfwGetTime() * 1000L;
    }
}
