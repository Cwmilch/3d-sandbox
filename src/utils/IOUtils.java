package utils;

import entities.LightSource;
import gui.GridFrame;
import gui.IDButton;
import gui.Shape;
import gui.Texture;
import org.joml.Vector3f;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Carter Milch on 5/25/2016.
 */
public class IOUtils {

    //layer:id:shape:(texture/color)|
    public static void saveConfiguration(){
        JFileChooser fileChooser = new JFileChooser();
        int val = fileChooser.showSaveDialog(null);
        if(val == JFileChooser.APPROVE_OPTION){
            try(FileWriter fw = new FileWriter(fileChooser.getSelectedFile() + ".txt")){
                BufferedWriter bw = new BufferedWriter(fw);
                StringBuilder sb = new StringBuilder("");
                for(GridFrame f : GridFrame.frames){
                    HashMap<Integer, Shape> shapes = f.getShapes();
                    HashMap<Integer, Integer> textures = f.getTextures();
                    for(IDButton[] buttons : f.getButtons()){
                        for(IDButton button : buttons){
                            int shape = shapes.get(button.getID()).getID();
                            if(shape != 0) {
                                int id = button.getID();
                                int texture = textures.get(button.getID());
                                int layer = f.getLayer();
                                sb.append(layer).append(":").append(id).append(":")
                                        .append(shape).append(":").append(texture).append("|");
                            }
                        }
                    }
                }
                bw.write(sb.toString());
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadConfiguration(File f){
        for(GridFrame frame : GridFrame.frames){
            SwingUtilities.getWindowAncestor(frame).dispose();
        }
        int lastLayer = -1;
        ArrayList<IDButton> buttons = new ArrayList<>();
        HashMap<Integer, Integer> lightButtons = new HashMap<>();
        HashMap<Integer, Shape> shapes = new HashMap<>();
        HashMap<Integer, Integer> textures = new HashMap<>();
        try(FileReader fr = new FileReader(f)){
            BufferedReader br = new BufferedReader(fr);
            String next;
            String full = "";
            while((next = br.readLine()) != null){
                full += next;
            }
            br.close();
            String[] frameInfo = full.substring(0, full.length() - 1).split("\\|");
            for(String s : frameInfo){
                String[] data = s.split(":");
                int layer = Integer.parseInt(data[0]);

                if(layer != lastLayer){
                    if(lastLayer != -1){
                        JFrame frame = new JFrame();
                        GridFrame g = new GridFrame(frame, layer, buttons, shapes, textures);
                        GridFrame.frames.add(g);

                        frame.setContentPane(g);
                        frame.setSize(416, 416);
                        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                        frame.setVisible(true);
                    }
                    shapes.clear();
                    textures.clear();
                    int id = 0;
                    for(int i = 0; i < GridFrame.NUM_BUTTONS * GridFrame.NUM_BUTTONS; i++){
                        IDButton idButton = new IDButton(id);
                        idButton.setSize(13, 13);
                        shapes.put(id, Shape.AIR);
                        textures.put(id, Texture.getID(Texture.BLANK));
                        buttons.add(idButton);
                        id++;
                    }
                    lastLayer = Integer.parseInt(data[0]);
                }
                int button = Integer.parseInt(data[1]);
                int shape = Integer.parseInt(data[2]);
                int texture = Integer.parseInt(data[3]);
                shapes.remove(button);
                shapes.put(button, Shape.getShape(shape));
                if(Shape.getShape(shape) == Shape.LIGHT){
                    lightButtons.put(button, texture);
                }
                textures.remove(button);
                textures.put(button, texture);
            }
            JFrame frame = new JFrame();
            GridFrame g = new GridFrame(frame, lastLayer, buttons, shapes, textures);
            for(int i : lightButtons.keySet()){
                float xPos = (i % GridFrame.NUM_BUTTONS) * 4.0f;
                float yPos = (i / GridFrame.NUM_BUTTONS) * 4.0f;
                float zPos = -30 - (g.getLayer() * 4);
                Vector3f pos = new Vector3f(xPos, yPos, zPos);

                Color c = new Color(lightButtons.get(i));
                Vector3f color = new Vector3f(c.getRed() / 100, c.getGreen() / 100, c.getBlue() / 100);
                LightSource s = new LightSource(pos, color);
                g.addLight(s);
            }
            GridFrame.frames.add(g);

            frame.setContentPane(g);
            frame.setSize(416, 416);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
