package oesk;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class MeasurementCPUandMemory extends Thread{
    private Measurement measurement;
    private long usageMemoryAtStart = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

    private boolean running = true;
    public MeasurementCPUandMemory(Measurement m){

        this.measurement = m;
    }

    @Override
    public void run() {
        OperatingSystemMXBean bean = (com.sun.management.OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();
        while (running) {
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println(((com.sun.management.OperatingSystemMXBean) bean).getProcessCpuLoad());
            this.measurement.addCPU(((com.sun.management.OperatingSystemMXBean) bean).getProcessCpuLoad());
           // System.out.println(((com.sun.management.OperatingSystemMXBean) bean).getProcessCpuLoad()/((com.sun.management.OperatingSystemMXBean) bean).getSystemCpuLoad());
            //System.out.println(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
            //System.out.println((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
            this.measurement.addMemory((double) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));

        }
    }

    public synchronized void doStop(){
        this.running = false;
    }



}
