package com.lanyage.datamining.run;

import com.lanyage.datamining.datastructure.CPTreeNode;
import com.lanyage.datamining.factory.StrategyFactory;
import com.lanyage.datamining.strategy.IStringSplitStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 三 构建初始CP树和merge CPTree
 */
public class ConstructCPTreeAndMineThird {
    private static final Logger logger = LoggerFactory.getLogger(ConstructCPTreeAndMineThird.class);
    private static final String COUNT_OF_TRANSACTIONS_IN_BOTH_DATASET = "resources/COUNT_OF_TRANSACTIONS_IN_BOTH_DATASET";
    private static final String MIXED_DATASET = "resources/MIXED_DATASET";

    private static final IStringSplitStrategy STRATEGY = StrategyFactory.stringSplitStrategy();

    private CPTreeNode<Object> root;
    private int _1total = 0;
    private int _2total = 0;
    private Integer index = 0;

    public void initialize() {
        logger.info("————————————— ———————————— ———————————— ——————————— ——————————— —————————— ———————————— ———————————the beginning of initializing the cp tree with a root node");
        root = new CPTreeNode<>();
        root.setIndex(index++);
        root.setValue("root");
        root.set_1c(0);
        root.set_2c(0);
        root.setParent(null);
        root.setSibling(null);
        logger.info("the root node is {}", this.root);
        logger.info("————————————— ———————————— ———————————— ——————————— ——————————— —————————— ———————————— ———————————the end of initializing the cp tree with a root node");
    }

    public void createInitialCPTree() throws IOException {
        logger.info("————————————— ———————————— ———————————— ——————————— ——————————— —————————— ———————————— ———————————the beginning of constructing an cp tree with existing transactions");
        File file = new File(MIXED_DATASET);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

        String line;
        while ((line = br.readLine()) != null && !line.trim().equals("")) {
            String[] nodesAndTag = line.split(",");
            String nodesString = nodesAndTag[0];
            String classTag = nodesAndTag[1];
            logger.info("start——————current transaction is {}, classTag is {} ——————start", nodesString, classTag);
            nodeStringToItemSetAndAppendToRoot(nodesString, classTag);                                                  //根据(FDGB,1)类型的数据生成小树并添加到cptree上
            logger.info("end  ——————current transaction is {}, classTag is {} ——————  end", nodesString, classTag);
            if (classTag.equals("1")) {                                                                                 //纪录每个数据集的transaction的条数
                this._1total++;
            }
            if (classTag.equals("2")) {
                this._2total++;
            }
        }
        logger.info("————————————— ———————————— ———————————— ——————————— ——————————— —————————— ———————————— ———————————the end of constructing an cp tree with existing transactions");
    }

    private void nodeStringToItemSetAndAppendToRoot(String nodesString, String classTag) {
        List<CPTreeNode<Object>> nodes = STRATEGY.splitSequence(nodesString, classTag);                                 //采用采用StringSplitToCharactersStrategy策略进行字符串的切分处理
        setupRelationship(nodes);                                                                                       //给待添加到CPTree的ItemSet添加父子关系
        CPTreeNode<Object> head = nodes.get(0);                                                                         //node.size() > 0一定成立
        addSubTreeToParent(head, this.root);
    }

    public void flushTotalOfBothDataSetToFile() throws IOException {
        logger.info("————————————— ———————————— ———————————— ——————————— ——————————— —————————— ———————————— ———————————the beginning of counting numbers of both datasets");
        File file = new File(COUNT_OF_TRANSACTIONS_IN_BOTH_DATASET);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        bw.write(String.valueOf(this._1total));
        bw.newLine();
        bw.write(String.valueOf(this._2total));
        bw.newLine();
        bw.flush();
        logger.info("flush total of records of both datasets into file : {} and {}", this._1total, this._2total);
        logger.info("————————————— ———————————— ———————————— ——————————— ——————————— —————————— ———————————— ———————————the end of counting numbers of both datasets");
    }

