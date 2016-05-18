package engine;

import gui.GridFrame;
import org.lwjgl.opengl.GL;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Carter Milch on 5/9/2016.
 */
public class Driver {

    private static DisplayManager m;
    private static ModelLoader loader;

    /*public static void main(String[] args){
        m = new DisplayManager();
        m.init();
        GL.createCapabilities();
        long window = m.getWindow();
        loader = new ModelLoader();

        JFrame f = new JFrame();
        GridFrame g = new GridFrame();
        f.setContentPane(g);
        f.setSize(416, 416);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                g.processInput();
            }
        });
        f.setVisible(true);
    }*/

    public static ModelLoader getLoader(){
        return loader;
    }

    public static DisplayManager getDisplayManager(){
        return m;
    }
}
