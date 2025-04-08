package Components;

import Main.Vector;
import Manager.MapManager.Cell;

public class Breaker extends ComponentClass {
      
    public Breaker(String name) {
        this.name = name;
    }
    
    public Breaker(Vector position, String name) {
        Init(position, name);
    }

    @Override
    public void Init(Vector position, String name) {
        this.position.SetVector(position);
        this.name = name;
    }

    public void Init(Cell cell) {
        this.position.SetVector(cell.absPos);
        this.name = cell.getName();
    }


    @Override
    public Breaker CreateInstance(Vector position, String name) {
        
        Breaker breaker = new Breaker(position, name);

        return breaker;


    }


}
