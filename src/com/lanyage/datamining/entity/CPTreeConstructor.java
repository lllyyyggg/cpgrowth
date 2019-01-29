package com.lanyage.datamining.entity;

import com.lanyage.datamining.datastructure.CPTreeNode;
import com.lanyage.datamining.enums.FilePathEnum;
import com.lanyage.datamining.factory.StrategyFactory;
import com.lanyage.datamining.strategy.IStringSplitStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * 初始CP树构建
 */
public class CPTreeConstructor {

    public static final Logger LOGGER = LoggerFactory.getLogger(CPTreeConstructor.class);
    public static final IStringSplitStrategy STRATEGY = StrategyFactory.stringSplitStrategy();
    private Map<Object, Integer> nodeAndCount;

    public CPTreeConstructor() {
        this.nodeAndCount = new NodeCounter(FilePathEnum.getPath("itemcount")).getNodeCountMap();
    }

    public Map<Object, Integer> nodeAndCount() {
        return this.nodeAndCount;
    }

    /*—————————-
    |初始化根节点|
     ——————————*/
    private void initializeRoot(CPTreeNode<Object> root) {
        root
                .value("$")
                .c1(0)
                .c2(0)
                .parent(null)
                .sibling(null);
    }

    /*——————————————————————————————
    |根据已有的transactions生成初始CP树|
     ———————————————————————————————*/
    public CPTreeNode<Object> createInitialCPTree() {
        CPTreeNode<Object> root = new CPTreeNode<>();
        initializeRoot(root);                                                                                           //初始化root节点

        //LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the beginning of constructing a cp tree with existing transactions");
        TreeAppender addTreeToTree = new TreeAppender();
        File file = new File(FilePathEnum.getPath("mixeddataset"));
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = br.readLine()) != null && !line.trim().equals("")) {
                String[] nodesAndTag = line.split(",");
                String nodesString = nodesAndTag[0];
                String classTag = nodesAndTag[1];
                //LOGGER.info("{},{} —————————————— start", nodesString, classTag);
                CPTreeNode<Object> head = nodeStringToItemSet(nodesString, classTag);                                       //根据(FDGB,1)类型的数据生成小树并添加到cptree上
                addTreeToTree.addTreeToTree(head, root);
                //LOGGER.info("{},{} —————————————— end", nodesString, classTag);
            }
        } catch (IOException e) {
            throw new RuntimeException("发生了IO异常");
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                throw new RuntimeException("流关闭错误");
            }
        }
        //LOGGER.info("———————————————————————————————————————————————————————————————————————————————————————————————————the end of constructing a cp tree with existing transactions");
        return root;
    }

    /*———————————————————————
    |将Transaction转变称为子树|
     ———————————————————————*/
    private CPTreeNode<Object> nodeStringToItemSet(String nodesString, String classTag) {
        List<CPTreeNode<Object>> nodes = STRATEGY.splitSequence(nodesString, classTag);                                 //采用采用StringSplitToCharactersStrategy策略进行字符串的切分处理
        setupRelationshipBetweenParentAndChildNode(nodes);                                                              //给待添加到CPTree的ItemSet添加父子关系
        CPTreeNode<Object> head = nodes.get(0);                                                                         //node.size() > 0一定成立
        return head;
    }


    /*—————————————————
    | 设置父子节点的关系 |
     —————————————————*/
    private void setupRelationshipBetweenParentAndChildNode(List<CPTreeNode<Object>> nodeList) {
        if (nodeList.size() > 1) {                                                                                      //为一个Transaction中的Nodes设定父子关系
            for (int i = 1; i < nodeList.size(); i++) {
                CPTreeNode<Object> prev = nodeList.get(i - 1);
                CPTreeNode<Object> curr = nodeList.get(i);
                prev.children().add(curr);
                curr.parent(prev);
            }
        } else {
            //LOGGER.info("nodeList size = 1，no need to set the parent-child relationship");
        }
    }
}

