package Plugin.Task;

public class TaskTimer {
    private double individualDuration;
    private double remainingDuration;
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
    public double getIndividualDuration() { return individualDuration; }

    public TaskTimer(int defaultDuration, double speedFactor) {
        individualDuration = defaultDuration * speedFactor;
        remainingDuration = individualDuration;
    }
}
