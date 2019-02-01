package com.lanyage.version.two.command;
import com.lanyage.version.two.algorithm.MineWithCPGrowth;
import com.lanyage.version.two.algorithm.MiningAlgorithm;

public class RunCpGrowthOrder implements Order {
    private MiningAlgorithm cpGrowth;
    public RunCpGrowthOrder(Double alpha, Double beta) { this.cpGrowth = new MineWithCPGrowth(alpha, beta); }
    @Override
    public void execute() {
        long start = System.currentTimeMillis();
        cpGrowth.mine();
        System.out.println("cost : " + (System.currentTimeMillis() - start) + "ms");
    }
}
