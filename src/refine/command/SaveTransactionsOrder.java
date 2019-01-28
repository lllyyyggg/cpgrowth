package refine.command;

import com.lanyage.datamining.enums.FilePathEnum;
import refine.ItemCountFacade;

public class SaveTransactionsOrder implements Order {
    private String dataSet1File;
    private String dataSet2File;

    public SaveTransactionsOrder(String dataSet1File,
                                 String dataSet2File) {
        this.dataSet1File = dataSet1File;
        this.dataSet2File = dataSet2File;
    }

    @Override
    public void execute() {
        ItemCountFacade
                .get()
                .sortAndSaveTransaction(
                        FilePathEnum.ITEM_COUNT_FILE.getSource(),
                        dataSet1File,
                        dataSet2File,
                        FilePathEnum.MIX_DATASET.getSource());
    }
}
