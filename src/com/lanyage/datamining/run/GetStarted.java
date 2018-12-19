package com.lanyage.datamining.run;

import java.io.IOException;

public class GetStarted {
    public static void main(String[] args) throws IOException {
        Executor executor = new CPGrowthExecutor();
        executor.execute();
    }
}