    /**
     * 将所有的后缀融合到CPTree上去。
     * Correction of counts
     */
    public void merge() {
        logger.info("————————————— ———————————— ———————————— ——————————— ——————————— —————————— ———————————— ———————————the beginning of merging the subtrees to the cp tree");
        for (int i = 0; i < root.children().size(); i++) {
            CPTreeNode<Object> node = root.children().get(i);
            List<CPTreeNode<Object>> nodeChildren = node.children();
            for (int k = 0; k < nodeChildren.size(); k++) {
                List<CPTreeNode<Object>> nodeList = new ArrayList<>();
                CPTreeNode<Object> childK = nodeChildren.get(k);
                CPTreeNode<Object> newChildK = CPTreeNode.getInstance();
                newChildK.setValue(childK.value());
                newChildK.set_1c(childK._1c());
                newChildK.set_2c(childK._2c());
                nodeList.add(newChildK);
                copySubTree(nodeList, childK);                                                                          //([D],0,0)
                logger.info("————————————————————————————————————————————————————————————————————————————————————————");
                logger.info("find a subtree and the tree head is {}", newChildK);
                addSubTreeToParent(newChildK, this.root);                                                               //添加子树到cp tree并且
                logger.info("successfully merged subtree {} to {}", newChildK, this.root);
                logger.info("————————————————————————————————————————————————————————————————————————————————————————");
            }
            //break;
        }
        logger.info("————————————— ———————————— ———————————— ——————————— ——————————— —————————— ———————————— ———————————the end of merging the subtrees to the cp tree");

    }

    /**
     * <pre>
     * 拷贝子树，例:
     *
     *           [A]
     *            |
     *  [B—————————————————C]
     *   |                 |
     * [D—E]             [F—G]
     *
     * @param nodeList 节点的集合
     * </pre>
     */
    private void copySubTree(List<CPTreeNode<Object>> nodeList, CPTreeNode<Object> top) {
        int nodeChildrenSize = top.children().size();                                                                   //获取top的子节点的个数
        if (nodeChildrenSize == 0) {
            return;
        } else {
            List<CPTreeNode<Object>> topChildren = top.children();

            CPTreeNode<Object> parent = null;
            for (int i = 0; i < nodeList.size(); i++) {
                if (nodeList.get(i).value().equals(top.value())) {
                    parent = nodeList.get(i);
                    break;
                }
            }

            for (int i = 0; i < topChildren.size(); i++) {
                CPTreeNode<Object> child = topChildren.get(i);
                CPTreeNode<Object> newChild = CPTreeNode.getInstance();
                newChild.setValue(child.value());
                newChild.set_1c(child._1c());
                newChild.set_2c(child._2c());

                parent.children().add(newChild);
                newChild.setParent(parent);

                nodeList.add(newChild);
                copySubTree(nodeList, child);                                                                           //递归调用方法
            }

            for (int i = nodeList.size() - nodeChildrenSize + 1; i < nodeList.size(); i++) {                            //在这里父子关系已经从做好了，开始做兄弟关系
                CPTreeNode<Object> prev = nodeList.get(i - 1);
                CPTreeNode<Object> curr = nodeList.get(i);
                prev.setSibling(curr);
            }

            for (int i = 0; i < nodeChildrenSize; i++) {                                                                //兄弟关系设置好了，然后就可以移除后缀，回溯到上一层
                nodeList.remove(nodeList.size() - 1);
            }
        }
    }


    public void traverseRoot() {
        logger.info("————————————— ———————————— ———————————— ——————————— ——————————— —————————— ———————————— ———————————the beginning of traversing the cp tree");
        for (int i = 0; i < this.root.children().size(); i++) {
            traverse(new ArrayList<>(0), this.root.children().get(i));
        }
        logger.info("————————————— ———————————— ———————————— ——————————— ——————————— —————————— ———————————— ———————————the end of traversing the cp tree");
    }

