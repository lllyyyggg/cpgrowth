package com.lanyage.version.two.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lanyage.version.two.datastructure.ContrastPatternTree;
import com.lanyage.version.two.utils.FunctorStaticFactory;
import com.lanyage.version.two.context.Context;
import com.lanyage.version.two.utils.ContrastPatternUtil;
import java.util.*;
import java.util.function.Function;
public class MineFromNodeList implements MiningAlgorithm{
    private Double alpha;
    private Double beta;
    private static final ThreadLocal<Integer> total = ThreadLocal.withInitial(() -> 0);
    private static final ThreadLocal<Integer> calculated = ThreadLocal.withInitial(() -> 0);
    private static final Logger LOGGER = LoggerFactory.getLogger(MineFromNodeList.class);
    private final Set<String> prune_set = new HashSet<>();
    private ContrastPatternTree tree;
    public MineFromNodeList(Double alpha, Double beta) {
        this.alpha = alpha;
        this.beta = beta;
    }
    @Override
    public void mine() {
        LOGGER.info("————————————STARTING MINECPNODELIST ALGORITHM————————————");
        Context context = Context.getInstance();
        int n1 = context.getN1();
        int n2 = context.getN2();
        this.tree = context.getTree();          //构建初始CP树

        Function<Map<String, Integer>, List<String>> getSortedItems = FunctorStaticFactory.getSortedItems();
        Function<List<String>, Map<String, Integer>> getItemIndexMap = FunctorStaticFactory.getItemIndexMap();

        Map<String, Integer> itemCount = context.getItemcountMap();
        List<String> sortedItems = getSortedItems.apply(itemCount);
        Map<String, Integer> itemIndexMap = getItemIndexMap.apply(sortedItems);


        Map<String, List<SequenceSuffix>> initialSuffixMap = getInitialSuffix(this.tree.getRoot());
        Map<String, List<SequenceSuffix>> tempMap = initialSuffixMap;


        initialSuffixMap.remove("$");
        while (!tempMap.isEmpty()) {

            mine(tempMap, n1, n2);
            tempMap = tryCombine(tempMap, initialSuffixMap, sortedItems, itemIndexMap);

        }
        LOGGER.info("TOTAL : {}, N1: {}, N2: {}, ALPHA: {}, BETA: {}", total.get(), n1, n2, alpha, beta);
        LOGGER.info("CALCULATED TIME : {} TIMES AND PRUNE SIZE : {}", calculated.get(), prune_set.size());
    }
    private void mine(Map<String, List<SequenceSuffix>> suffixMap, int n1, int n2) {
        for (Map.Entry<String, List<SequenceSuffix>> entry : suffixMap.entrySet()) {
            String sequence = entry.getKey();
            List<SequenceSuffix> nodeList = entry.getValue();
            int c1 = 0, c2 = 0;
            for (int j = 0; j < nodeList.size(); j++) {
                SequenceSuffix suffix = nodeList.get(j);
                c1 += suffix.c1;
                c2 += suffix.c2 ;
            }
            if (existsInPruneset(sequence)) continue;
            calculated.set(calculated.get() + 1);
            if (ContrastPatternUtil.isContrastPattern(c1, c2, n1, n2, alpha, beta)) {
                total.set(total.get() + 1);
                LOGGER.info("1 - {}, [{} {}], [{} {}]", sequence, c1, c2, alpha * n1, beta * n2);
            } else if (!ContrastPatternUtil.canPrune(c1, c2, n1, n2, alpha)) {
                LOGGER.info("2 - {}, [{} {}], [{} {}]", sequence, c1, c2, alpha * n1, beta * n2);
            } else {
                prune_set.add(sequence);
                LOGGER.info("3 - {}, [{} {}], [{} {}]", sequence, c1, c2, alpha * n1, beta * n2);
            }
        }
    }
    private Map<String, List<SequenceSuffix>> tryCombine(Map<String, List<SequenceSuffix>> tempMap,
                                                         Map<String, List<SequenceSuffix>> initialSuffixMap,
                                                         List<String> sortedItems,
                                                         Map<String, Integer> itemIndexMap) {
        Map<String, List<SequenceSuffix>> m = new HashMap<>();
        for (String childKey : tempMap.keySet()) {
            int parentIndex = itemIndexMap.get(childKey.substring(0, 2)) - 1;   //index out of bound
            for(int j = 0; j <= parentIndex; j++) {
                String parentKey = sortedItems.get(j);
                List<SequenceSuffix> parentNodeList = initialSuffixMap.get(parentKey);
                List<SequenceSuffix> childrenNodeList = tempMap.get(childKey);
                List<SequenceSuffix> combinedNodeList = combine(parentNodeList, childrenNodeList);
                if (!combinedNodeList.isEmpty()) m.put(parentKey + " " + childKey, combinedNodeList);
            }
        }
        return m;
    }

