package Plugin.Task;

public class TaskTimer {
    private int expectedDuration;
    private int remainingDuration;
    private int waitingTime = 0;

    public int getWaitingTime() {
        return waitingTime;
    }

    /**
     * Increase the {@link TaskTimer#waitingTime} by one
     */
    public void increaseWaitingTime() {
        waitingTime += 1;
    }

    /**
     * Decrease the {@link TaskTimer#remainingDuration} by one
     */
    public void decreaseDuration() {
        remainingDuration -= 1;
    }

    /**
     * @return True if the {@link TaskTimer#remainingDuration} is zero or below
     */
    public boolean durationExceeded() {
        return remainingDuration <= 0;
    }

    /**
     * @return The expected duration based
     */
    public int getExpectedDuration() { return expectedDuration; }

    public TaskTimer(int defaultDuration, double speedFactor) {
        // The expected duration should be based on the speedFactor but should be at least one
        expectedDuration = (int)Math.round(defaultDuration / speedFactor);
        if (expectedDuration == 0) {
            expectedDuration = 1;
        }

        remainingDuration = expectedDuration;
    }
}
