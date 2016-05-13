package gui;

import engine.MasterRenderer;
import entities.Entity;
import models.TexturedModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Carter Milch on 5/9/2016.
 */
public class GridFrame extends JPanel {
    private static final int NUM_BUTTONS = 32;

    private HashMap<Integer, Shape> shapes = new HashMap<>();
    private HashMap<Integer, Texture> textures = new HashMap<>();

    IDButton[][] buttons;

    public GridFrame(){
        int nextID = 0;
        buttons = new IDButton[NUM_BUTTONS][NUM_BUTTONS];
        GridLayout layout = new GridLayout(32, 32, 0, 0);
        setLayout(layout);
        for(int i = 0; i < NUM_BUTTONS; i++){
            for(int j = 0; j < NUM_BUTTONS; j++){
                IDButton idButton = new IDButton(nextID);
                idButton.setSize(13, 13);
                idButton.addActionListener(new SelectionListener());
                buttons[i][j] = idButton;
                shapes.put(nextID, Shape.AIR);
                textures.put(nextID, Texture.BLANK);
                nextID++;
            }
        }
        for(int i = NUM_BUTTONS - 1; i >= 0; i--){
            for(int j = 0; j < NUM_BUTTONS; j++){
                add(buttons[i][j]);
            }
        }
    }

    private class SelectionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame f = new JFrame();
            f.setContentPane(new SelectionPanel(gui.GridFrame.this, (IDButton)e.getSource()));
            f.setSize(275, 400);
            f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            f.setVisible(true);
        }
    }

    public void modifyShape(int button, Shape shape){
        shapes.remove(button);
        shapes.put(button, shape);
    }

    public void modifyTexture(int button, Texture texture){
        textures.remove(button);
        textures.put(button, texture);
    }

    public void processInput(){
        float cube_offset = -0.4f;
        HashMap<TexturedModel, List<Entity>> entities = new HashMap<>();

        ArrayList<Entity> cubes = new ArrayList<>();
        TexturedModel cubeStone = MasterRenderer.makeTexturedModel("cube", "stone");
        TexturedModel cubeWater = MasterRenderer.makeTexturedModel("cube", "water");
        TexturedModel cubeBrick = MasterRenderer.makeTexturedModel("cube", "brick");
        TexturedModel cubeGrass = MasterRenderer.makeTexturedModel("cube", "grass");

        ArrayList<Entity> spheres = new ArrayList<>();
        TexturedModel sphereStone = MasterRenderer.makeTexturedModel("sphere", "stone");
        TexturedModel sphere;

        ArrayList<Entity> cylinders = new ArrayList<>();


        ArrayList<Entity> cones = new ArrayList<>();

        for(IDButton[] button : buttons){
            for(IDButton b : button){
                Shape s = shapes.get(b.getID());
                Texture t = textures.get(b.getID());

            }
        }
    }

    public HashMap<Integer, Shape> getShapes() {
        return shapes;
    }

    public HashMap<Integer, Texture> getTextures() {
        return textures;
    }

}
