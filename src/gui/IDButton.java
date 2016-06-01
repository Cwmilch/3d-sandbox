package gui;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Carter Milch on 5/9/2016.
 */
public class IDButton extends JButton {
    private int id;

    public IDButton(int id) {
        this.id = id;
    }

    public void updateIcon(String file) {
        try {
            Image icon = ImageIO.read(new File("res/" + file.toLowerCase() + ".png"));
            setIcon(new ImageIcon(icon.getScaledInstance(13, 13, 0)));
        } catch (NullPointerException | IIOException e) {
            setIcon(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getID(){
        return id;
    }
}