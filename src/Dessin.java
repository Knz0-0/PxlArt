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
import java.util.ArrayList;

public class Dessin extends JPanel {
    private static int CELL_SIZE = 25;
    private static int GRID_WIDTH = 20;
    private static int GRID_HEIGHT = 20;
    private Color[][] gridColors;
    private Color currentColor = Color.BLACK;
    private boolean showGrid = false;
    private int paintMode = 0; // 0 : 1 pixel, 1 : fill

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
                if (paintMode == 0) colorierCellule(e);
                if (paintMode == 1) fill(e);
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

    /**
     * change le mode de dessin (peinceau, remplissage)
     */
    public void togglePaintMode(){
        paintMode = (paintMode == 0) ? 1 : 0;
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
            // Colorier la cellule avec la couleur actuelle 
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


    public void saveRealSizeImage() {
        // Création de l'image avec la vraie taille
        BufferedImage imageTailleReelle = new BufferedImage(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                for (int xr = 0; xr < CELL_SIZE; xr++) {
                    for (int yr = 0; yr < CELL_SIZE; yr++) {
                        imageTailleReelle.setRGB(x * CELL_SIZE + xr, y * CELL_SIZE + yr, gridColors[x][y].getRGB());
                    }
                }
            }
        }

        // Sélecteur de fichier
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer l'image en taille réelle");

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
                ImageIO.write(imageTailleReelle, "png", fileToSave);
                JOptionPane.showMessageDialog(null, "Image sauvegardée sous : " + fileToSave.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur lors de la sauvegarde : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    /**
     * ouvre un fichier .png et permet de le modifier
     */
    public void openImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Ouvrir une image");

        // Filtre pour n'accepter que les fichiers PNG
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichiers PNG", "png");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File inputFile = fileChooser.getSelectedFile();

            try {
                BufferedImage image = ImageIO.read(inputFile);

                if (image == null) {
                    JOptionPane.showMessageDialog(null, "Le fichier sélectionné n'est pas une image valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Vérification de la taille max 32x32
                if (image.getWidth() > 32 || image.getHeight() > 32) {
                    JOptionPane.showMessageDialog(null, "Image trop grande ! Max : 32x32 pixels.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(null, "Image ouverte : " + inputFile.getPath());

                // Mise à jour de la grille en fonction de l'image
                GRID_WIDTH = image.getWidth();
                GRID_HEIGHT = image.getHeight();
                gridColors = new Color[GRID_WIDTH][GRID_HEIGHT];

                for (int x = 0; x < GRID_WIDTH; x++) {
                    for (int y = 0; y < GRID_HEIGHT; y++) {
                        gridColors[x][y] = new Color(image.getRGB(x, y));
                    }
                }

                repaint(); // Repeindre la grille après chargement

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur lors de l'ouverture : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
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

    public Color[][] getGridColors() {
        return gridColors;
    }

    public void setGridColors(Color[][] gridColors) {
        this.gridColors = gridColors;
        revalidate();
        repaint();
    }

    public Color getColor(int x, int y){
        // on suppose que les coordonnees sont valides
        return gridColors[x][y];
    }

    public void setColor(int x, int y, Color color){
        // on suppose que les coordonnees sont valides
        gridColors[x][y] = color;
    }

    public void fill(MouseEvent e){
        int x = e.getX() / (CELL_SIZE + 1);
        int y = e.getY() / (CELL_SIZE + 1);

        Color colorToReplace = gridColors[x][y];
        fillRecWorker(x, y, colorToReplace);

        revalidate();
        repaint();
    }

    private void fillRecWorker(int x, int y, Color colorToReplace){
        
        
        for (Coord coord : getAdjascentSquaresPlus(x, y)) {


            if(getColor(coord.getX(), coord.getY()) == colorToReplace){
                setColor(coord.getX(), coord.getY(), currentColor);
                fillRecWorker(coord.getX(), coord.getY(), colorToReplace);
            }
        }
        
    }


    private ArrayList<Coord> getAdjascentSquares(int x, int y){
        ArrayList<Coord> adjascentSquares = new ArrayList<>();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(x+i < 0 || x+i >= gridColors.length || y+j < 0 || y+j >= gridColors.length)) {
                    adjascentSquares.add(new Coord(x+i, y+j));
                }
            }
        }

        return adjascentSquares;
    }

    private ArrayList<Coord> getAdjascentSquaresPlus(int x, int y){
        ArrayList<Coord> adjascentSquares = new ArrayList<>();

        adjascentSquares.add(new Coord(x, y));
        if(x-1 >= 0) adjascentSquares.add(new Coord(x-1, y));
        if(x+1 < gridColors.length) adjascentSquares.add(new Coord(x+1, y));
        if(y-1 >= 0) adjascentSquares.add(new Coord(x, y-1));
        if(y+1 < gridColors.length) adjascentSquares.add(new Coord(x, y+1));

        return adjascentSquares;
    }

}
