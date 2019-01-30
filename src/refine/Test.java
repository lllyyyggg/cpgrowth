package refine;

import com.lanyage.datamining.enums.FilePathEnum;
import refine.command.Application;
import refine.command.CreateItemCountFile;
import refine.command.CreateMixedDatasetFile;
import refine.command.RunCpGrowthOrder;



public class Test {
    public static void main(String[] args) {
        String source1 = FilePathEnum.getPath("dataset1");
        String source2 = FilePathEnum.getPath("dataset2");
        Application application = new Application();
        application.take(new CreateItemCountFile(source1, source2));
        application.take(new CreateMixedDatasetFile(source1, source2));
        application.take(new RunCpGrowthOrder(0.6d, 0.05d));
        application.run();
    }
}
