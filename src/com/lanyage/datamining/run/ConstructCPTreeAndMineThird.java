package com.lanyage.datamining.run;

import com.lanyage.datamining.datastructure.CPTreeNode;
import com.lanyage.datamining.factory.StrategyFactory;
import com.lanyage.datamining.strategy.IStringSplitStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 三 构建初始CP树和merge CPTree
 */
public class ConstructCPTreeAndMineThird {
    public static final Logger LOGGER = LoggerFactory.getLogger(ConstructCPTreeAndMineThird.class);
    public static final String COUNT_OF_TRANSACTIONS_IN_BOTH_DATASET = "resources/COUNT_OF_TRANSACTIONS_IN_BOTH_DATASET";
    public static final String MIXED_DATASET = "resources/MIXED_DATASET";
    public static final ConstructCPTreeAndMineThird INSTANCE = new ConstructCPTreeAndMineThird();
    public static final IStringSplitStrategy STRATEGY = StrategyFactory.stringSplitStrategy();
    private static final String ITEMSCOUNT_FILE = "resources/ITEMSCOUNT_FILE";
    public static final double MINIMAL_THRESHOLD = 0.7d;                                                                //0.6,   0.8,   1.0,   0.8,   0.8
    public static final double MAXIMUM_THRESHOLD = 0.30d;                                                              //0.050, 0.050, 0.050, 0.025, 0.075
    public static final Map<Object, Integer> NODEANDCOUNT = new HashMap<>();
    private CPTreeNode<Object> root;
    private int _1total = 0;
    private int _2total = 0;
    private Integer index = 0;

    /*—————————-
    |初始化根节点|
     ——————————*/
    public void initialize() throws IOException {
        //LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the beginning of initializing the cp tree with a root node");
        root = new CPTreeNode<>();
        root.setIndex(index++);
        root.setValue("ROOT");
        root.set_1c(0);
        root.set_2c(0);
        root.setParent(null);
        root.setSibling(null);
        getNodeCount();
        //LOGGER.info("the root node is {}", this.root);
        //LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the end of initializing the cp tree with a root node");
    }

    /*——————————————————————————————
    |根据已有的transactions生成初始CP树|
     ———————————————————————————————*/
    public void createInitialCPTree() throws IOException {
        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the beginning of constructing a cp tree with existing transactions");
        File file = new File(MIXED_DATASET);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

        String line;
        while ((line = br.readLine()) != null && !line.trim().equals("")) {
            String[] nodesAndTag = line.split(",");
            String nodesString = nodesAndTag[0];
            String classTag = nodesAndTag[1];
            LOGGER.info("{},{} —————————————— start", nodesString, classTag);
            nodeStringToItemSetAndAppendToRoot(nodesString, classTag);                                                  //根据(FDGB,1)类型的数据生成小树并添加到cptree上
            LOGGER.info("{},{} —————————————— end", nodesString, classTag);
            if (classTag.equals("1")) {                                                                                 //纪录每个数据集的transaction的条数
                this._1total++;
            }
            if (classTag.equals("2")) {
                this._2total++;
            }
        }
        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the end of constructing a cp tree with existing transactions");
    }

    private void nodeStringToItemSetAndAppendToRoot(String nodesString, String classTag) {
        List<CPTreeNode<Object>> nodes = STRATEGY.splitSequence(nodesString, classTag);                                 //采用采用StringSplitToCharactersStrategy策略进行字符串的切分处理
        setupRelationship(nodes);                                                                                       //给待添加到CPTree的ItemSet添加父子关系
        CPTreeNode<Object> head = nodes.get(0);                                                                         //node.size() > 0一定成立
        addSubTreeToParent(head, this.root);
    }

