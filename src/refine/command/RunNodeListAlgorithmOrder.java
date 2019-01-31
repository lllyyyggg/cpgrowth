package refine.command;

import refine.algorithm.MineFromNodeList;
public class RunNodeListAlgorithmOrder implements Order {
    private MineFromNodeList mineFromNodeList;
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
