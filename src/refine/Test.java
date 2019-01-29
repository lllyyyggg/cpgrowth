package refine;


import com.lanyage.datamining.enums.FilePathEnum;
import refine.command.RunCpGrowthOrder;
import refine.command.SaveItemCountOrder;
import refine.command.SaveTransactionsOrder;

public class Test {
    public static void main(String[] args) {

        //ContrastPatternTree.newTree().preTraverse();
        //ContrastPatternTree.newTree().postTraverse();
        ContrastPatternTree tree = ContrastPatternTree.newTree();
        //ContrastPatternTree.preTraverse(tree.getRoot());
        ContrastPatternTree.postTraverse(tree.getRoot());
        //String dataset1 = FilePathEnum.getPath("dataset1");
        //String dataset2 = FilePathEnum.getPath("dataset2");
        //
        //Application application = new Application();
        //
        //application.take(new SaveItemCountOrder(dataset1, dataset2));
        //application.take(new SaveTransactionsOrder(dataset1, dataset2));
        //application.run();
        //
        //application.take(new RunCpGrowthOrder(0.6d, 0.05d));
        //application.run();
    }
}
