import org.w3c.dom.css.RGBColor;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Dessin extends JPanel {
    private static int CELL_SIZE = 25;
    private static int GRID_WIDTH = 20;
    private static int GRID_HEIGHT = 20;
    private Color[][] gridColors;
    private Color currentColor = Color.BLACK;
    private boolean showGrid = false;

    public Dessin() {
        
        //paramètres de fenètre
        setPreferredSize(new Dimension(400, 400)); // Dimensions préférées de la zone de dessin

        // Initialiser la grille avec des couleurs par défaut (par exemple, blanc)
        gridColors = new Color[GRID_WIDTH][GRID_HEIGHT];
        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_HEIGHT; j++) {
                gridColors[i][j] = Color.WHITE;
            }
        }

        // Gestion des clics de souris pour colorier les cellules
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                colorierCellule(e);
            }
        });

        // Gestion des déplacements de souris pour colorier en glissant
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                colorierCellule(e);
            }
        });
    }

    /**
     * affiche ou cache la grille
     */
    public void toggleGrid(){
        showGrid = !showGrid;
        repaint();
    }

    public void clear(){
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                gridColors[x][y] = Color.WHITE;
            }
        }
        repaint();
    }


    /**
     * calcule la case à colorier en fonction de la position de la souris sur le jpanel
     * @param e
     */
    private void colorierCellule(MouseEvent e) {
        int col = e.getX() / (CELL_SIZE + 1);
        int row = e.getY() / (CELL_SIZE + 1);

        if (col < GRID_WIDTH && row < GRID_HEIGHT && col >= 0 && row >= 0) {
            // Colorier la cellule avec la couleur actuelle (par exemple, rouge)
            gridColors[col][row] = currentColor;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dessiner la grille
        for (int x = 0; x < getWidth() && x / (CELL_SIZE + 1) < gridColors.length; x += CELL_SIZE + 1) {
            for (int y = 0; y < getHeight() && y / (CELL_SIZE + 1) < gridColors[0].length; y += CELL_SIZE + 1) {
                int col = x / (CELL_SIZE + 1);
                int row = y / (CELL_SIZE + 1);

                // Assurer que les indices sont dans la limite de gridColors
                if (col < gridColors.length && row < gridColors[0].length) {
                    // Remplir la cellule avec la couleur stockée dans gridColors
                    g.setColor(gridColors[col][row]);
                    g.fillRect(x, y, CELL_SIZE +1, CELL_SIZE +1);

                    // Dessiner les bordures de la cellule
                    if (showGrid){
                        g.setColor(Color.GRAY);
                        g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                    }

                }
            }
        }
    }


    //PROBLEME avec saveImage et openImage. openimage se déclenche
    //quand on clique sur le bouton save, et pas le bouton open. le bouton save devient donc inutile

    /**
     * sauvegarde le dessin dans un fichier .png
     */
    public void saveImage() {
        // Création de l'image avec la taille de la grille
        BufferedImage image = new BufferedImage(GRID_WIDTH, GRID_HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                image.setRGB(x, y, gridColors[x][y].getRGB());
            }
        }

        // Sélecteur de fichier
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer sous...");

        // Filtre pour ne permettre que le PNG
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichiers PNG", "png");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            // Ajoute .png si l'utilisateur ne l'a pas mis
            if (!filePath.toLowerCase().endsWith(".png")) {
                fileToSave = new File(filePath + ".png");
            }

            try {
                ImageIO.write(image, "png", fileToSave);
                JOptionPane.showMessageDialog(null, "Image sauvegardée sous : " + fileToSave.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur lors de la sauvegarde : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }

        repaint();
    }


    public void saveRealSizeImage(){
        //version taille réelle

        BufferedImage imageTailleReelle = new BufferedImage(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                for (int xr = 0; xr < CELL_SIZE; xr++) {
                    for (int yr = 0; yr < CELL_SIZE; yr++) {
                        imageTailleReelle.setRGB(x * CELL_SIZE + xr, y * CELL_SIZE + yr, gridColors[x][y].getRGB());

                        //debug
                        // System.out.println("pixel (" + (x+xr) + " ; " + (y+yr) + ") écrit : " + gridColors[x][y]);
                    }
                }
            }
        }


        try {

            //version taille réelle
            File outputfileRealSize = new File("pixel_art_image_taille_reelle.png"); // pour le test, l'image est sauvegardée à la racine du projet
            ImageIO.write(imageTailleReelle, "png", outputfileRealSize);
            JOptionPane.showMessageDialog(this, "Image saved as pixel_art_image_taille_reelle.png");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving image: " + e.getMessage());
        }
    }


    /**
     * ouvre un fichier .png et permet de le modifier
     */
    public void openImage(){
        BufferedImage image;
        try {
            File inputFile = new File("pixel_art_image.png");
            image = ImageIO.read(inputFile);
            JOptionPane.showMessageDialog(this, "Opened " + inputFile.getPath());

            // Redimensionner la grille en fonction de l'image ouverte
            GRID_WIDTH = image.getWidth();
            GRID_HEIGHT = image.getHeight();

            gridColors = new Color[GRID_WIDTH][GRID_HEIGHT]; // Redimensionner la grille

            for (int x = 0; x < GRID_WIDTH; x++) {
                for (int y = 0; y < GRID_HEIGHT; y++) {
                    gridColors[x][y] = new Color(image.getRGB(x, y));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error opening image: " + e.getMessage());
        }

        repaint();  // Repeindre la grille après avoir chargé une nouvelle image
    }


    public void resizeGrid(int newWidth, int newHeight) {
        // Créer un nouveau tableau temporaire avec la nouvelle taille
        Color[][] newGridColors = new Color[newWidth][newHeight];

        // Copier les couleurs de l'ancienne grille dans la nouvelle (dans les limites des dimensions)
        for (int x = 0; x < Math.min(GRID_WIDTH, newWidth); x++) {
            for (int y = 0; y < Math.min(GRID_HEIGHT, newHeight); y++) {
                newGridColors[x][y] = gridColors[x][y];
            }
        }

        // Remplir les nouvelles cases avec du blanc par défaut
        for (int x = 0; x < newWidth; x++) {
            for (int y = 0; y < newHeight; y++) {
                if (newGridColors[x][y] == null) {
                    newGridColors[x][y] = Color.WHITE;
                }
            }
        }

        // Mettre à jour les dimensions de la grille
        GRID_WIDTH = newWidth;
        GRID_HEIGHT = newHeight;

        // Mettre à jour la grille
        gridColors = newGridColors;

        // Ajuster la taille de la zone de dessin
        setPreferredSize(new Dimension(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE));

        // Redessiner la fenêtre et ses composants
        revalidate();
        repaint();
    }

    public void resizeCell(int newSize){
        CELL_SIZE = newSize;
        setPreferredSize(new Dimension(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE));
        revalidate();
        repaint();
    }



    //getters setters

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((CELL_SIZE + 1) * GRID_WIDTH, (CELL_SIZE + 1) * GRID_HEIGHT);
    }

    public void setCurrentColor(Color color){
        this.currentColor = color;
    }

    public int getCellSize() {
        return CELL_SIZE;
    }
    public int getGridWidth(){
        return GRID_WIDTH;
    }

    public int getGridHeight(){
        return GRID_HEIGHT;
    }
}
