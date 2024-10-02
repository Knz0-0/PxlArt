import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        //JFrame
        JFrame jFrame = new JFrame("PxlArt");
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setSize(960, 600);
        jFrame.setResizable(false);


        //JPanel
        Dessin dessin = new Dessin();
        jFrame.add(dessin);

        jFrame.setVisible(true);

    }
}
