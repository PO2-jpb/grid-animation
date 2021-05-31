package pt.ipbeja.estig.fifteen.model;

import org.junit.jupiter.api.Test;
import pt.ipbeja.estig.fifteen.gui.View;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    @Test
    void moveHero() {
        Model model = new Model(new View() {
            @Override
            public void notifyView(int[][] board) {
                // does nothing
            }

            @Override
            public void showMonsterPosition(Monster randMonster) {
                // does nothing
            }
        });
        Position beginPos = model.getHero().getPos();
        model.moveHeroInDirection(Direction.RIGHT);
        Position expectedPos = new Position(beginPos.getLine(), beginPos.getCol() + 1);
        assertEquals(expectedPos, model.getHero().getPos());

        beginPos = model.getHero().getPos();
        model.moveHeroInDirection(Direction.DOWN);
        expectedPos = new Position(beginPos.getLine() + 1, beginPos.getCol());
        assertEquals(expectedPos, model.getHero().getPos());

        beginPos = model.getHero().getPos();
        model.moveHeroInDirection(Direction.LEFT);
        expectedPos = new Position(beginPos.getLine(), beginPos.getCol() - 1);
        assertEquals(expectedPos, model.getHero().getPos());

        beginPos = model.getHero().getPos();
        model.moveHeroInDirection(Direction.UP);
        expectedPos = new Position(beginPos.getLine() - 1, beginPos.getCol());
        assertEquals(expectedPos, model.getHero().getPos());
    }

    @Test
    void moveMonsters() {
        Model model = new Model(new View() {
            @Override
            public void notifyView(int[][] board) {
                // does nothing
            }
            @Override
            public void showMonsterPosition(Monster randMonster) {
                System.out.println(randMonster);
            }
        });

        for (int i = 0; i < 5; i++) {
            System.out.println(model.addNewMonster());
        }
        System.out.println("------------------------");

        Thread t = model.moveMonsters(5);
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}