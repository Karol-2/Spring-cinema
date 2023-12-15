package com.tje.cinema.services;

import com.tje.cinema.domain.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final List<Order> orderDatabase = new ArrayList<>();
    private long orderIdCounter = 1;

    public List<Order> getOrders(){
        return this.orderDatabase;
    }

    public void addOrder(Order order){
        order.setOrderId(orderIdCounter++);
        orderDatabase.add(order);
        System.out.println("Dodano Zamówienie z id: "+order.getOrderId());
        System.out.println("baza danych:[ ");
        orderDatabase.forEach(o -> System.out.println(o.toString()));
        System.out.println("] ");
    }
    public void removeOrder(Order order){
        orderDatabase.remove(order);
        System.out.println("Usunieto Zamówienie z id: "+order.getOrderId());
    }

    public void finalizeOrder(Order orderfinalized){
        orderDatabase.stream()
                .filter(order -> order.getOrderId() == orderfinalized.getOrderId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order not found"))
                .setStatus(Order.OrderStatus.COMPLETED);
        System.out.println("Sfinalizowano Zamówienie z id: " + orderfinalized.getOrderId());
    }


    public List<Order> getOrdersByUserId(long userId){
        return orderDatabase.stream()
                .filter(order -> order.getUser().getId() == userId)
                .collect(Collectors.toList());
    }

}
