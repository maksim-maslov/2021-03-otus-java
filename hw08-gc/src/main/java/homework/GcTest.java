package homework;

import com.sun.management.GarbageCollectionNotificationInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

public class GcTest {
    public static void main(String... args) throws Exception {

        List<String> JMXparams = ManagementFactory.getRuntimeMXBean().getInputArguments();
        System.setOut(new PrintStream(new FileOutputStream("logs" + File.separator + JMXparams.get(3).substring(8) + "-" + JMXparams.get(8).substring(4) + "-" + new SimpleDateFormat("dd_MM_HH_mm_ss").format(new Date()) + ".log")));

        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        switchOnMonitoring();
        long beginTime = System.currentTimeMillis();

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("homework:type=Benchmark");

        Benchmark mbean = new Benchmark();
        mbs.registerMBean(mbean, name);
        mbean.run();

        System.out.println("time:" + (System.currentTimeMillis() - beginTime) / 1000);
    }

    private static void switchOnMonitoring() {

        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();

        GCRunsStatisticCollector gcRunsStatisticCollector = new GCRunsStatisticCollector();
        gcRunsStatisticCollector.statistics.put("allTime", 0);

        for (GarbageCollectorMXBean gcbean : gcbeans) {

            System.out.println("GC name:" + gcbean.getName());
            gcRunsStatisticCollector.statistics.put(gcbean.getName(), 0);

            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    String gcCause = info.getGcCause();

                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");

                    long endTime = info.getGcInfo().getEndTime();
                    long pacing = endTime - gcRunsStatisticCollector.getLastTimestampRunGC();
                    gcRunsStatisticCollector.setLastTimestampRunGC(endTime);

                    long timeForGCCollection = (long) (duration * (60000f / pacing));

                    gcRunsStatisticCollector.statistics.put(gcbean.getName(), gcRunsStatisticCollector.statistics.get(gcbean.getName()) + 1);

                    int allTime = gcRunsStatisticCollector.statistics.get("allTime") + (int) duration;
                    gcRunsStatisticCollector.statistics.put("allTime", allTime);

                    for (Map.Entry entry : gcRunsStatisticCollector.statistics.entrySet()) {
                        if (!entry.getKey().equals("allTime")) {
                            System.out.print(entry.getKey() + ":" + entry.getValue() + ", ");
                        }
                    }

                    System.out.print("Time for collection (in min):" + timeForGCCollection + " ms");
                    System.out.println(", all time:" + gcRunsStatisticCollector.statistics.get("allTime") + " ms");
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }

    private static class GCRunsStatisticCollector {
        Map<String, Integer> statistics = new HashMap<>();
        private long lastTimestampRunGC = 0;

        public long getLastTimestampRunGC() {
            return lastTimestampRunGC;
        }

        public void setLastTimestampRunGC(long lastTimestampRunGC) {
            this.lastTimestampRunGC = lastTimestampRunGC;
        }
    }
}
