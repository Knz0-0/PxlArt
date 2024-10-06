import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

public class PxlArtApp extends JFrame {

    private Dessin dessin;
    private Color[] colors = {Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};
    private JPanel headerPanel;
    private JPanel colorPanel;

    public PxlArtApp() {
        setLayout(new BorderLayout());

        // Création de la zone de dessin
        dessin = new Dessin();


        //création du "header"
        headerPanel = new JPanel();
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




        // Créer les spinners pour ajuster la largeur et la hauteur de la grille
        JSpinner widthSpinner = new JSpinner(new SpinnerNumberModel(dessin.getGridWidth(), 1, 100, 1));
        JSpinner heightSpinner = new JSpinner(new SpinnerNumberModel(dessin.getGridHeight(), 1, 100, 1));
        JSpinner cellSizeSpinner = new JSpinner(new SpinnerNumberModel(dessin.getCellSize(), 5, 30, 1));

        // Ajouter des labels pour chaque spinner
        JLabel widthLabel = new JLabel("Width:");
        JLabel heightLabel = new JLabel("Height:");
        JLabel cellSizeLabel = new JLabel("Cell size:");

        // Ajouter un ChangeListener pour redimensionner la grille immédiatement lors du changement de valeur
        widthSpinner.addChangeListener(e -> {
            int newWidth = (int) widthSpinner.getValue();
            dessin.resizeGrid(newWidth, dessin.getGridHeight()); // Appeler resizeGrid avec la nouvelle largeur
        });

        heightSpinner.addChangeListener(e -> {
            int newHeight = (int) heightSpinner.getValue();
            dessin.resizeGrid(dessin.getGridWidth(), newHeight); // Appeler resizeGrid avec la nouvelle hauteur
        });

        cellSizeSpinner.addChangeListener(e -> {
            int newCellSize = (int) cellSizeSpinner.getValue();
            dessin.resizeCell(newCellSize); //appeler resizeCell avec la nouvelle taille
        });

        // Ajouter les éléments au headerPanel (ou créer un nouveau panel spécifique)
        headerPanel.add(widthLabel);
        headerPanel.add(widthSpinner);
        headerPanel.add(heightLabel);
        headerPanel.add(heightSpinner);
        headerPanel.add(cellSizeLabel);
        headerPanel.add(cellSizeSpinner);



        // Encapsulation de la zone de dessin dans un JPanel parent pour centrer
        JPanel drawingPanelWrapper = new JPanel();
        drawingPanelWrapper.setLayout(new GridBagLayout());
        drawingPanelWrapper.add(dessin);

        // Panneau de sélection des couleurs
        colorPanel = new JPanel();
        add(colorPanel, BorderLayout.SOUTH);


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

        // ajout du vouton "personaliser les couleurs"
        JButton customizeColorsButton = new JButton("Edit Colors");
        customizeColorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openColorCustomizationWindow();
            }
        });
        colorPanel.add(customizeColorsButton);

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
        //ajout du logo
        try{
            BufferedImage icon = ImageIO.read(new File("./logo.png"));
            setIconImage(icon);
        }catch(IOException e){
            e.printStackTrace();
        }


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setResizable(false);
    }

    private void openColorCustomizationWindow(){
        //nouvelle fenetre pop-up
        JDialog colorDialog = new JDialog(this, "Edit colors");
        colorDialog.setLayout(new GridLayout(0, 1));

        //affichage des couleurs à personaliser
        for (int i = 0; i < colors.length; i++) {
            int colorIndex = i; //necessaire pour l'acces dans le actionlistener

            JButton colorButton = new JButton("Modify color" + (i+1));
            colorButton.setBackground(colors[i]);
            colorButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Color newColor = JColorChooser.showDialog(null, "Choose a color", colors[colorIndex]);
                    if (newColor != null){
                        colors[colorIndex] = newColor;


                        //JButton button = (JButton) colorDialog.getComponent(colorIndex);
                        //button.setBackground(newColor);


                        updateColorButtons();
                    }
                }
            });
            colorDialog.add(colorButton);
        }
        colorDialog.setSize(400, 400);
        colorDialog.setVisible(true);
    }


    private void updateColorButtons(){
        for (int i = 0; i < colorPanel.getComponentCount() - 3; i++) {
            if (colorPanel.getComponent(i) instanceof JButton){
                //j'ai pas compris ça, c'est chatgpt qui a fait
                JButton button = (JButton) colorPanel.getComponent(i);
                button.setBackground(colors[i]);

                //modifier la couleur qui d'active quand on clique sur le bouton
                button.removeActionListener(button.getActionListeners()[0]);
                int finalI = i;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dessin.setCurrentColor(colors[finalI]);
                    }
                });
            }
        }
    }



    public static void main(String[] args) {
        PxlArtApp app = new PxlArtApp();
        app.setVisible(true);
    }
}