    /*———————————————————————
    |获取两个数据集的BOTH COUNT|
     ————————————————————————*/
    public void flushTotalOfBothDataSetToFile() throws IOException {
        //LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the beginning of counting numbers of both datasets");
        File file = new File(COUNT_OF_TRANSACTIONS_IN_BOTH_DATASET);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        bw.write(String.valueOf(this._1total));
        bw.newLine();
        bw.write(String.valueOf(this._2total));
        bw.newLine();
        bw.flush();
        //LOGGER.info("flush total of records of both datasets into file : {} and {}", this._1total, this._2total);
        //LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the end of counting numbers of both datasets");
    }

    /*————————————————————————
    |将所有的后缀融合到CPTree上去|
     —————————————————————————*/
    public void merge() {
        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the beginning of merging the subtrees to the cp tree");
        for (int i = 0; i < this.root.children().size(); i++) {
            CPTreeNode<Object> node = this.root.children().get(i);                                                      //root的直接子节点
            //————————————————————————————————————————————————————————————————
            List<CPTreeNode<Object>> nodeChildren = node.children();
            for (int j = 0; j < nodeChildren.size(); j++) {
                CPTreeNode<Object> curr = nodeChildren.get(j);
                CPTreeNode<Object> currNew = new CPTreeNode<>();
                currNew.setValue(curr.value());
                currNew.set_1c(curr._1c());
                currNew.set_2c(curr._2c());
                List<CPTreeNode<Object>> nodeList = new ArrayList<>();
                nodeList.add(currNew);
                copySubTree(nodeList, curr);
                CPTreeNode<Object> toAdd = currNew;
                LOGGER.info("—————————————————————————————————————————————————————————————————————————————————");
                LOGGER.info("SUBHEAD {} APPENDED TO {}", toAdd, this.root);
                addSubTreeToParent(toAdd, this.root);
                LOGGER.info("—————————————————————————————————————————————————————————————————————————————————");
                Collections.sort(root.children(), (o1, o2) -> {
                    if (!NODEANDCOUNT.get(o1.value()).equals(NODEANDCOUNT.get(o2.value()))) {
                        return NODEANDCOUNT.get(o2.value()).compareTo(NODEANDCOUNT.get(o1.value()));
                    } else {
                        return ((Comparable) o1.value()).compareTo(o2.value());
                    }
                });
                for (int k = 1; k < root.children().size(); k++) {
                    CPTreeNode<Object> prev = root.children().get(k - 1);
                    CPTreeNode<Object> now = root.children().get(k);
                    prev.setSibling(now);
                    now.setSibling(null);
                }
            }
            //—————————————————————————————————————————————————————————————————
            //CPTreeNode<Object> newNode = new CPTreeNode<>();
            //newNode.setValue(node.value());
            //newNode.set_1c(node._1c());
            //newNode.set_2c(node._2c());
            //List<CPTreeNode<Object>> nodeList = new ArrayList<>();
            //nodeList.add(newNode);
            //copySubTree(nodeList, node);                                                                                //直接拷贝整颗子树
            //
            //
            //if (newNode.children().size() > 0) {
            //    CPTreeNode<Object> toAdd = newNode.children().get(0);
            //    LOGGER.info("—————————————————————————————————————————————————————————————————————————————————");
            //    LOGGER.info("SUBHEAD {} APPENDED TO {}", toAdd, this.root);
            //    addSubTreeToParent(toAdd, this.root);
            //    LOGGER.info("—————————————————————————————————————————————————————————————————————————————————");
            //}
            //
            //Collections.sort(root.children(), (o1, o2) -> {
            //    if (!NODEANDCOUNT.get(o1.value()).equals(NODEANDCOUNT.get(o2.value()))) {
            //        return NODEANDCOUNT.get(o2.value()).compareTo(NODEANDCOUNT.get(o1.value()));
            //    } else {
            //        return ((Comparable) o1.value()).compareTo(o2.value());
            //    }
            //});
            //
            //for (int k = 1; k < root.children().size(); k++) {
            //    CPTreeNode<Object> prev = root.children().get(k - 1);
            //    CPTreeNode<Object> curr = root.children().get(k);
            //    prev.setSibling(curr);
            //    curr.setSibling(null);
            //}
            //—————————————————————————————————————————————————————————————————
        }
        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the end of merging the subtrees to the cp tree");
    }

