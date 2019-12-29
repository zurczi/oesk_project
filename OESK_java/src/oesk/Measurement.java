package oesk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Measurement {
    private List<Double> cpu = new ArrayList<>();
    private List<Double> memory = new ArrayList<>();
    private double time;
    private String name;

    public Measurement(String name){
        this.name = name;
    }
    public void addCPU(Double cpu){
        this.cpu.add(cpu);
    }

    public void addMemory(Double memory){
        this.memory.add(memory);
    }

    public void setTime(double time){
        this.time = time;
    }

    private double calculateAverage(List <Double> marks) {
        Double sum = 0.0;
        if(!marks.isEmpty()) {
            for (Double mark : marks) {
                sum += mark;
            }
            return sum / marks.size();
        }
        return sum;
    }

    private double returnMeanCpu(){
        return calculateAverage(this.cpu);
    }

    private double returnMaxMemory(){
        return Collections.max(this.memory);
    }

    @Override
    public String toString() {
        return this.name + " cpu: " + returnMeanCpu() + " memory: " + returnMaxMemory() + " time: " + time;
    }
}
