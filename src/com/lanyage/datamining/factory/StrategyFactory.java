package com.lanyage.datamining.factory;

import com.lanyage.datamining.enums.StringSplitStrategyEnum;
import com.lanyage.datamining.strategy.IStringSplitStrategy;

public class StrategyFactory {
    public static IStringSplitStrategy stringSplitStrategy() {
        return StringSplitStrategyEnum.CHARACTER_STRATEGY.getInstance();
    }
}
