package homework;


class Benchmark implements BenchmarkMBean {

    private volatile int size = 0;

    void run() throws InterruptedException {

        int local = size;
        Object[] array = new Object[local];


        for (int i = 0; i < local; i++) {

            array[i] = new String(new char[1000]);

            if (i % 2 == 0) {
                array[i] = null;
            }

            if (i % 50 == 0) {
                Thread.sleep(10); //Label_1
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        System.out.println("new size:" + size);
        this.size = size;
    }
}
