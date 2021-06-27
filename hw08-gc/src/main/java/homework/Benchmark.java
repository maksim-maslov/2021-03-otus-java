package homework;


import java.util.ArrayList;
import java.util.List;

class Benchmark implements BenchmarkMBean {

    void run() throws InterruptedException {
        List<String> list = new ArrayList<>();

        for (int c = 0; c < 5_000_000; c++) {
            list.add(new String(new char[1000]));

            if (c % 2 == 0) {
                list.remove(list.size() - 1);
            }

            if (c % 50 == 0) { // -Xmx256m : c % 50, -Xmx2048m : c % 500
                Thread.sleep(20);
            }
        }
    }
}
