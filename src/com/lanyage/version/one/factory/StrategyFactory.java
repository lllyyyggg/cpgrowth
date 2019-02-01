package com.lanyage.version.one.factory;

import com.lanyage.version.one.enums.StringSplitStrategyEnum;
import com.lanyage.version.one.strategy.IStringSplitStrategy;

public class StrategyFactory {
    public static IStringSplitStrategy stringSplitStrategy() {
        return StringSplitStrategyEnum.STRING_STRATEGY.getInstance();
    }
}
