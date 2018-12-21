package com.lanyage.datamining.datastructure;

public class OrdersAndCounts {
    private Integer preIndex;
    private Integer postIndex;
    private Integer c1;
    private Integer c2;

    public OrdersAndCounts(Integer preIndex, Integer postIndex, Integer c1, Integer c2) {
        this.preIndex = preIndex;
        this.postIndex = postIndex;
        this.c1 = c1;
        this.c2 = c2;
    }

    @Override
    public String toString() {
        return "<" +
                preIndex + ","
                + postIndex + ","
                + c1 + ","
                + c2 +
                '>';
    }
}
