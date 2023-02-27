/**
 * Class representing a timer, with duration in milliseconds unless stated otherwise
 *
 * @Victoria Halim
 */

public class Timer {
    // number of times Bagel renders a frame in a second
    private final static int FRAME_PER_SECOND = 60;
    private final static int MILLISECONDS_PER_SECOND = 1000;

    // how long the timer should run for, in milliseconds
    private final int DURATION;

    // how long the timer should run for in terms of number of times frames are refreshed
    private final int FRAMES_DURATION;
    private int framesElapsed = 0;

    private boolean isRunning = false;

    public Timer(int DURATION) {
        this.DURATION = DURATION;
        this.FRAMES_DURATION = durationToFrames(DURATION);
    }

    /**
     * Returns the number of frames Bagel would render in a certain duration
     *
     * @param duration  milliseconds that are getting converted to frames
     * @return          duration converted to number of frames that must be elapsed
     */
    public int durationToFrames(int duration) {
        return duration/MILLISECONDS_PER_SECOND * FRAME_PER_SECOND;
    }

    /**
     * Starts the timer
     */
    public void startTimer() {
        isRunning = true;
    }

    /**
     * Increments the number of frames elapsed by 1 and returns true. But if the timer has run for its set duration,
     * stops the timer and returns false.
     *
     * @return  whether the timer is still running
     */
    public boolean incrementFrame() {
        framesElapsed++;
        if (framesElapsed == FRAMES_DURATION) {
            stopTimer();
            return false;
        }

        return true;
    }

    /**
     * Stops the timer
     */
    private void stopTimer() {
        framesElapsed = 0;
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
