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
        shapes.setSelectedIndex(Shape.getID(grid.getShapes().get(button.getID())));
        textures.setSelectedIndex(Texture.getID(grid.getTextures().get(button.getID())));
        add(shapes);
        add(textures);
    }

    private void initBoxes(){
        for(int i = 0; i < Shape.values().length; i++){
            shapes.addItem(Shape.getName(i));
        }
        shapes.addItemListener(e -> {
            Shape s = Shape.getShape(shapes.getSelectedIndex());
            int id = button.getID();
            if(s == Shape.LIGHT && e.getStateChange() == ItemEvent.SELECTED){
                if(GridFrame.getLights().size() == 10){
                    JOptionPane.showMessageDialog(frame, "10 lights is the maximum amount allowed.");
                }else {
                    if(!warning) {
                        JOptionPane.showMessageDialog(frame, "The higher the light, the larger the area it covers.");
                        warning = true;
                    }
                    float xPos = (id % GridFrame.NUM_BUTTONS) * 4.0f;
                    float yPos = (id / GridFrame.NUM_BUTTONS) * 4.0f;
                    float zPos = -30 - (frame.getLayer() * 4);
                    Color c = JColorChooser.showDialog(this, "Choose Light Color", Color.WHITE);
                    Vector3f pos = new Vector3f(xPos, yPos, zPos);
                    float diff = 100 + pos.y;
                    Vector3f color = new Vector3f(c.getRed() / diff, c.getGreen() / diff, c.getBlue() / diff);
                    frame.addLight(new LightSource(pos, color));
                    button.setIcon(null);
                    button.setForeground(c);
                    button.setBackground(c);
                }
            } else {
                frame.modifyShape(id, s);
                if(textures.getSelectedIndex() != 0){
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
        });
    }
}
