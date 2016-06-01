package gui;

import engine.EngineMain;
import engine.MasterRenderer;
import entities.Entity;
import entities.LightSource;
import models.TexturedModel;
import org.joml.Vector3f;
import utils.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Carter Milch on 5/9/2016.
 */
public class GridFrame extends JPanel {
    public static final int NUM_BUTTONS = 32;
    public static ArrayList<GridFrame> frames = new ArrayList<>();

    private static int nextLayer = 0;

    private HashMap<Integer, Shape> shapes = new HashMap<>();
    private HashMap<Integer, Integer> textures = new HashMap<>();

    private static ArrayList<Entity> entityList = new ArrayList<>();
    private static ArrayList<LightSource> lightList = new ArrayList<>();

    private IDButton[][] buttons;
    private JFrame jFrame;
    private int layer;

    public GridFrame(JFrame frame){
        jFrame = frame;

        layer = nextLayer;
        nextLayer++;

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
                textures.put(nextID, Texture.BLANK.getID());
                nextID++;
            }
        }
        for(int i = NUM_BUTTONS - 1; i >= 0; i--){
            for(int j = 0; j < NUM_BUTTONS; j++){
                add(buttons[i][j]);
            }
        }

        initMenu();
    }

    public GridFrame(JFrame frame, int layer, ArrayList<IDButton> buttons,
                     HashMap<Integer, Shape> shapes, HashMap<Integer, Integer> textures){
        GridLayout layout = new GridLayout(32, 32, 0, 0);
        setLayout(layout);

        this.buttons = new IDButton[NUM_BUTTONS][NUM_BUTTONS];
        frame.setTitle(layer == 0 ? "Layer 1 (Closest)" : "Layer " + (layer + 1));

        jFrame = frame;
        this.layer = layer;
        nextLayer = layer+1;
        this.shapes = shapes;
        this.textures = textures;

        int count = 0;
        for(int i = 0; i < NUM_BUTTONS; i++){
            for(int j = 0; j < NUM_BUTTONS; j++){
                this.buttons[i][j] = buttons.get(count);
                this.buttons[i][j].addActionListener(new SelectionListener());

                Texture t = Texture.getTexture(textures.get(this.buttons[i][j].getID()));
                if(t != Texture.BLANK){
                    if(t != null) {
                        this.buttons[i][j].updateIcon(t.toString());
                    }else{
                        int rgb = textures.get(this.buttons[i][j].getID());
                        Color color = new Color(rgb);
                        this.buttons[i][j].setBackground(color);
                        this.buttons[i][j].setForeground(color);
                    }
                }

                this.buttons[i][j].setSize(13, 13);
                count++;
            }
        }

        for(int i = NUM_BUTTONS - 1; i >= 0; i--){
            for(int j = 0; j < NUM_BUTTONS; j++){
                add(this.buttons[i][j]);
            }
        }

        initMenu();
    }

    private class SelectionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame f = new JFrame();
            f.setContentPane(new SelectionPanel(gui.GridFrame.this, (IDButton)e.getSource()));
            f.setSize(200, 100);
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
        textures.put(button, texture.getID());
    }

    public void modifyTexture(int button, int color){
        textures.remove(button);
        textures.put(button, color);
    }

    public void processInput(){
        SwingUtilities.getWindowAncestor(this).setVisible(false);

        float cubeYOffset = -0.3f;
        float sphereXOffset = 1.9f;
        float sphereYOffset = 2.0f;

        for(IDButton[] button : buttons){
            for(IDButton b : button) {
                if (shapes.get(b.getID()) != Shape.AIR) {
                    Shape s = shapes.get(b.getID());
                    if(s != Shape.LIGHT) {
                        Texture t = Texture.getTexture(textures.get(b.getID()));
                        int id = b.getID();
                        float xPos = (id % NUM_BUTTONS) * 4.0f;
                        float yPos = ((id / NUM_BUTTONS) * 4.0f);
                        float zPos = -30 - (layer * 4);
                        Vector3f pos = new Vector3f(xPos, yPos, zPos);

                        TexturedModel model = MasterRenderer.getModel(s, t);
                        switch (s) {
                            case CONE:
                                entityList.add(new Entity(model, pos, 0, 0, 0, 1));
                                break;
                            case CUBE:
                                Vector3f cubePos = new Vector3f(xPos, yPos + cubeYOffset, zPos);
                                entityList.add(new Entity(model, cubePos, 0, 0, 0, 1));
                                break;
                            case CYLINDER:
                                entityList.add(new Entity(model, pos, 0, 0, 0, 1));
                                break;
                            case SPHERE:
                                Vector3f spherePos = new Vector3f(xPos + sphereXOffset, yPos + sphereYOffset, zPos);
                                entityList.add(new Entity(model, spherePos, 0, 0, 0, 1));
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    public void initMenu(){
        JMenuBar menu = new JMenuBar();

        JMenu options = new JMenu("Options");
        JMenu file = new JMenu("File");

        JMenuItem save = new JMenuItem("Save Configuration");
        save.addActionListener(e -> IOUtils.saveConfiguration());

        JMenuItem load = new JMenuItem("Load Configuration");
        load.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int val = fileChooser.showOpenDialog(null);
            if(val == JFileChooser.APPROVE_OPTION){
                IOUtils.loadConfiguration(fileChooser.getSelectedFile());
            }
        });

        JMenuItem newLayer = new JMenuItem("New Layer");
        newLayer.addActionListener(e -> {
            JFrame f = new JFrame("Layer " + (nextLayer + 1));
            GridFrame g = new GridFrame(f);
            f.setContentPane(g);
            f.setSize(416, 416);
            f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            f.setVisible(true);
            frames.add(g);
        });

        JMenuItem render = new JMenuItem("Render All");
        render.addActionListener(e -> EngineMain.init());

        options.add(newLayer);
        options.add(render);

        file.add(save);
        file.add(load);

        menu.add(file);
        menu.add(options);

        jFrame.setJMenuBar(menu);
    }

    public HashMap<Integer, Shape> getShapes() {
        return shapes;
    }

    public HashMap<Integer, Integer> getTextures() {
        return textures;
    }

    public IDButton[][] getButtons(){
        return buttons;
    }

    public static ArrayList<Entity> getEntities(){
        return entityList;
    }

    public static ArrayList<LightSource> getLights(){
        return lightList;
    }

    public int getLayer(){
        return layer;
    }

    public void addLight(LightSource s){
        lightList.add(s);
    }
}
