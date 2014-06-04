package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;

/**
 * An about dialog to display information about the project and team.
 */
public class AboutDialog implements WindowFocusListener {

    private final String IMAGE_URL = "WatchListPro.png";
    private JDialog dialog;

    /**
     * Constructor.
     */
    public AboutDialog() {
        createAndShow();
    }

    /**
     * Creates and shows the about dialog.
     */
    private void createAndShow() {
        dialog = new JDialog();
        dialog.addWindowFocusListener(this);
        //dialog.setUndecorated(true);
        Container container = new Container();
        JLabel line      = new JLabel("•.¸¸¸.•¨¨•.¸¸¸.•¨¨•.¸¸¸.•¨¨•.¸¸¸.•¨¨•.¸¸¸.•¨¨•.¸¸¸.•¨¨•.¸¸¸.•", JLabel.CENTER);
        JLabel creators1 = new JLabel("      Created by The Dragons (Team 5): Keith Hamm, Noah Hasson,");
        JLabel creators2 = new JLabel("            Jason Keller, Wenbo Zhang, John Beck, David Chapman");
        container.add(creators1);
        container.add(creators2);

        JLabel madeFor   = new JLabel("      Created for CIS 422.");
        JLabel madeIn    = new JLabel("      Programmed using IntelliJ IDEA, using Java 8.");

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

    /**
     * Does nothing.
     * @param e is the window event.
     */
    @Override
    public void windowGainedFocus(WindowEvent e) {

    }

    /**
     * Disposes of the dialog when it looses focus.
     * @param e is the window event.
     */
    @Override
    public void windowLostFocus(WindowEvent e) {
        dialog.dispose();
    }
}
