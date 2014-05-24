package controller;

/**
 * Created by Keith on 5/18/14.
 */

    import java.awt.event.WindowEvent;
    import java.awt.event.WindowFocusListener;
    import java.io.IOException;
    import javax.imageio.ImageIO;
    import javax.swing.ImageIcon;
    import javax.swing.JDialog;
    import javax.swing.JLabel;

    public class AboutDialog implements WindowFocusListener {
        private final String IMAGE_URL = "../WatchListPro.png";
        JDialog dialog;


        public AboutDialog() {
            createAndShowGUI();
        }

        private void createAndShowGUI() {
            dialog = new JDialog();
            dialog.addWindowFocusListener(this);
            dialog.setUndecorated(true);

            try {
                dialog.add(new JLabel(new ImageIcon(ImageIO.read(getClass().getResourceAsStream(IMAGE_URL)))));
            } catch (IOException e) {
                e.printStackTrace();
            }

            //dialog.pack();

            dialog.setSize(460, 450);
            dialog.setResizable(false);
            dialog.setLocationRelativeTo(null);

            //dialog.setLocationByPlatform(true);
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
