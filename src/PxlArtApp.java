import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PxlArtApp extends JFrame {
    private Dessin dessin;

    public PxlArtApp() {
        setLayout(new BorderLayout());


        //création du "header"
        JPanel headerPanel = new JPanel();
        add(headerPanel, BorderLayout.NORTH);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dessin.saveImage();
            }
        });

        JButton openButton = new JButton("Open");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dessin.openImage();
            }
        });


        headerPanel.add(saveButton);
        headerPanel.add(openButton);





        // Création de la zone de dessin
        dessin = new Dessin();
        dessin.setPreferredSize(new Dimension(400, 400)); // Dimensions préférées de la zone de dessin


        // Encapsulation de la zone de dessin dans un JPanel parent pour centrer
        JPanel drawingPanelWrapper = new JPanel();
        drawingPanelWrapper.setLayout(new GridBagLayout());
        drawingPanelWrapper.add(dessin);

        // Panneau de sélection des couleurs
        JPanel colorPanel = new JPanel();
        add(colorPanel, BorderLayout.SOUTH);

        // Liste des couleurs prédéfinies
        Color[] colors = {Color.BLACK, Color.WHITE, Color.GREEN, Color.BLUE, Color.RED, Color.ORANGE, Color.YELLOW};

        // Création des boutons de couleur
        for (Color color : colors) {
            JButton colorButton = new JButton();
            colorButton.setBackground(color);
            colorButton.setPreferredSize(new Dimension(30, 30));
            colorButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dessin.setCurrentColor(color);
                }
            });
            colorPanel.add(colorButton);
        }

        // Ajout du bouton "Toggle Grid"
        JButton toggleGridButton = new JButton("Toggle Grid");
        toggleGridButton.setPreferredSize(new Dimension(100, 30));
        toggleGridButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dessin.toggleGrid();// Appel de la méthode pour afficher ou masquer la grille

            }
        });
        colorPanel.add(toggleGridButton);

        // Ajout du panneau de dessin centré
        add(drawingPanelWrapper, BorderLayout.CENTER);

        // Paramètres de la fenêtre
        setTitle("PxlArt Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        
        setMinimumSize(new Dimension(dessin.getGridWidth() * (dessin.getCellSize() + 2), dessin.getGridHeight() * (dessin.getGridHeight() + 2) + 60));
    }

    public static void main(String[] args) {
        PxlArtApp app = new PxlArtApp();
        app.setVisible(true);
    }
}
