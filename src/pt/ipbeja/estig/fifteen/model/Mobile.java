package pt.ipbeja.estig.fifteen.model;

public abstract class Mobile {
    private Position pos;

    public Mobile(Position pos) {
        this.pos = pos;
    }

    public Position getPos() {
        return pos;
    }

    public void move(Direction dir) {
        switch (dir) {
            case UP: pos = new Position(pos.getLine() - 1, pos.getCol()); break;
            case DOWN: pos = new Position(pos.getLine() + 1, pos.getCol()); break;
            case LEFT: pos = new Position(pos.getLine(), pos.getCol() - 1); break;
            case RIGHT: pos = new Position(pos.getLine(), pos.getCol() + 1); break;
        }
    }

    public void moveTo(Position neighborPosition) {
        this.pos = neighborPosition;
    }
}
