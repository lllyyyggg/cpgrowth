package com.lanyage.version.two.command;


import com.lanyage.version.two.context.Context;
public class CreateItemCountFile implements Order {
    private String dataSet1;
    private String dataSet2;
    public CreateItemCountFile(String dataSet1, String dataSet2) {
        this.dataSet1 = dataSet1;
        this.dataSet2 = dataSet2;
    }
    @Override
    public void execute() {
        Context.getInstance().getItemFacade().createItemCountFile(dataSet1, dataSet2);
    }
}