    private void addSubTreeToParent(CPTreeNode<Object> subTreeHead, CPTreeNode<Object> parent) {                        //在这里父子关系已经从做好了，开始做兄弟关系
        if (subTreeHead == null || parent == null) {
            return;
        }
        if (parent.children().size() == 0) {                                                                            //如果root没有孩子节点
            CPTreeNode<Object> toAdd = subTreeHead;
            while (toAdd != null) {
                logger.info("add {} to {}'s children.", toAdd, parent);
                parent.children().add(toAdd);
                toAdd.setParent(parent);
                traverseAndAddIndexForNodes(toAdd);                                                                     //把toAdd添加index
                toAdd = toAdd.sibling();
            }
        } else {
            CPTreeNode<Object> toAdd = subTreeHead;
            while (toAdd != null) {
                int i;
                for (i = 0; i < parent.children().size(); i++) {
                    CPTreeNode<Object> currChild = parent.children().get(i);
                    if (parent.children().get(i).value().equals(toAdd.value())) {
                        currChild.set_1c(toAdd._1c() + currChild._1c());
                        currChild.set_2c(toAdd._2c() + currChild._2c());
                        logger.info("merge {} to {}, now the node is {}", toAdd, currChild.value(), currChild);
                        addSubTreeToParent(toAdd.children().size() == 0 ? null : toAdd.children().get(0), currChild);
                        break;                                                                                          //找到了就跳出循环，开始将兄弟节点添加到root
                    }
                }
                CPTreeNode<Object> next = toAdd.sibling();                                                              //此处也就是root没有child和toAdd的value等价，那么直接将toAdd加到root
                if (i == parent.children().size()) {
                    logger.info("add {} to {}'s children.", toAdd, parent);
                    parent.children().get(parent.children().size() - 1).setSibling(toAdd);
                    toAdd.setSibling(null);
                    parent.children().add(toAdd);
                    toAdd.setParent(parent);
                    traverseAndAddIndexForNodes(toAdd);                                                                 //把toAdd添加index
                }
                toAdd = next;
            }
        }
    }

    /**
     * 深度遍历游走cp tree打印itemsets
     *
     * @param nodeList 用来存储节点的临时列表，初始为[]
     * @param top      顶节点
     */
    private void traverse(List<CPTreeNode<Object>> nodeList, CPTreeNode<Object> top) {
        if (top == null) {
            return;
        } else {
            nodeList.add(top);
        }
        List<CPTreeNode<Object>> rootChildren = top.children();
        if (rootChildren.size() == 0) {
            logger.info("find transaction {}", nodeList);
            nodeList.remove(nodeList.size() - 1);
            return;
        }
        for (int i = 0; i < rootChildren.size(); i++) {
            CPTreeNode<Object> currChild = rootChildren.get(i);
            traverse(nodeList, currChild);
        }
        nodeList.remove(nodeList.size() - 1);
    }

    private void traverseAndAddIndexForNodes(CPTreeNode<Object> top) {
        if (top == null) {
            return;
        } else {
            top.setIndex(this.index++);
            logger.info("[{} set index {}]", top, this.index - 1);
        }
        List<CPTreeNode<Object>> rootChildren = top.children();
        if (rootChildren.size() == 0) {
            return;
        }
        for (int i = 0; i < rootChildren.size(); i++) {
            CPTreeNode<Object> currChild = rootChildren.get(i);
            traverseAndAddIndexForNodes(currChild);
        }
    }

    private void setupRelationship(List<CPTreeNode<Object>> nodeList) {
        if (nodeList.size() > 1) {                                                                                      //为一个Transaction中的Nodes设定父子关系
            for (int i = 1; i < nodeList.size(); i++) {
                CPTreeNode<Object> prev = nodeList.get(i - 1);
                CPTreeNode<Object> curr = nodeList.get(i);
                prev.children().add(curr);
                curr.setParent(prev);
            }
        } else {
            logger.info("nodeList size = 1，no need to set the parent-child relationship");
        }
    }

    public static void main(String[] args) throws IOException {
        ConstructCPTreeAndMineThird constructor = new ConstructCPTreeAndMineThird();
        constructor.initialize();                                                                                       //初始化创建根节点
        logger.info("|");
        logger.info("|");
        logger.info("|");
        constructor.createInitialCPTree();                                                                              //构建初始CP树
        logger.info("|");
        logger.info("|");
        logger.info("|");
        constructor.traverseRoot();
        logger.info("|");
        logger.info("|");
        logger.info("|");
        constructor.flushTotalOfBothDataSetToFile();                                                                    //将两个数据集的总transaction数
        logger.info("|");
        logger.info("|");
        logger.info("|");
        constructor.merge();
        logger.info("|");
        logger.info("|");
        logger.info("|");
        constructor.traverseRoot();
    }
}

