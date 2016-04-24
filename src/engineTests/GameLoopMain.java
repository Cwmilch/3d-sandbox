package engineTests;

import com.sun.javafx.sg.prism.NGShape;
import engine.DisplayManager;
import engine.ModelLoader;
import engine.RawModel;
import engine.Renderer;

import java.util.ArrayList;
import java.util.List;

public class GameLoopMain {

    private static List<ModelLoader> loaderList;
    private static Renderer renderer;

    public static void main(String[] args){
        loaderList = new ArrayList<>();
        renderer = new Renderer();

        DisplayManager m = new DisplayManager();
        m.run();

        ModelLoader loader = new ModelLoader();
        loaderList.add(loader);

        float[] vertices = {
                -0.5f, 0.5f, 0f,    //v0
                -0.5f, -0.5f, 0f,   //v1
                -0.5f, 0.5f, 0f,    //v2
                0.5f, -0.5f, 0f,    //v3
        };

        int[] indices = {
                0, 1, 3, //Top left triangle (v0, v1, v3)
                3, 1, 2  // Bottom right triangle
        };

        RawModel model = loader.loadToVAO(vertices, indices);
        renderer.render(model);
    }

    public static List<ModelLoader> getLoaderList(){
        return loaderList;
    }

    public static Renderer getRenderer(){
        return renderer;
    }
}
