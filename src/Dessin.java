import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Dessin extends JPanel {
    private static final int CELL_SIZE = 25;
    private static final int GRID_WIDTH = 20;
    private static final int GRID_HEIGHT = 20;
    private Color[][] gridColors;
    private Color currentColor = Color.BLACK;
    private boolean showGrid = false;

    public Dessin() {
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

    public void toggleGrid(){
        showGrid = !showGrid;
        repaint();
    }

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
                        g.setColor(Color.BLACK);
                        g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                    }

                }
            }
        }
    }


    // methode pour sauvegarder l'image
    public void saveImage(){
        /*//version 1 case = 1 px
        BufferedImage image = new BufferedImage(GRID_WIDTH, GRID_HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                image.setRGB(x, y, gridColors[x][y].getRGB());
            }
        }*/

        //version taille réelle
        BufferedImage imageTailleReelle = new BufferedImage(GRID_WIDTH * (CELL_SIZE + 1), GRID_HEIGHT * (CELL_SIZE + 1), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                for (int xr = 0; xr < CELL_SIZE; x++) {
                    for (int yr = 0; yr < CELL_SIZE; y++) {
                        imageTailleReelle.setRGB(x * CELL_SIZE + xr, y * CELL_SIZE + yr, gridColors[x][y].getRGB());

                        //debug
                        System.out.println("pixel (" + (x+xr) + " ; " + (y+yr) + ") écrit : " + gridColors[x][y]);
                    }
                }
            }
        }

        try {
            /*// Choisir un chemin pour sauvegarder le fichier
            File outputfile = new File("pixel_art_image.png"); // pour le test, l'image est sauvegardée à la racine du projet
            ImageIO.write(image, "png", outputfile);
            JOptionPane.showMessageDialog(this, "Image saved as pixel_art_image.png");*/


            File outputfileRealSize = new File("pixel_art_image_taille_reelle.png"); // pour le test, l'image est sauvegardée à la racine du projet
            ImageIO.write(imageTailleReelle, "png", outputfileRealSize);
            JOptionPane.showMessageDialog(this, "Image saved as pixel_art_image_taille_reelle.png");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving image: " + e.getMessage());
        }

    }

    public void openImage(){

    }















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
