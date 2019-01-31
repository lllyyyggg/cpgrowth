package refine.context;
import refine.ContrastPatternTree;
import refine.ItemFacade;
import java.util.HashMap;
import java.util.Map;
public class Context {
    private Map<String, String> locationsMap = new HashMap<>();
    private Map<String, Integer> itemcountMap = new HashMap<>();
    private Integer n1;
    private Integer n2;
    private ContrastPatternTree tree;
    public final ItemFacade itemFacade = ItemFacade.INSTANCE;
    private static final Context INSTANCE = new Context();
    private Context() {
        locationsMap.put("mixeddataset", "resources/MIXEDDATASET");
        locationsMap.put("itemcount", "resources/ITEMCOUNT");
    }
    public String getItemCountFile() {
        return locationsMap.get("itemcount");
    }
    public String getMixedDatasetFile() {
        return locationsMap.get("mixeddataset");
    }
    public Map<String, Integer> getItemcountMap() {
        return itemcountMap;
    }
    public Integer getN1() {
        return n1;
    }
    public void setN1(Integer n1) {
        this.n1 = n1;
    }
    public Integer getN2() {
        return n2;
    }
    public void setN2(Integer n2) {
        this.n2 = n2;
    }
    public static Context getInstance() {
        return INSTANCE;
    }
    public ContrastPatternTree getTree() {
        if(tree == null)
            tree = ContrastPatternTree.newTree();
        return tree;
    }
    public ItemFacade getItemFacade() {
        return itemFacade;
    }
    public void addItemCount(String key, Integer value) {
        itemcountMap.put(key, value);
    }
}
