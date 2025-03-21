import java.awt.*;
import java.util.ArrayList;

public class UndoRedo {
    private ArrayList<Color[][]> versions;
    private final int MAX_NB_ACTIONS = 10;
    private int currentStateIndex;
    private Dessin dessin;



    UndoRedo(Dessin dessin){
        this.dessin = dessin;
        versions = new ArrayList<>();
        versions.add(dessin.getGridColors());
        currentStateIndex = 0;

    }


    public void saveState(){
        if (versions.size() < MAX_NB_ACTIONS) {
            // array add
            if (isRedoPossible()){
                for (int i = currentStateIndex; i < versions.size(); i++){
                    versions.remove(currentStateIndex);
                }
            }

            versions.add(dessin.getGridColors());
            currentStateIndex += 1;
        }else{
            // array push -> 10
            Color[][] temp = dessin.getGridColors();
            for (int i = MAX_NB_ACTIONS -1; i >= 0; i--) {
                Color[][] temp2 = versions.get(i);
                versions.set(i, temp);
                temp = temp2;
            }
        }
    }


    public void undo(){
        if (isUndoPossible()) {
            dessin.setGridColors(versions.get(currentStateIndex - 1));
            currentStateIndex -= 1;
        }else {
            System.err.println("undo : impossible");
        }
    }


    public void redo(){
        if (isRedoPossible()){
            dessin.setGridColors(versions.get(currentStateIndex + 1));
            currentStateIndex += 1;
        }else{
            System.err.println("redo : impossible");
        }
    }

    public boolean isUndoPossible(){
        return currentStateIndex > 1;
    }

    public boolean isRedoPossible(){
        return currentStateIndex < versions.size() -1;
    }

}
