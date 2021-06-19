import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class CallList {
    private final TreeMap<Date, String> missedCalls;              // these date & number to be united into an integral Call class
                                                                  // lists for other calls intended as well
    private Date virtualInternalTime =                            // simulating of initial date on device
            new Date((long) (39.9 * 365.25 * 24 * 60 * 60 * 1000));
    SimpleDateFormat listDateFormat = new SimpleDateFormat("E d MMMM HH:mm");

    public CallList() {
        missedCalls = new TreeMap<>();
    }
    public CallList(long startTime) {
        this();
        virtualInternalTime.setTime(startTime);
    }

    void takeMissedCall(String number) {
        missedCalls.put(new Date(virtualInternalTime.getTime()), number);
    }

    void promoteVirtualTime(long period) {          // simulating of a time period to pass
        virtualInternalTime.setTime(virtualInternalTime.getTime() + period);
    }

    String[] giveMissedCalls(ContactBase processedThrough) {
        String[] lines = new String[missedCalls.size()];
        Iterator<Map.Entry<Date, String>> it = missedCalls.entrySet().iterator();
        for (int i = 0; it.hasNext(); i++) {
            Map.Entry<Date, String> next = it.next();
            lines[i] = listDateFormat.format(next.getKey()) + " " +
                    processedThrough.tryToGetNameFor(next.getValue());
        }
        return lines;
    }
    void clear() {
        missedCalls.clear();
    }
}
