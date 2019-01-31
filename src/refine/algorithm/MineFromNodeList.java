package refine.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import refine.datastructure.ContrastPatternTree;
import refine.utils.FunctorStaticFactory;
import refine.context.Context;
import refine.utils.ContrastPatternUtil;
import java.util.*;
import java.util.function.Function;
public class MineFromNodeList implements MiningAlgorithm{
    private Double alpha;
    private Double beta;
    private static final ThreadLocal<Integer> total = ThreadLocal.withInitial(() -> 0);
    private static final ThreadLocal<Integer> calculated = ThreadLocal.withInitial(() -> 0);
    private static final Logger LOGGER = LoggerFactory.getLogger(CPGrowth.class);
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
        this.tree = context.getTree();
        Map<String, Integer> itemCount = context.getItemcountMap();
        Function<Map<String, Integer>, List<String>> getSortedItems = FunctorStaticFactory.getSortedItems();
        Function<List<String>, Map<String, Integer>> getItemIndexMap = FunctorStaticFactory.getItemIndexMap();
        List<String> sortedItems = getSortedItems.apply(itemCount);
        Map<String, Integer> itemIndexMap = getItemIndexMap.apply(sortedItems);
        Map<String, List<SequenceSuffix>> suffixMap = getInitialSuffix(this.tree.getRoot());
        Map<String, List<SequenceSuffix>> tempMap = suffixMap;
        suffixMap.remove("$");
        while (!tempMap.isEmpty()) {
            mine(tempMap, n1, n2);
            Map<String, List<SequenceSuffix>> m = new HashMap<>();
            for (String childKey : tempMap.keySet()) {//children
                int parentIndex = itemIndexMap.get(childKey.substring(0, 2)) - 1;
                for(int j = 0; j <= parentIndex; j++) {
                    String parentKey = sortedItems.get(j);
                    List<SequenceSuffix> parentNodeList = suffixMap.get(parentKey);
                    List<SequenceSuffix> childrenNodeList = tempMap.get(childKey);
                    List<SequenceSuffix> combinedNodeList = combine(parentNodeList, childrenNodeList);
                    if (!combinedNodeList.isEmpty()) {
                        m.put(parentKey + " " + childKey, combinedNodeList);
                    }
                }
            }
            tempMap = m;
        }
        LOGGER.info("TOTAL : {}, N1: {}, N2: {}, ALPHA: {}, BETA: {}", total.get(), n1, n2, alpha, beta);
        LOGGER.info("CALCULATED TIME : {} TIMES AND PRUNE SIZE : {}", calculated.get(), prune_set.size());
    }
    public void mine(Map<String, List<SequenceSuffix>> suffixMap, int n1, int n2) {
        for (Map.Entry<String, List<SequenceSuffix>> entry : suffixMap.entrySet()) {
            String key = entry.getKey();
            List<SequenceSuffix> nodeList = entry.getValue();
            SequenceSuffix tobemined = new SequenceSuffix();
            tobemined.sequence = key;
            tobemined.preIndex = -1;
            tobemined.postIndex = -1;
            for (int j = 0; j < nodeList.size(); j++) {
                SequenceSuffix suffix = nodeList.get(j);
                tobemined.c1 = suffix.c1 + tobemined.c1;
                tobemined.c2 = suffix.c2 + tobemined.c2;
            }
            if (existsInPruneset(tobemined.sequence)) continue;
            calculated.set(calculated.get() + 1);
            if (tobemined.isContrastPattern(alpha, beta)) {
                total.set(total.get() + 1);
                LOGGER.info("1 - {}, [{} {}], [{} {}]", tobemined.sequence, tobemined.c1, tobemined.c2, alpha * n1, beta * n2);
            } else if (!tobemined.canPrune(alpha)) {
                LOGGER.info("2 - {}, [{} {}], [{} {}]", tobemined.sequence, tobemined.c1, tobemined.c2, alpha * n1, beta * n2);
            } else {
                prune_set.add(tobemined.sequence);
                LOGGER.info("3 - {}, [{} {}], [{} {}]", tobemined.sequence, tobemined.c1, tobemined.c2, alpha * n1, beta * n2);
            }
        }
    }
    public List<SequenceSuffix> combine(List<SequenceSuffix> parent, List<SequenceSuffix> child) {
        int i = 0, j = 0;
        List<SequenceSuffix> combinedNodeList = new ArrayList<>();
        while (i < parent.size() && j < child.size()) {
            SequenceSuffix poc = parent.get(i);
            SequenceSuffix coc = child.get(j);
            if (poc.preIndex() < coc.preIndex()) {
                if (poc.postIndex() > coc.postIndex()) {
                    boolean canCombine = coc.node().getParent() == poc.node();
                    if (canCombine) {
                        SequenceSuffix combinedOne = new SequenceSuffix();
                        combinedOne.preIndex = poc.preIndex();
                        combinedOne.postIndex = poc.postIndex();
                        combinedOne.c1 = coc.c1();
                        combinedOne.c2 = coc.c2();
                        combinedOne.node = poc.node();
                        combinedOne.sequence = poc.sequence() + " " + coc.sequence();
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
        String temp = prefix;
        while (!"".equals(temp)) {
            if (prune_set.contains(temp)) return true;
            int lastindex = temp.lastIndexOf(" ");
            if (lastindex < 0) return false;
            temp = temp.substring(0, lastindex);
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
            suffix.node = node;
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
        private String sequence;
        private ContrastPatternTree.ContrastPatterTreeNode node;
        public SequenceSuffix() {
            this.c1 = 0;
            this.c2 = 0;
        }
        public ContrastPatternTree.ContrastPatterTreeNode node() {
            return node;
        }
        public void setNode(ContrastPatternTree.ContrastPatterTreeNode node) {
            this.node = node;
        }
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
        @Override
        public String toString() {
            return "(" + sequence + ", " + preIndex + ", " + postIndex + ", " + c1 + ", " + c2 + ")";
        }
        public boolean isContrastPattern(Double alpha, Double beta) {
            Context context = Context.getInstance();
            int n1 = context.getN1();
            int n2 = context.getN2();
            return ContrastPatternUtil.isContrastPatter(c1, c2, n1, n2, alpha, beta);
        }
        public boolean canPrune(Double alpha) {
            Context context = Context.getInstance();
            int n1 = context.getN1();
            int n2 = context.getN2();
            return ContrastPatternUtil.canPrune(c1, c2, n1, n2, alpha);
        }
    }
}
