package com.lanyage.version.one.enums;

import com.lanyage.version.one.strategy.IStringSplitStrategy;
import com.lanyage.version.one.strategy.StringSplitBySpaceStrategy;
import com.lanyage.version.one.strategy.StringSplitToCharactersStrategy;

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
