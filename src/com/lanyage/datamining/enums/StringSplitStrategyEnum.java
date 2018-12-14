package com.lanyage.datamining.enums;

import com.lanyage.datamining.strategy.IStringSplitStrategy;
import com.lanyage.datamining.strategy.StringSplitBySpaceStrategy;
import com.lanyage.datamining.strategy.StringSplitToCharactersStrategy;

public enum  StringSplitStrategyEnum {
    CHARACTER_STRATEGY(StringSplitToCharactersStrategy.INSTANCE), STRING_STRATEGY(StringSplitBySpaceStrategy.INSTANCE);
    private IStringSplitStrategy instance;
    StringSplitStrategyEnum(IStringSplitStrategy instance) {
        this.instance = instance;
    }
    public IStringSplitStrategy getInstance() {
        return instance;
    }
}
