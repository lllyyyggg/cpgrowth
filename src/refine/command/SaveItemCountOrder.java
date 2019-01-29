package refine.command;

import com.lanyage.datamining.enums.FilePathEnum;
import refine.ItemCountFacade;

public class SaveItemCountOrder implements Order {
    private String dataSet1;
    private String dataSet2;

    public SaveItemCountOrder(String dataSet1,
                              String dataSet2) {
        this.dataSet1 = dataSet1;
        this.dataSet2 = dataSet2;
    }

    @Override
    public void execute() {
        ItemCountFacade
                .get()
                .getAndSaveItemCount(
                        dataSet1,
                        dataSet2,
                        FilePathEnum.getPath("itemcount"));
    }
}
