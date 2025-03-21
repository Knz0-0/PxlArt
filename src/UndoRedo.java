import java.awt.*;
import java.util.ArrayList;

public class UndoRedo {
    private final ArrayList<Color[][]> versions;
    private final int MAX_NB_ACTIONS = 10;
    private int currentStateIndex;
    private final Dessin dessin;

    UndoRedo(Dessin dessin) {
        this.dessin = dessin;
        this.versions = new ArrayList<>();
        // Sauvegarde une **copie** de l’état initial
        versions.add(copyGrid(dessin.getGridColors()));
        currentStateIndex = 0;
    }

    public void saveState() {
        // Supprime les états futurs si on est en train de dessiner après un undo
        while (versions.size() > currentStateIndex + 1) {
            versions.remove(versions.size() - 1);
        }

        // Ajoute un nouvel état (en copiant les valeurs pour éviter les références)
        versions.add(copyGrid(dessin.getGridColors()));
        currentStateIndex++;

        // Gère la limite MAX_NB_ACTIONS
        if (versions.size() > MAX_NB_ACTIONS) {
            versions.remove(0);
            currentStateIndex--;
        }
    }

    public void undo() {
        if (isUndoPossible()) {
            currentStateIndex--;
            dessin.setGridColors(copyGrid(versions.get(currentStateIndex)));
        } else {
            System.err.println("undo : impossible");
        }
    }

    public void redo() {
        if (isRedoPossible()) {
            currentStateIndex++;
            dessin.setGridColors(copyGrid(versions.get(currentStateIndex)));
        } else {
            System.err.println("redo : impossible");
        }
    }

    public boolean isUndoPossible() {
        return currentStateIndex > 0;
    }

    public boolean isRedoPossible() {
        return currentStateIndex < versions.size() - 1;
    }

    // Fonction utilitaire pour copier la grille (évite la modification par référence)
    private Color[][] copyGrid(Color[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        Color[][] newGrid = new Color[rows][cols];

        for (int i = 0; i < rows; i++) {
            System.arraycopy(grid[i], 0, newGrid[i], 0, cols);
        }

        return newGrid;
    }
}
