package com.lanyage.datamining.entity;


import java.util.Objects;

public class PrunePrefix {
    private Object value;
    private Integer c1;
    private Integer c2;

    public PrunePrefix() {
    }

    public PrunePrefix(Object value, Integer c1, Integer c2) {
        this.value = value;
        this.c1 = c1;
        this.c2 = c2;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Integer getC1() {
        return c1;
    }

    public void setC1(Integer c1) {
        this.c1 = c1;
    }

    public Integer getC2() {
        return c2;
    }

    public void setC2(Integer c2) {
        this.c2 = c2;
    }

    @Override
    public String toString() {
        return "(" +
                value + "," +
                c1 + "," +
                c2 +
                ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrunePrefix prefix = (PrunePrefix) o;
        return Objects.equals(value, prefix.value) &&
                Objects.equals(c1, prefix.c1) &&
                Objects.equals(c2, prefix.c2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, c1, c2);
    }
}
