public class StepDevice {

    public final long startInterval;
    public final long intervalsPerStep;
    private long currentInterval;
    private long stepsTaken;

    public StepDevice(long startInterval, long intervalsPerStep) {
        this.startInterval = startInterval;
        this.intervalsPerStep = intervalsPerStep;
        currentInterval = startInterval;
        stepsTaken = 0;
    }

    public StepDevice(long intervalsPerStep) {
        this(0, intervalsPerStep);
    }

    public boolean setInterval(long interval) {
        currentInterval = interval;
        if (currentInterval - startInterval - stepsTaken * intervalsPerStep >= intervalsPerStep) {
            stepsTaken++;
            return true;
        }
        return false;
    }

    public boolean addInterval(long interval) {
        return setInterval(currentInterval + interval);
    }
}
