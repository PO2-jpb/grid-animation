package pt.ipbeja.estig.fifteen.model;

public class Monster extends Mobile {


    public Monster(String name, Position pos) {
        super(name, pos);
    }

    @Override
    public String toString() {
        return "Monster{" +
                "pos=" + this.getPos() +
                '}';
    }
}
