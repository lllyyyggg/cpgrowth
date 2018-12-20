package com.lanyage.datamining.enums;

public enum FilePathEnum {
    DATA_SET_I("resources/DATASET_I"),
    DATA_SET_II("resources/DATASET_II"),
    MIX_DATASET("resources/MIXED_DATASET"),
    ITEM_COUNT_FILE("resources/ITEMSCOUNT_FILE");
    private String source;
    FilePathEnum(String source) {
        this.source = source;
    }
    public String getSource() {
        return source;
    }
}
