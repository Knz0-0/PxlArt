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


        //bouton save
        JButton saveButton = new JButton("Save image");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dessin.saveImage();
            }
        });
        headerPanel.add(saveButton);

        // 2e bouton save
        JButton saveRealSizeButton = new JButton("Save real size image");
        saveRealSizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dessin.saveRealSizeImage();
            }
        });
        headerPanel.add(saveRealSizeButton);

        //bouton open
        JButton openButton = new JButton("Open");
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dessin.openImage();
            }
        });
        headerPanel.add(openButton);



        //PROBLEME : on dirait que tous les boutons sont superposés sous le bouton save, meme si ils s'affichent correctement



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
        Color[] colors = {Color.BLACK, Color.WHITE, Color.GREEN, Color.CYAN, Color.BLUE, Color.RED, Color.ORANGE, Color.YELLOW};

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

        // Ajout du bouton "clear"
        JButton clearButton = new JButton("Clear");
        clearButton.setPreferredSize(new Dimension(100, 30));
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dessin.clear();// Appel de la méthode pour tout "effacer" (mettre toutes les cases en blanc)

            }
        });
        colorPanel.add(clearButton); // meme probleme que pour les boutons du header



        // Ajout du panneau de dessin centré
        add(drawingPanelWrapper, BorderLayout.CENTER);

        // Paramètres de la fenêtre
        setTitle("PxlArt Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setResizable(false);
    }

    public static void main(String[] args) {
        PxlArtApp app = new PxlArtApp();
        app.setVisible(true);
    }
}
