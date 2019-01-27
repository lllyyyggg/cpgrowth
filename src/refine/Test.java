package refine;


public class Test {
    public static void main(String[] args) {
        ItemSet itemSet = new ItemSet();
        itemSet.addItem("B2", 3);
        itemSet.addItem("A1", 2);
        itemSet.addItem("C3", 1);
        itemSet.sort();
        System.out.println(itemSet);
        System.out.println(itemSet.length());
    }
}
