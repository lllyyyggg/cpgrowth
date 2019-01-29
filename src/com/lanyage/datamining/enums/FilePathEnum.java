package com.lanyage.datamining.enums;

import java.util.HashMap;
import java.util.Map;

public enum FilePathEnum {
    DATA_SET_I("resources/DATASET_I"),
    DATA_SET_II("resources/DATASET_II"),
    MIX_DATASET("resources/MIXED_DATASET"),
    ITEM_COUNT_FILE("resources/ITEMSCOUNT_FILE");

    private static Map<String, String> CACHE = new HashMap<>();

    static {
        CACHE.put("dataset1", DATA_SET_I.source);
        CACHE.put("dataset2", DATA_SET_II.source);
        CACHE.put("itemcount", ITEM_COUNT_FILE.source);
        CACHE.put("mixeddataset", MIX_DATASET.source);
    }

    private String source;

    FilePathEnum(String source) {
        this.source = source;
    }

    public static String getPath(String key) {
        return CACHE.get(key);
    }
}
