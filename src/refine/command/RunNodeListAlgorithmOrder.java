package refine.command;

import refine.algorithm.MineFromNodeList;
import refine.algorithm.MiningAlgorithm;
public class RunNodeListAlgorithmOrder implements Order {
    private MiningAlgorithm mineFromNodeList;
    public RunNodeListAlgorithmOrder(Double alpha, Double beta) {
        this.mineFromNodeList = new MineFromNodeList(alpha, beta);
    }
    @Override
    public void execute() {
        long start = System.currentTimeMillis();
        mineFromNodeList.mine();
        System.out.println("cost : " + (System.currentTimeMillis() - start) + "ms");
    }
}
