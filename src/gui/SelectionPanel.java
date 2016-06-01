package gui;

import entities.LightSource;
import org.joml.Vector3f;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

/**
 * Created by Carter Milch on 5/9/2016.
 */
public class SelectionPanel extends JPanel {

    private static boolean warning = false;

    private boolean userChange = false;

    private GridFrame frame;
    private IDButton button;

    private JComboBox<String> shapes;
    private JComboBox<String> textures;

    public SelectionPanel(GridFrame grid, IDButton button){
        this.button = button;
        frame = grid;
        setLayout(new FlowLayout());
        shapes = new JComboBox<>();
        textures = new JComboBox<>();
        initBoxes();
        if(grid.getShapes().get(button.getID()) != Shape.LIGHT) {
            textures.setSelectedIndex(grid.getTextures().get(button.getID()));
        }
        add(shapes);
        add(textures);
    }

    private void initBoxes(){
        Shape current = frame.getShapes().get(button.getID());
        shapes.addItem(current.toString());
        for(int i = 0; i < Shape.values().length; i++){
            if(!Shape.getName(i).equals(current.toString())) {
                shapes.addItem(Shape.getName(i));
            }
        }
        shapes.addItemListener(e -> {
            Shape s = Shape.getShape(shapes.getSelectedIndex());
            int id = button.getID();
            if(userChange && s == Shape.LIGHT && e.getStateChange() == ItemEvent.SELECTED){
                if(GridFrame.getLights().size() == 10){
                    JOptionPane.showMessageDialog(frame, "10 lights is the maximum amount allowed.");
                }else{
                    if(!warning) {
                        JOptionPane.showMessageDialog(frame, "The higher the light, the larger the area it covers.");
                        warning = true;
                    }
                    float xPos = (id % GridFrame.NUM_BUTTONS) * 4.0f;
                    float yPos = (id / GridFrame.NUM_BUTTONS) * 4.0f;
                    float zPos = -30 - (frame.getLayer() * 4);
                    Color c = JColorChooser.showDialog(this, "Choose Light Color", Color.WHITE);
                    Vector3f pos = new Vector3f(xPos, yPos, zPos);
                    Vector3f color = new Vector3f(c.getRed() / 100, c.getGreen() / 100, c.getBlue() / 100);
                    frame.addLight(new LightSource(pos, color));
                    button.setIcon(null);
                    button.setForeground(c);
                    button.setBackground(c);
                    frame.modifyTexture(button.getID(), c.getRGB());
                }
            } else {
                if(!userChange) {
                    userChange = true;
                }
                if(s != Shape.LIGHT && e.getStateChange() == ItemEvent.SELECTED) {
                    textures.setSelectedIndex(0);
                    JButton color = new JButton();
                    button.setForeground(color.getForeground());
                    button.setBackground(color.getBackground());
                    frame.modifyTexture(button.getID(), Texture.BLANK);
                }
                frame.modifyShape(id, s);
                if (textures.getSelectedIndex() != 0) {
                    button.updateIcon(textures.getSelectedItem().toString().toLowerCase());
                    frame.modifyTexture(button.getID(), Texture.getTexture(textures.getSelectedIndex()));
                }
            }
        });

        for(int i = 0; i  < Texture.values().length; i++) {
            textures.addItem(Texture.getName(i));
        }
        textures.addItemListener(e -> {
            int index = shapes.getSelectedIndex();
            if(index != Shape.values().length - 1 && index != 0) {
                button.updateIcon(textures.getSelectedItem().toString().toLowerCase());
                frame.modifyTexture(button.getID(), Texture.getTexture(textures.getSelectedIndex()));
            }
            if(textures.getSelectedIndex() == 0){
                button.setIcon(null);
                JButton color = new JButton();
                button.setBackground(color.getBackground());
                button.setForeground(color.getForeground());
            }
        });
    }
}
