import bagel.*;
import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.io.FileReader;

/**
 * Class representing a level in a game
 *
 * @author Victoria Halim
 */

public abstract class Level {
    private final Image BACKGROUND_IMAGE;
    private final String WORLD_FILE_PATH;

    // list of currently existing non-character game objects in an instance of the level
    private ArrayList<GameObject> levelObjects = new ArrayList<>();

    // list of currently existing demon-type game enemies in an instance of the level
    private ArrayList<AbstractDemon> demonEnemies = new ArrayList<>();

    // names of each game object
    private final static String PLAYER_NAME = "Fae";
    private final static String SINKHOLE_NAME = "Sinkhole";
    private final static String WALL_NAME = "Wall";
    private final static String TREE_NAME = "Tree";
    private final static String DEMON_NAME = "Demon";
    private final static String NAVEC_NAME = "Navec";

    // to initialize player
    private Player player;

    // flag for possible lose condition
    private boolean isNavecKilled = false;

    // is level running
    private boolean isRunning = false;

    // timescale constants
    private final static int MAX_TIMESCALE = 3;
    private final static int MIN_TIMESCALE = -3;
    private int timescale = 0;

    public Level(String BACKGROUND_IMAGE_SRC, String WORLD_FILE_PATH) {
        this.BACKGROUND_IMAGE = new Image(BACKGROUND_IMAGE_SRC);
        this.WORLD_FILE_PATH = WORLD_FILE_PATH;
        readCSV();
    }

    /**
     * Method used to read CSV file of game object coordinates, and to create these game objects.
     */
    public void readCSV() {
        try (Scanner file = new Scanner(new FileReader(WORLD_FILE_PATH));) {
            String info;

            // to help initialize characters
            Point playerCoordinates = new Point();
            Point navecCoordinates = null;
            ArrayList<Point> demonPoints = new ArrayList<>();

            // game character movement bounds
            Point topLeftBound = new Point();
            Point bottomRightBound = new Point();

            while (file.hasNextLine()) {
                info = file.nextLine();
                String[] fields = info.split(",");

                String type = fields[0];
                int x = Integer.parseInt(fields[1]);
                int y = Integer.parseInt(fields[2]);

                Point coordinate = new Point(x, y);

                if (type.equals(PLAYER_NAME)) {
                    playerCoordinates = coordinate;
                } else if (type.equals(WALL_NAME)) {
                    Wall newWall = new Wall(coordinate);
                    levelObjects.add(newWall);
                } else if (type.equals(SINKHOLE_NAME)) {
                    Sinkhole newSinkhole = new Sinkhole(coordinate);
                    levelObjects.add(newSinkhole);
                } else if (type.equals(TREE_NAME)) {
                    Tree newTree = new Tree(coordinate);
                    levelObjects.add(newTree);
                } else if (type.equals(DEMON_NAME)) {
                    demonPoints.add(coordinate);
                } else if (type.equals(NAVEC_NAME)) {
                    navecCoordinates = coordinate;
                }
                else if (type.equals("TopLeft")) {
                    topLeftBound = coordinate;
                } else if (type.equals("BottomRight")) {
                    bottomRightBound = coordinate;
                }
            }

            // initialize characters with bounds
            for (Point point: demonPoints) {
                Demon newDemon = new Demon(point, topLeftBound, bottomRightBound);
                demonEnemies.add(newDemon);
            }

            player = new Player(playerCoordinates, topLeftBound, bottomRightBound);
            if (navecCoordinates != null) {
                demonEnemies.add(new Navec(navecCoordinates, topLeftBound, bottomRightBound));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Performs a state update on the level
     *
     * @param input  user input with which various state updates will be performed
     */
    public void update(Input input) {
        BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);

        // check for whether any non-character level objects collide with the player
        for (Iterator<GameObject> iterator = levelObjects.iterator(); iterator.hasNext(); ) {
            GameObject levelObject = iterator.next();
            if (levelObject.collideWith(player) && (levelObject instanceof Collideable)) {
                ((Collideable) levelObject).collisionAction(player);

                if (levelObject.isKilled()) {
                    iterator.remove();
                }
            }
        }

        // check for whether any non-character level objects collide with the demon enemies
        for (Iterator<GameObject> iterator = levelObjects.iterator(); iterator.hasNext(); ) {
            GameObject levelObject = iterator.next();
            for (AbstractDemon demon: demonEnemies) {
                if (levelObject.collideWith(demon) && (levelObject instanceof Collideable)) {
                    ((Collideable) levelObject).collisionAction(demon);

                    if (levelObject.isKilled()) {
                        iterator.remove();
                    }
                }
            }
        }

        // updates demon enemy behavior in relation to player and vice versa
        for (Iterator<AbstractDemon> iterator = demonEnemies.iterator(); iterator.hasNext(); ) {
            AbstractDemon demonEnemy = iterator.next();
            demonEnemy.update(player);

            if (player.collideWith(demonEnemy)) {
                player.collisionAction(demonEnemy);
            }

            if (demonEnemy.isKilled()) {
                if (demonEnemy instanceof Navec) {
                    isNavecKilled = true;
                }
                iterator.remove();
            }
        }

        // render all game objects in the level
        player.renderImage();
        for (GameObject levelObject: levelObjects) {
            levelObject.renderImage();
        }
        for (Character character: demonEnemies) {
            character.renderImage();
        }

        // update player status
        player.update(input);

        // update timescale settings of demon enemies
        if (input.wasReleased(Keys.K)) {
            decreaseTimeScale();
        } else if (input.wasReleased(Keys.L)) {
            increaseTimeScale();
        }
    }

    /**
     * Return true if the player has lost the level
     *
     * @return  whether player lost the level
     */
    public boolean isLevelLost() {
        return player.isKilled();
    }

    /**
     * Return true if the player has won the level (conditions for winning differ in each level and thus must be
     * implemented in subclasses)
     */
    public abstract boolean isLevelWon();

    /**
     *  Increases level timescale by 1
     */
    private void increaseTimeScale() {
        if (timescale + 1 <= MAX_TIMESCALE) {
            timescale++;
            for (AbstractDemon demonEnemy: demonEnemies) {
                demonEnemy.changeMovementSpeed(timescale);
            }
            System.out.println("Sped up, Speed:  " + timescale);
        }
    }

    /**
     *  Decreases level timescale by 1
     */
    private void decreaseTimeScale() {
        if (timescale - 1 >= MIN_TIMESCALE) {
            timescale--;
            for (AbstractDemon demonEnemy: demonEnemies) {
                demonEnemy.changeMovementSpeed(timescale);
            }
            System.out.println("Slowed down, Speed:  " + timescale);
        }
    }

    /**
     * Start running the level
     */
    public void run() {
        isRunning = true;
    }

    /**
     * Stop running the level
     */
    public void stopRunning() {
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isNavecKilled() {
        return isNavecKilled;
    }

    public Player getPlayer() {
        return player;
    }
}
