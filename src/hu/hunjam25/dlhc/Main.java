package hu.hunjam25.dlhc;

import javax.swing.*;
import java.awt.*;

class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        var window = new JFrame();
        window.setTitle("Dont let him cook!");
        window.add(new JLabel("Hello world"));
        window.setVisible(true);
        window.setBounds(10,10,600,500);

        var canvas = new Canvas();
        window.add(canvas);
        for(;;){
            window.repaint();
        }

    }
}