    /*—————————————————————————————————————————————
    | 从指的节点开始挖掘对比模式，todo 还需要进一步测试 |
     —————————————————————————————————————————————*/
    public int mineCpFromNode(CPTreeNode<Object> node) {                                                                //返回-1说明删掉了一个节点
        return mineTraverse(new ArrayList<>(), node);
    }

    private int mineTraverse(List<CPTreeNode<Object>> prefix, CPTreeNode<Object> top) {
        if (top == null) {
            return 0;
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("#0.0000");
            if (isContrastPattern(top)) {                                                                               //如果当前是对比模式
                prefix.add(top);                                                                                        //添加到前缀
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < prefix.size(); i++) {
                    sb.append(prefix.get(i).value());
                }
                LOGGER.info("1 - {},[{} {}],[{} {}],[{} {}]", sb.toString(), top._1c(), top._2c(), decimalFormat.format(top.supportOfD1(_1total)), decimalFormat.format(top.supportOfD2(_2total)), MINIMAL_THRESHOLD, MAXIMUM_THRESHOLD);
            } else if (!canPrune(top)) {
                prefix.add(top);                                                                                        //添加到前缀
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < prefix.size(); i++) {
                    sb.append(prefix.get(i).value());
                }
                LOGGER.info("2 - {},[{} {}],[{} {}],[{} {}]", sb.toString(), top._1c(), top._2c(), decimalFormat.format(top.supportOfD1(_1total)), decimalFormat.format(top.supportOfD2(_2total)), MINIMAL_THRESHOLD, MAXIMUM_THRESHOLD);
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < prefix.size(); i++) {
                    sb.append(prefix.get(i).value());
                }
                sb.append(top.value());
                LOGGER.info("3 - {},[{} {}],[{} {}],[{} {}]", sb.toString(), top._1c(), top._2c(), decimalFormat.format(top.supportOfD1(_1total)), decimalFormat.format(top.supportOfD2(_2total)), MINIMAL_THRESHOLD, MAXIMUM_THRESHOLD);

