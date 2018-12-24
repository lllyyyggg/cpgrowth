package com.lanyage.datamining.datastructure;

/**
 * ————————————————————————————————————————————————————————————
 * Node-code类，封装了一个Item对应的所有节点计数和前后序索引
 *
 * @author lanyage
 * @see com.lanyage.datamining.entity.CPNodeListCreator
 * ————————————————————————————————————————————————————————————
 **/
public class SequenceSuffix {
    private Integer preIndex;
    private Integer postIndex;
    private Integer c1;
    private Integer c2;
    private Object sequence;
    private CPTreeNode<Object> node;

    public SequenceSuffix() {
        this.c1 = 0;
        this.c2 = 0;
    }

    public SequenceSuffix(Integer preIndex, Integer postIndex, Integer c1, Integer c2) {
        this.preIndex = preIndex;
        this.postIndex = postIndex;
        this.c1 = c1;
        this.c2 = c2;
    }

    public CPTreeNode<Object> node() {
        return node;
    }

    public void setNode(CPTreeNode<Object> node) {
        this.node = node;
    }

    public Object sequence() {
        return sequence;
    }

    public void setSequence(Object sequence) {
        this.sequence = sequence;
    }

    public Integer preIndex() {
        return preIndex;
    }

    public void setPreIndex(Integer preIndex) {
        this.preIndex = preIndex;
    }

    public Integer postIndex() {
        return postIndex;
    }

    public void setPostIndex(Integer postIndex) {
        this.postIndex = postIndex;
    }

    public Integer c1() {
        return c1;
    }

    public void setC1(Integer c1) {
        this.c1 = c1;
    }

    public Integer c2() {
        return c2;
    }

    public void setC2(Integer c2) {
        this.c2 = c2;
    }

    @Override
    public String toString() {
        return "(" +
                sequence + "," +
                preIndex + ","
                + postIndex + ","
                + c1 + ","
                + c2 +
                ")";
    }
}
