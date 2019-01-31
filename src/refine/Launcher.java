package refine;

import refine.command.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
public class Launcher {
    private static HashMap<String, Class<? extends Order>> algorithmMap = new HashMap<>();
    static {
        algorithmMap.put("cpgrowth", RunCpGrowthOrder.class);
        algorithmMap.put("cpnodelist", RunNodeListAlgorithmOrder.class);
    }
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Application application = new Application();
        application.take(new CreateItemCountFile(args[0], args[1]));
        application.take(new CreateMixedDatasetFile(args[0], args[1]));
        Class<? extends Order> clazz;
        if(args.length == 5) {
            clazz = algorithmMap.get(args[4]);
            if(clazz == null) throw new RuntimeException("xxx");
            application.take(clazz.getConstructor(Double.class, Double.class).newInstance(Double.parseDouble(args[2]), Double.parseDouble(args[3])));
        } else {
            application.take(new RunCpGrowthOrder(Double.parseDouble(args[2]), Double.parseDouble(args[3])));
        }
        application.run();
    }
}
