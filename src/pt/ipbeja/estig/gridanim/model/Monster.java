package pt.ipbeja.estig.gridanim.model;

public class Monster extends Mobile {


    public Monster(String name, Position pos) {
        super(name, pos);
    }

    @Override
    public String toString() {
        return "Monster{ " + getName() + ", " +
                "pos=" + this.getPos() +
                " }";
    }
}
