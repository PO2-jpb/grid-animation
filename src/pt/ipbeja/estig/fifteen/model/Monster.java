package pt.ipbeja.estig.fifteen.model;

public class Monster extends Mobile {


    public Monster(Position pos) {
        super(pos);
    }

    @Override
    public String toString() {
        return "Monster{" +
                "pos=" + this.getPos() +
                '}';
    }
}
