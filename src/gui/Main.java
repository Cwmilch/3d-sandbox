package gui;

import engine.EngineMain;
import engine.ModelLoader;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Carter Milch on 5/9/2016.
 */
public class Main {

    private static ModelLoader loader;

    public static void main(String[] args){
        loader = new ModelLoader();

        JFrame f = new JFrame("Layer 1 (Closest)");
        GridFrame g = new GridFrame(f);
        GridFrame.frames.add(g);

        f.setContentPane(g);
        f.setSize(416, 416);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public static ModelLoader getLoader(){
        return loader;
    }
}
