package pt.ipbeja.estig.gridanim.model;

import java.util.Objects;

/**
 * Mobile objects
 *
 * @author João Paulo Barros
 * @version 2021/05/31
 */
public abstract class Mobile {

    private String name;
    private Position pos;
    private boolean isMoving;

    public Mobile(String name, Position pos) {
        this.name = name;
        this.pos = pos;
        this.isMoving = false;
    }

    public String getName() {
        return name;
    }

    public Position getPos() {
        return pos;
    }

    public Position move(Direction dir) {
        switch (dir) {
            case UP: pos = new Position(pos.getLine() - 1, pos.getCol()); break;
            case DOWN: pos = new Position(pos.getLine() + 1, pos.getCol()); break;
            case LEFT: pos = new Position(pos.getLine(), pos.getCol() - 1); break;
            case RIGHT: pos = new Position(pos.getLine(), pos.getCol() + 1); break;
        }
        return pos;
    }

    public void moveTo(Position neighborPosition) {
        if (! neighborPosition.isInside()) System.out.println("ERRO!!!!!!!!!!!!!!!!!!!!!!!!");
        this.pos = neighborPosition;
    }

    public boolean isMoving() {
        return this.isMoving;
    }

    public void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mobile mobile = (Mobile) o;
        return Objects.equals(name, mobile.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
