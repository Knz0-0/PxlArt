import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PxlArtApp extends JFrame {

    private Dessin dessin;
    private UndoRedo versionsManager;
    private Color[] colors = {Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};
    private JPanel headerPanel;
    private JPanel colorPanel;

    public PxlArtApp() {
        setLayout(new BorderLayout());
        dessin = new Dessin();
        versionsManager = new UndoRedo(dessin);

        dessin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                versionsManager.saveState();
            }
        });




        // Création du header
        headerPanel = new JPanel();
        add(headerPanel, BorderLayout.NORTH);



        // Zone de dessin
        JPanel drawingPanelWrapper = new JPanel();
        drawingPanelWrapper.setLayout(new GridBagLayout());
        drawingPanelWrapper.add(dessin);

        // Panneau de sélection des couleurs
        colorPanel = new JPanel();
        add(colorPanel, BorderLayout.SOUTH);
        createColorButtons(); // Création des boutons de couleur

        // Ajout du panneau de dessin centré
        add(drawingPanelWrapper, BorderLayout.CENTER);

        // Boutons principaux
        addMainButtons();

        // Créer les spinners pour ajuster la largeur et la hauteur de la grille
        JSpinner widthSpinner = new JSpinner(new SpinnerNumberModel(dessin.getGridWidth(), 1, 32, 1));
        JSpinner heightSpinner = new JSpinner(new SpinnerNumberModel(dessin.getGridHeight(), 1, 32, 1));
        JSpinner cellSizeSpinner = new JSpinner(new SpinnerNumberModel(dessin.getCellSize(), 5, 30, 1));

        // Ajouter des labels pour chaque spinner
        JLabel widthLabel = new JLabel("Width:");
        JLabel heightLabel = new JLabel("Height:");
        JLabel cellSizeLabel = new JLabel("Zoom:");

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

        // Paramètres de la fenêtre
        setTitle("PxlArt Editor");
        try {
            BufferedImage icon = ImageIO.read(new File("./logo.png"));
            setIconImage(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setResizable(false);
    }

    private void addMainButtons() {
        // Boutons du header
        addButton(headerPanel, "Save image", e -> dessin.saveImage());
        // addButton(headerPanel, "Save real size image", e -> dessin.saveRealSizeImage());
        addButton(headerPanel, "Open", e -> dessin.openImage());
        addButton(colorPanel, "Clear", e -> dessin.clear());
        addButton(colorPanel, "Toggle grid", e -> dessin.toggleGrid());
        addButton(headerPanel, "Undo", e -> versionsManager.undo());
        addButton(headerPanel, "Redo", e -> versionsManager.redo());
    }

    private void createColorButtons() {
        for (int i = 0; i < colors.length; i++) {
            final int index = i;
            JButton colorButton = new JButton();
            colorButton.setBackground(colors[i]);
            colorButton.setPreferredSize(new Dimension(30, 30));

            // Clic gauche → sélectionne la couleur
            colorButton.addActionListener(e -> dessin.setCurrentColor(colors[index]));

            // Clic droit → ouvre le JColorChooser
            colorButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        Color newColor = JColorChooser.showDialog(null, "Choose a color", colors[index]);
                        if (newColor != null) {
                            colors[index] = newColor;
                            colorButton.setBackground(newColor);
                        }
                    }
                }
            });

            colorPanel.add(colorButton);
        }
    }

    private void addButton(JPanel panel, String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        panel.add(button);
    }

    public static void main(String[] args) {
        PxlArtApp app = new PxlArtApp();
        app.setVisible(true);
    }
}
