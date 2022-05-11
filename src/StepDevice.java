// timer device which calls a step event on an interval
public class StepDevice {

    // step interval data
    public final long startInterval;
    public final long intervalsPerStep;

    // runtime data for when to step
    private long currentInterval;
    private long stepsTaken;

    // initialize timer device
    public StepDevice(long startInterval, long intervalsPerStep) {

        // store settings
        this.startInterval = startInterval;
        this.intervalsPerStep = intervalsPerStep;
        currentInterval = startInterval;
        stepsTaken = 0;
    }

    // initialize timer without start time
    public StepDevice(long intervalsPerStep) {
        this(0, intervalsPerStep);
    }

    // set time passed and check for step based on new time
    public boolean setInterval(long interval) {

        // store new timer
        currentInterval = interval;

        // check if interval passed
        if (currentInterval - startInterval - stepsTaken * intervalsPerStep >= intervalsPerStep) {

            // take a step
            stepsTaken++;
            return true;
        }

        // return no step
        return false;
    }

    // add time passed and check for step
    public boolean addInterval(long interval) {

        // calculate new time and set
        return setInterval(currentInterval + interval);
    }
}
