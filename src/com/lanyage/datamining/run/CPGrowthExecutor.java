package com.lanyage.datamining.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CPGrowthExecutor implements Executor {
    public static final Logger LOGGER = LoggerFactory.getLogger(CPGrowthExecutor.class);

    @Override
    public void execute() throws IOException {
        CalculateItemCountFirst.main(new String[0]);
        SortItemsForItemSetSecond.main(new String[0]);
        ConstructCPTreeAndMineThird MINER = ConstructCPTreeAndMineThird.INSTANCE;
        MINER.initialize();                                                                                             //初始化创建根节点,并且将ITEM出现次数存到Map中
        MINER.createInitialCPTree();                                                                                    //构建初始CP树
        MINER.traverseRoot();                                                                                           //遍历ROOT，确保所有路径正确。如:FDGB
        MINER.merge();                                                                                                  //融合并挖掘
        MINER.traverseRoot();                                                                                           //遍历ROOT，确保所有路径正确。如:FDGB
        MINER.startMining();                                                                                            //开始挖掘
        LOGGER.info("INDEX {}", MINER.index - 1);
    }
}
