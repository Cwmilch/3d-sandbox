package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Carter Milch on 5/9/2016.
 */
public class SelectionPanel extends JPanel {

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
        shapes.addItemListener(e -> frame.modifyShape(button.getID(),
                Shape.getShape(shapes.getSelectedIndex())));

        for(int i = 0; i  < Texture.values().length; i++){
            textures.addItem(Texture.getName(i));
        }
        textures.addItemListener(e -> {
            button.updateIcon(textures.getSelectedItem().toString().toLowerCase());
            frame.modifyTexture(button.getID(), Texture.getTexture(textures.getSelectedIndex()));
        });
    }


}
