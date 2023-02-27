import bagel.*;
import bagel.util.Point;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 2, 2022
 *
 * @author Victoria Halim
 */

public class ShadowDimension extends AbstractGame {
    // window size specifications
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;

    // game start, win, and lose messages
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final static String START_MESSAGE = "PRESS SPACE TO START\nUSE ARROW KEYS TO FIND GATE";
    private final static String LEVEL_COMPLETE_MESSAGE = "LEVEL COMPLETE!";
    private final static String LEVEL_1_MESSAGE = "PRESS SPACE TO START\nPRESS A TO ATTACK\nDEFEAT NAVEC TO WIN\n";
    private final static int LEVEL_1_NUM_LINES = 3;
    private final static String WIN_MESSAGE = "CONGRATULATIONS!";
    private final static String LOSE_MESSAGE = "GAME OVER!";

    // game message coordinates
    private final static Point GAME_TITLE_COORDINATE = new Point(260, 250);
    private final static Point START_MESSAGE_COORDINATE = new Point(350, 440);
    private final static Point LEVEL_1_MESSAGE_COORDINATE = new Point(350, 350);

    // game fonts
    private final static int FONT_LARGE_SIZE = 75;
    private final static int FONT_MEDIUM_SIZE = 40;
    private final Font FONT_LARGE = new Font("res/frostbite.ttf", FONT_LARGE_SIZE);
    private final Font FONT_MEDIUM = new Font("res/frostbite.ttf", FONT_MEDIUM_SIZE);

    // flags to indicate states the game is in
    private boolean inStartScreen = true;

    // timer for how long the level complete message should run for
    private final static Timer LEVEL_COMPLETE_TIMER = new Timer(3000);

    // initializing levels of the game
    Level0 level0 = null;
    Level level1 = null;
    private boolean startLevel1 = false;

    public ShadowDimension(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }

    /**
     * Method used to get necessary coordinates to centre a given message on the game window.
     *
     * @param font     font the message would be drawn with
     * @param fontSize size of the font
     * @param message  message to draw
     * @return         bottom left point at which to draw the message
     * @see            Font
     */
    private Point centredMessageCoordinate(Font font, int fontSize, String message) {
        double x = Window.getWidth()/2 - font.getWidth(message)/2;
        double y = Window.getHeight()/2 + fontSize/2;

        Point coordinate = new Point(x, y);

        return coordinate;
    }

    /**
     * Draws a centre aligned message, given bottom left coordinates.
     *
     * @param font        font the message would be drawn with
     * @param fontSize    size of the font
     * @param message     message to draw
     * @param coordinates bottom left point at which to draw the message
     * @see               Font
     */
    private void centreAlign(Font font, int fontSize, String message, Point coordinates, int numLines) {
        int beginIndex = 0;
        int lineCount = 1;
        double x, y;

        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == '\n') {
                String currMessage = message.substring(beginIndex, i);
                x = Window.getWidth()/2 - font.getWidth(currMessage)/2;
                y = coordinates.y - (fontSize * (numLines - lineCount));

                FONT_MEDIUM.drawString(currMessage, x, y);

                beginIndex = i + 1;
                lineCount += 1;
            }
        }
    }

    /**
     * Renders game lose message
     */
    private void gameLoseMessage() {
        Point drawingCoordinate = centredMessageCoordinate(FONT_LARGE, FONT_LARGE_SIZE, LOSE_MESSAGE);
        FONT_LARGE.drawString(LOSE_MESSAGE, drawingCoordinate.x, drawingCoordinate.y);
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {
        // start screen implementation
        if (inStartScreen) {
            FONT_LARGE.drawString(GAME_TITLE, GAME_TITLE_COORDINATE.x, GAME_TITLE_COORDINATE.y);
            FONT_MEDIUM.drawString(START_MESSAGE, START_MESSAGE_COORDINATE.x, START_MESSAGE_COORDINATE.y);

            // exit start screen and proceed to game when space is pressed
            if (input.wasPressed(Keys.SPACE)) {
                inStartScreen = false;
                level0 = new Level0();
                level0.run();
            }
        }

        // running level0
        else if (level0.isRunning()) {
            if (level0.isLevelWon()) {
                // if level is won, show level complete message for the duration of LEVEL_COMPLETE_TIMER
                if (!LEVEL_COMPLETE_TIMER.isRunning()) {
                    LEVEL_COMPLETE_TIMER.startTimer();
                }
                if (!LEVEL_COMPLETE_TIMER.incrementFrame()) {
                    level0.stopRunning();
                    level1 = new Level1();
                }
                Point drawingCoordinate = centredMessageCoordinate(FONT_LARGE, FONT_LARGE_SIZE, LEVEL_COMPLETE_MESSAGE);
                FONT_LARGE.drawString(LEVEL_COMPLETE_MESSAGE, drawingCoordinate.x, drawingCoordinate.y);
            } else if (level0.isLevelLost()) {
                gameLoseMessage();
            } else {
                level0.update(input);
            }
        }

        // level 1 start screen
        else if (!level1.isRunning()) {
            centreAlign(FONT_MEDIUM, FONT_MEDIUM_SIZE, LEVEL_1_MESSAGE, LEVEL_1_MESSAGE_COORDINATE,
            LEVEL_1_NUM_LINES);

            // exit level start screen and start level 1 when space is pressed
            if (input.wasPressed(Keys.SPACE)) {
                level1.run();
            }
        }

        // running level1
        else if (level1.isRunning()) {
            if (level1.isLevelWon()) {
                Point drawingCoordinate = centredMessageCoordinate(FONT_LARGE, FONT_LARGE_SIZE, WIN_MESSAGE);
                FONT_LARGE.drawString(WIN_MESSAGE, drawingCoordinate.x, drawingCoordinate.y);
            } else if (level1.isLevelLost()) {
                gameLoseMessage();
            } else {
                level1.update(input);
            }
        }

        // quit game if escape is pressed
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
    }
}
