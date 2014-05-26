package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;

public class AboutDialog implements WindowFocusListener {
    private final String IMAGE_URL = "WatchListPro.png";
    JDialog dialog;
    JLabel line;
    JLabel creators1;
    JLabel creators2;
    JLabel madeFor;
    JLabel madeIn;
    Container container;


    public AboutDialog() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        dialog = new JDialog();
        dialog.addWindowFocusListener(this);
        dialog.setUndecorated(true);
        container = new Container();
        line = new JLabel(" •.¸¸¸.•¨¨•.¸¸¸.•¨¨•.¸¸¸.•¨¨•.¸¸¸.•¨¨•.¸¸¸.•¨¨•.¸¸¸.•¨¨•.¸¸¸.•");
        creators1 = new JLabel("      Created by The Dragons (Team 5): Keith Hamm, Noah Hasson,");
        creators2 = new JLabel("            Jason Keller, Wenbo Zhang, John Beck, David Chapman");
        container.add(creators1);
        container.add(creators2);


        madeFor = new JLabel("      Created for CIS 422.");
        madeIn = new JLabel("      Programmed using IntelliJ IDEA, using Java 8.");

        GridLayout layout = new GridLayout(6,0);
        GridLayout layout2 = new GridLayout(3,0);
        dialog.setLayout(layout);
        container.setLayout(layout2);


        try {
            dialog.add(new JLabel(new ImageIcon(ImageIO.read(getClass().getResourceAsStream(IMAGE_URL)))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.add(line);
        dialog.add(container);

        dialog.add(madeFor);
        dialog.add(madeIn);

        dialog.setSize(425, 250);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }



    @Override
    public void windowGainedFocus(WindowEvent e) {

    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        dialog.dispose();
    }
}
