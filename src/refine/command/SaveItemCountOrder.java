package refine.command;

import refine.ItemCountFacade;

public class SaveItemCountOrder implements Order {
    private String dataSet1;
    private String dataSet2;
    private String itemCountFile;

    public SaveItemCountOrder(String dataSet1,
                              String dataSet2,
                              String itemCountFile) {
        this.dataSet1 = dataSet1;
        this.dataSet2 = dataSet2;
        this.itemCountFile = itemCountFile;
    }

    @Override
    public void execute() {
        ItemCountFacade
                .get()
                .getAndSaveItemCount(
                        dataSet1,
                        dataSet2,
                        itemCountFile);
    }
}
