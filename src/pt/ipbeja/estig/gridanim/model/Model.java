package pt.ipbeja.estig.gridanim.model;

import pt.ipbeja.estig.gridanim.gui.View;

import java.util.*;

/**
 * board model
 *
 * @author João Paulo Barros
 * @version 2021/05/28
 */
final public class Model {
    private static final int TIME_IN_MILLS_BETWEEN_MOVES = 5;

    public static final int N_LINES = 20;
    public static final int N_COLS = 20;

    private final static Random RAND = new Random();
    private int[][] board;
    private Set<Position> freePositions;
    private Hero hero;

    private List<Monster> monsters;

    private View view;

    /**
     * Creates board in winning position
     */
    public Model(View view) {

        this.hero = new Hero("Hero", new Position(N_LINES / 2, N_COLS / 2));
        this.view = view;
        this.monsters = new ArrayList<>();
        this.freePositions = new HashSet<>();
        this.resetBoard();
    }

    /**
     * Creates a random mixed board starting from a winning position
     *
     * @param minIter        minimum number of iterations to mix board
     * @param additionalIter maximum number of additional iterations to mix board
     */
    public Model(View view, int minIter, int additionalIter) {
        this(view); // call default constructor Fifteen()
    }

    public Hero getHero() {
        return this.hero;
    }

    public Monster addNewMonster(String name) {
        Position freePos = this.getRandomFreePos();
        Monster monster = new Monster(name, freePos);
        this.monsters.add(monster);
        return monster;
    }

    public List<Monster> getMonsters() {
        return this.monsters;
    }


    public Thread moveMonsters(long nMovements) {
        Runnable r = () -> {
            for (long i = 0; i < nMovements; i++) {
                int randMonsterPosition = RAND.nextInt(this.monsters.size());
                Monster randMonster = this.monsters.get(randMonsterPosition);
                if (!randMonster.isMoving()) {
                    Position beginPos = randMonster.getPos();
                    Direction randomDirection = Direction.values()[RAND.nextInt(Direction.values().length)];
                    if (beginPos.isInsideAfter(randomDirection)) {
                        randMonster.setIsMoving(true);
                        Position endPos = beginPos.neighborPosition(randomDirection);
                        System.out.println("Going to update " + randMonster);
                        this.view.updateMove(randMonster, endPos);
                        Model.sleep(TIME_IN_MILLS_BETWEEN_MOVES);
                    }
                } else {
                    System.out.println(randMonster + " is moving");
                }
            }
            System.out.println("END");
        };
        Thread t = new Thread(r);
        t.start();
        return t;
    }

    public void moveMobile(Mobile mobile, Position endPos) {
        mobile.moveTo(endPos);
        mobile.setIsMoving(false);
        System.out.println("Updated " + mobile);
    }

    /**
     * Move hero in specified direction
     *
     * @param direction movement direction
     */
    public void moveHeroInDirection(Direction direction) {
        System.out.println(direction + " pressed ##################################");
        Position neighborPosition = this.hero.getPos().neighborPosition(direction);
        if (neighborPosition != null) {
            this.view.updateMove(this.hero, neighborPosition);
        }
    }

    /**
     * @return fifteen board content in text form
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int line = 0; line < N_LINES; line++) {
            for (int col = 0; col < N_COLS; col++) {
                s.append(String.format("%2d, ", this.board[line][col]));
            }
            s.setLength(s.length() - 2);
            s.append("\n");
        }
        return s.toString();
    }

    /**
     * get piece at given position
     *
     * @param position to get piece
     * @return the piece at position
     */
    public int pieceAt(Position position) {
        return this.board[position.getLine()][position.getCol()];
    }

    /**
     * Get all the pieces in a new list
     *
     * @return list with all pieces (line order)
     */
    public List<Integer> getBoard() {
        List<Integer> list = new ArrayList<>();
        for (int line = 0; line < N_LINES; line++) {
            for (int col = 0; col < N_COLS; col++) {
                list.add(this.board[line][col]);
            }
        }
        return list;
    }

    /**
     * Get a random posiiton that is free from monsters
     *
     * @return the random free position
     */
    private Position getRandomFreePos() {
        Position[] positions = new Position[this.freePositions.size()];
        this.freePositions.toArray(positions);
        return positions[RAND.nextInt(this.freePositions.size())];
    }

    /**
     * Puts the board in the starting position
     */
    private void resetBoard() {
        this.board = new int[Model.N_LINES][Model.N_COLS];
        int pos = 1;
        for (int line = 0; line < Model.N_LINES; line++) {
            for (int col = 0; col < Model.N_COLS; col++) {
                this.board[line][col] = pos++;
                this.freePositions.add(new Position(line, col)); // all are free
            }
        }
    }

    /**
     * Wait the specified time in milliseconds
     * More info at  https://stackoverflow.com/questions/26703324/why-do-i-need-to-handle-an-exception-for-thread-sleep
     *
     * @param sleepTime time in miliseconds
     */
    public static void sleep(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.out.println("Thread " + Thread.currentThread() + " was interrupted!");
            e.printStackTrace();
        }
    }

}
