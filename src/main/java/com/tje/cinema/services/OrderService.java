package com.tje.cinema.services;

import com.tje.cinema.domain.Order;
import com.tje.cinema.domain.Reservation;
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

    public Order getOrder(long orderId){
        return this.orderDatabase.stream()
                .filter(order -> order.getOrderId() == orderId)
                .findFirst()
                .orElseThrow(()->new RuntimeException("Order not found"));
    }
    public void addOrder(Order order){
        order.setOrderId(orderIdCounter++);
        orderDatabase.add(order);
        System.out.println("Dodano Zamówienie z id: "+order.getOrderId());
    }
    public void removeOrder(Order order){
        orderDatabase.remove(order);
        System.out.println("Usunieto Zamówienie z id: "+order.getOrderId());
    }

    public void finalizeOrder(Long orderFinalizedId){
        orderDatabase.stream()
                .filter(order -> order.getOrderId() == orderFinalizedId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order not found"))
                .setStatus(Order.OrderStatus.COMPLETED);
        System.out.println("Sfinalizowano Zamówienie z id: " + orderFinalizedId);
    }

    public void editReservations(Long orderId, List<Reservation> newReservations){
       Order foundOrder =  orderDatabase.stream()
                .filter(order -> order.getOrderId() == orderId)
                .findFirst()
                .orElseThrow(()-> new RuntimeException("Order doesn't exists"));
       if(foundOrder != null){
           foundOrder.setReservations(newReservations);
       }
    }
    public void cancelEveryOrderOfMovie(long movieId){
        List<Order> ordersToCancel = orderDatabase.stream()
                .filter(order -> order.getReservations().stream()
                        .anyMatch(reservation -> reservation.getScreening().getMovieId() == movieId))
                .collect(Collectors.toList());

        ordersToCancel.forEach(order -> {
            order.setStatus(Order.OrderStatus.CANCELLED);
        });
    }

    public void cancelEveryOrderOfscreening(long screeningId){
        List<Order> ordersToCancel = orderDatabase.stream()
                .filter(order -> order.getReservations().stream()
                        .anyMatch(reservation -> reservation.getScreening().getScreeningId() == screeningId))
                .collect(Collectors.toList());

        ordersToCancel.forEach(order -> {
            order.setStatus(Order.OrderStatus.CANCELLED);
        });
    }


    public List<Order> getOrdersByUserId(long userId){
        return orderDatabase.stream()
                .filter(order -> order.getUser().getId() == userId)
                .collect(Collectors.toList());
    }



}
