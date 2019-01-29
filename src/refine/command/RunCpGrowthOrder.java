package refine.command;


import refine.algorithm.CPGrowth;


public class RunCpGrowthOrder implements Order {
    private CPGrowth cpGrowth;

    public RunCpGrowthOrder(double alpha, double beta) {
        this.cpGrowth = new CPGrowth(alpha, beta);
    }

    @Override
    public void execute() {
        long start = System.currentTimeMillis();
        cpGrowth.mine();
        System.out.println("cost : " + (System.currentTimeMillis() - start) + "ms");
    }
}