    private List<SequenceSuffix> combine(List<SequenceSuffix> parent, List<SequenceSuffix> child) {
        int i = 0, j = 0;
        List<SequenceSuffix> combinedNodeList = new ArrayList<>();
        while (i < parent.size() && j < child.size()) {
            SequenceSuffix poc = parent.get(i);
            SequenceSuffix coc = child.get(j);
            if (poc.preIndex() < coc.preIndex()) {
                if (poc.postIndex() > coc.postIndex()) {
                    //boolean canCombine = coc.node().getParent() == poc.node();
                    boolean canCombine = (coc.depth - poc.depth == 1);
                    if (canCombine) {
                        SequenceSuffix combinedOne = new SequenceSuffix();
                        combinedOne.preIndex = poc.preIndex();
                        combinedOne.postIndex = poc.postIndex();
                        combinedOne.c1 = coc.c1();
                        combinedOne.c2 = coc.c2();
                        //combinedOne.node = poc.node();
                        combinedOne.depth = poc.depth;
                        combinedOne.sequence = new StringBuilder(poc.sequence).append(" ").append(coc.sequence).toString();
                        combinedNodeList.add(combinedOne);
                    }
                    j++;
                } else {
                    i++;
                }
            } else {
                j++;
            }
        }
        return combinedNodeList;
    }
    private boolean existsInPruneset(String prefix) {
        int indexofspace = prefix.indexOf(" ", 0);
        int lastindexofspace = prefix.lastIndexOf(" ");
        if (indexofspace < 0) return prune_set.contains(prefix);
        while (indexofspace > 0 && indexofspace <= lastindexofspace){
            String s = prefix.substring(0, indexofspace);
            if (prune_set.contains(s)) return true;
            indexofspace = prefix.indexOf(" ", indexofspace + 1);
        }
        return false;
    }
    private Map<String, List<SequenceSuffix>> getInitialSuffix(ContrastPatternTree.ContrastPatterTreeNode root) {
        Map<String, List<SequenceSuffix>> initialSuffixMap = new HashMap<>();
        LinkedList<ContrastPatternTree.ContrastPatterTreeNode> stack = new LinkedList<>();
        stack.addLast(root);
        while (!stack.isEmpty()) {
            ContrastPatternTree.ContrastPatterTreeNode node = stack.pollLast();
            List<SequenceSuffix> suffixList = initialSuffixMap.getOrDefault(node.getValue(), null);
            if (null == suffixList) {
                suffixList = new ArrayList<>();
                initialSuffixMap.put(node.getValue(), suffixList);
            }
            SequenceSuffix suffix = new SequenceSuffix();
            suffix.preIndex = node.getPreIndex();
            suffix.postIndex = node.getPostIndex();
            suffix.c1 = node.getC1();
            suffix.c2 = node.getC2();
            suffix.sequence = node.getValue();
            //suffix.node = node;
            suffix.depth = node.getDepth();
            suffixList.add(suffix);
            if (node.childrenSize() > 0) {
                for (int j = node.childrenSize() - 1; j >= 0; j--) {
                    stack.addLast(node.getChild(j));
                }
            }
        }
        return initialSuffixMap;
    }
    public static class SequenceSuffix {
        private Integer preIndex;
        private Integer postIndex;
        private Integer c1;
        private Integer c2;
        private Integer depth;
        private String sequence;
        //private ContrastPatternTree.ContrastPatterTreeNode node;
        public SequenceSuffix() {
            this.c1 = 0;
            this.c2 = 0;
        }
        //public ContrastPatternTree.ContrastPatterTreeNode node() {
        //    return node;
        //}
        //public void setNode(ContrastPatternTree.ContrastPatterTreeNode node) {
        //    this.node = node;
        //}
        public Object sequence() {
            return sequence;
        }
        public void setSequence(String sequence) {
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
        public Integer depth() {
            return depth;
        }
        public void setDepth(Integer depth) {
            this.depth = depth;
        }

        @Override
        public String toString() {
            return "(" + sequence + ", " + preIndex + ", " + postIndex + ", " + c1 + ", " + c2 + ")";
        }
    }
}
