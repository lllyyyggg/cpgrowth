package refine;

import refine.command.Order;

import java.util.ArrayList;
import java.util.List;

public class Application {
    private List<Order> orderList;
    public Application() {
        this.orderList = new ArrayList<>();
    }

    public void take(Order order) {
        orderList.add(order);
    }

    public void run() {
        for (Order order : orderList) {
            order.execute();
        }
        orderList.clear();
    }
}
