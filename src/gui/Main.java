package gui;

import javax.swing.*;

/**
 * Created by Carter Milch on 5/9/2016.
 */
public class Main {

    public static void main(String[] args){
        JFrame f = new JFrame();
        f.setContentPane(new GridFrame());
        f.setSize(416, 416);
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        f.setVisible(true);
    }
}