                List<CPTreeNode<Object>> topChildren = top.parent().children();                                         //top.parent不可能为null
                for (int i = 0; i < topChildren.size(); i++) {
                    CPTreeNode<Object> node = topChildren.get(i);
                    if (node.sibling() == top) {
                        node.setSibling(top.sibling());
                        break;
                    }
                    if (node == top) {
                        break;
                    }
                }
                top.parent().children().remove(top);
                return -1;
            }
        }
        List<CPTreeNode<Object>> topChildren = top.children();
        if (topChildren.size() == 0) {
            prefix.remove(prefix.size() - 1);
            return 0;
        }
        for (int i = 0; i < topChildren.size(); i++) {
            CPTreeNode<Object> currChild = topChildren.get(i);
            int response = mineTraverse(prefix, currChild);
            if (response == -1) {
                i--;
            }
        }
        prefix.remove(prefix.size() - 1);
        return 0;
    }

    /*——————————————-
    |判断是不是对比模式|
     ——————————————-*/
    private boolean isContrastPattern(CPTreeNode<Object> node) {
        double supportOfD1 = node.supportOfD1(_1total);
        double supportOfD2 = node.supportOfD2(_2total);
        boolean result = (supportOfD1 > MINIMAL_THRESHOLD && supportOfD2 <= MAXIMUM_THRESHOLD) || (supportOfD2 > MINIMAL_THRESHOLD && supportOfD1 <= MAXIMUM_THRESHOLD);
        return result;
    }

    /*——————————————-
    | 判断是不是能剪枝 |
     ——————————————-*/
    private boolean canPrune(CPTreeNode<Object> node) {
        double supportOfD1 = node.supportOfD1(_1total);
        double supportOfD2 = node.supportOfD2(_2total);
        boolean result = supportOfD1 > MINIMAL_THRESHOLD || supportOfD2 > MINIMAL_THRESHOLD;
        return !result;
    }

    /*—————————————————
    | 拷贝子树，深度拷贝 |
     —————————————————*/
    private void copySubTree(List<CPTreeNode<Object>> nodeList, CPTreeNode<Object> top) {
        int nodeChildrenSize = top.children().size();                                                                   //获取top的子节点的个数
        if (nodeChildrenSize == 0) {
            return;
        } else {
            List<CPTreeNode<Object>> topChildren = top.children();

            CPTreeNode<Object> parent = null;
            for (int i = 0; i < nodeList.size(); i++) {                                                                 //确保和nodeList里面的newChildK的值相等, 获取parent用于下面的设置子节点
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

    /*————————————————————————————
    |遍历根节点，打印所有transaction|
     ————————————————————————————*/
    public void traverseRoot() {
        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the beginning of traversing the cp tree");
        for (int i = 0; i < this.root.children().size(); i++) {
            traverse(new ArrayList<>(0), this.root.children().get(i));
        }
        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the end of traversing the cp tree");
    }

    private void traverse(List<CPTreeNode<Object>> nodeList, CPTreeNode<Object> top) {
        if (top == null) {
            return;
        } else {
            nodeList.add(top);
        }
        List<CPTreeNode<Object>> topChildren = top.children();
        if (topChildren.size() == 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < nodeList.size(); i++) {
                sb.append(nodeList.get(i)).append(" ");
            }
            sb.deleteCharAt(sb.length() - 1);
            LOGGER.info("TRANSACTION — {}", sb.toString());
            //initialTreeTransactionList.add(sb.toString());
            nodeList.remove(nodeList.size() - 1);
            return;
        }
        for (int i = 0; i < topChildren.size(); i++) {
            CPTreeNode<Object> currChild = topChildren.get(i);
            traverse(nodeList, currChild);
        }
        nodeList.remove(nodeList.size() - 1);
    }

    /*—————————————————
    |将子树添加到父亲节点|
     —————————————————*/
    private void addSubTreeToParent(CPTreeNode<Object> subTreeHead, CPTreeNode<Object> parent) {                        //在这里父子关系已经从做好了，开始做兄弟关系
        if (subTreeHead == null || parent == null) {
            return;
        }
        if (parent.children().size() == 0) {                                                                            //如果root没有孩子节点
            CPTreeNode<Object> toAdd = subTreeHead;
            while (toAdd != null) {
                LOGGER.info("ADD {} TO {}", toAdd, parent);
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
                        CPTreeNode<Object> prev = new CPTreeNode<>(currChild.value(), currChild._1c(), currChild._2c());
                        currChild.set_1c(toAdd._1c() + currChild._1c());
                        currChild.set_2c(toAdd._2c() + currChild._2c());
                        LOGGER.info("MERGE {} TO {}, now the node is {}", toAdd, prev, currChild);
                        addSubTreeToParent(toAdd.children().size() == 0 ? null : toAdd.children().get(0), currChild);
                        break;                                                                                          //找到了就跳出循环，开始将兄弟节点添加到root
                    }
                }
                CPTreeNode<Object> next = toAdd.sibling();                                                              //此处也就是root没有child和toAdd的value等价，那么直接将toAdd加到root
                if (i == parent.children().size()) {
                    LOGGER.info("ADD {} TO {}", toAdd, parent);
                    parent.children().get(parent.children().size() - 1).setSibling(toAdd);
                    toAdd.setSibling(null);
                    parent.children().add(toAdd);
                    toAdd.setParent(parent);

                    List<CPTreeNode<Object>> parentChildren = parent.children();
                    Collections.sort(parentChildren, (o1, o2) -> {
                        if (NODEANDCOUNT.get(o1.value()) != NODEANDCOUNT.get(o2.value())) {
                            return NODEANDCOUNT.get(o2.value()).compareTo(NODEANDCOUNT.get(o1.value()));
                        } else {
                            return ((Comparable) o1.value()).compareTo(o2.value());
                        }
                    });

                    for (int k = 1; k < parentChildren.size(); k++) {
                        CPTreeNode<Object> prev = parentChildren.get(k - 1);
                        CPTreeNode<Object> curr = parentChildren.get(k);
                        prev.setSibling(curr);
                        curr.setSibling(null);
                    }
                    traverseAndAddIndexForNodes(toAdd);                                                                 //把toAdd添加index
                }
                toAdd = next;
            }
        }
    }


    /*———————————————————————————————————————————————————————————————————
    | 当添加了一个新的孩子的时候，如果是新节点，那么必须初始化其下所有子节点的计数 |
     ————————————————————————————————————————————————————————————————————*/
    private void traverseAndAddIndexForNodes(CPTreeNode<Object> top) {
        if (top == null) {
            return;
        } else {
            top.setIndex(this.index++);
            LOGGER.info("SET {} INDEX {}]", top, this.index - 1);
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


    /*—————————————————
    | 设置父子节点的关系 |
     —————————————————*/
    private void setupRelationship(List<CPTreeNode<Object>> nodeList) {
        if (nodeList.size() > 1) {                                                                                      //为一个Transaction中的Nodes设定父子关系
            for (int i = 1; i < nodeList.size(); i++) {
                CPTreeNode<Object> prev = nodeList.get(i - 1);
                CPTreeNode<Object> curr = nodeList.get(i);
                prev.children().add(curr);
                curr.setParent(prev);
            }
        } else {
            //LOGGER.info("nodeList size = 1，no need to set the parent-child relationship");
        }
    }

    /*———————————————————————————
    | 获取节点的在数据集中出现的总数 |
     ———————————————————————————*/
    private void getNodeCount() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ITEMSCOUNT_FILE)));
        String line;
        while ((line = br.readLine()) != null && !line.trim().equals("")) {
            Object[] objects = line.split(" ");
            NODEANDCOUNT.put(objects[0], Integer.valueOf((String) objects[1]));
        }
    }

    //private void printNodesAndCounts() {
    //    List<Map.Entry<Object, Integer>> entryList = new ArrayList<>(NODEANDCOUNT.entrySet());
    //    Collections.sort(entryList, (o1, o2) -> {
    //        if (!o1.getValue().equals(o2.getValue())) {
    //            return o2.getValue().compareTo(o1.getValue());
    //        } else {
    //            return ((Comparable) o1.getKey()).compareTo(o2.getKey());
    //        }
    //    });
    //    for (Map.Entry<Object, Integer> entry : entryList) {
    //        System.out.println(entry.getKey() + " " + entry.getValue());
    //    }
    //}

    public static void main(String[] args) throws IOException {
        INSTANCE.initialize();                                                                                          //初始化创建根节点
        INSTANCE.createInitialCPTree();                                                                                 //构建初始CP树
        INSTANCE.traverseRoot();
        INSTANCE.merge();                                                                                               //融合并挖掘
        INSTANCE.traverseRoot();
        LOGGER.info("INDEX {}", INSTANCE.index - 1);
        //int sum = 0;
        //for (CPTreeNode<Object> node : INSTANCE.root.children()) {
        //    sum += (node._1c() + node._2c());
        //    System.out.println(node.value() + " " + (node._1c() + node._2c()));
        //}
        //System.out.println("sum = " + sum);
        //todo 挖掘的剪枝会干掉所有的可能是模式的节点及其后缀
        INSTANCE.startMining();

    }

    private void startMining() {
        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the beginning of mining the merged cp tree");
        for (int i = 0; i < INSTANCE.root.children().size(); i++) {                                                      //开始挖掘
            CPTreeNode<Object> node = INSTANCE.root.children().get(i);
            INSTANCE.mineCpFromNode(node);
        }
        LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the end of mining the merged cp tree");
    }
}

