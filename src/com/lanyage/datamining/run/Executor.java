package com.lanyage.datamining.run;

import java.io.IOException;

public class Executor {
    public static void main(String[] args) throws IOException {
        CalculateItemCountFirst.main(new String[0]);
        SortItemsForItemSetSecond.main(new String[0]);
        ConstructCPTreeAndMineThird.main(new String[0]);
    }
}
