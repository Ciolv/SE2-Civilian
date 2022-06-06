package Plugin.Task;

public class TaskTimer {
    private int individualDuration;
    private int remainingDuration;
    private int waitingTime = 0;

    public void increaseWaitingTime() {
        waitingTime += 1;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void decreaseDuration() {
        remainingDuration -= 1;
    }

    public boolean durationExceeded() {
        return remainingDuration <= 0;
    }
    public int getIndividualDuration() { return individualDuration; }

    public TaskTimer(int defaultDuration, double speedFactor) {
        individualDuration = (int)Math.round(defaultDuration / speedFactor);

        if (individualDuration == 0) {
            individualDuration = 1;
        }

        remainingDuration = individualDuration;
    }
}
