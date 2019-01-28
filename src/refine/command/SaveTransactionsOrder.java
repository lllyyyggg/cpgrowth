package refine.command;

import refine.ItemCountFacade;

public class SaveTransactionsOrder implements Order {
    private String itemCountFile;
    private String dataSet1File;
    private String dataSet2File;
    private String mixedDataSetFile;

    public SaveTransactionsOrder(String itemCountFile,
                                 String dataSet1File,
                                 String dataSet2File,
                                 String mixedDataSetFile) {
        this.itemCountFile = itemCountFile;
        this.dataSet1File = dataSet1File;
        this.dataSet2File = dataSet2File;
        this.mixedDataSetFile = mixedDataSetFile;
    }

    @Override
    public void execute() {
        ItemCountFacade
                .get()
                .sortAndSaveTransaction(
                        itemCountFile,
                        dataSet1File,
                        dataSet2File,
                        mixedDataSetFile);
    }
}
