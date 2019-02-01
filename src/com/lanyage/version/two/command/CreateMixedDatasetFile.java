package com.lanyage.version.two.command;

import com.lanyage.version.two.datastructure.ContrastPatternTree;
import com.lanyage.version.two.context.Context;
public class CreateMixedDatasetFile implements Order {
    private String dataSet1File;
    private String dataSet2File;
    public CreateMixedDatasetFile(String dataSet1File, String dataSet2File) {
        this.dataSet1File = dataSet1File;
        this.dataSet2File = dataSet2File;
    }
    @Override
    public void execute() {
        Context.getInstance().getItemFacade().createMixedDatasetFile(dataSet1File, dataSet2File);
        ContrastPatternTree tree = Context.getInstance().getTree();
        tree.preTraverse();
        tree.postTraverse();
    }
}
