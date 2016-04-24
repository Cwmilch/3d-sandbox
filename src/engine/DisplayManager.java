package engine;

import engineTests.GameLoopMain;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Created by Carter Milch on 4/23/2016.
 */
public class DisplayManager {

    private static long window;


    public void run(){
        try{
            init();
            update();

            GameLoopMain.getLoaderList().forEach(ModelLoader::wipeLists);
            glfwFreeCallbacks(window);
            glfwDestroyWindow(window);
        }finally {
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }

    private void init(){
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
        window = glfwCreateWindow(width, height, "Hello world!", NULL, NULL);
        if(window == NULL){
            throw new RuntimeException("Failed to create GLFW window");
        }

        //Key callback - Action triggered by keystroke
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){
                glfwSetWindowShouldClose(window, true); //detected in rendering loop
            }
        });

        //Get resolution of primary monitor
        GLFWVidMode vm = glfwGetVideoMode(glfwGetPrimaryMonitor());
        //Center window
        glfwSetWindowPos(window, (vm.width() - width) / 2, (vm.height() - height) / 2);

        glfwMakeContextCurrent(window); //Make context of window current on calling thread
        glfwSwapInterval(1); //Enable v-sync - Syncs FPS to monitor refresh rate

        //Make window visible
        glfwShowWindow(window);
    }

    private void update(){
        //Detects context made current in current thread (purpose of line 51)
        //and makes it usable with OpenGL
        GL.createCapabilities();

        //Color used to clear screen
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        //Render until user has attempted to close window or press escape key
        while(!glfwWindowShouldClose(window)){
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); //clear framebuffer
            GameLoopMain.getRenderer().prepareFrame();
            glfwSwapBuffers(window);

            glfwPollEvents();
        }
    }
}
