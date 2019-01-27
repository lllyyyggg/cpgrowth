package refine;


import com.lanyage.datamining.enums.FilePathEnum;

import java.util.Map;

public class Test {
    public static void main(String[] args) {
        //Map<String, Integer> map = ItemCountFacade.load(FilePathEnum.ITEM_COUNT_FILE.getSource());
        //for(String s : map.keySet()) {
        //    System.out.println(s + " -> " + map.get(s));
        //}

        ItemCountFacade.sortAndSaveTransaction(
                FilePathEnum.ITEM_COUNT_FILE.getSource(),
                FilePathEnum.DATA_SET_I.getSource(),
                FilePathEnum.DATA_SET_II.getSource(),
                FilePathEnum.MIX_DATASET.getSource()
        );
    }
}
