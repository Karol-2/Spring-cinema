package com.tje.cinema.services;

import com.tje.cinema.domain.Order;
import com.tje.cinema.domain.Reservation;
import com.tje.cinema.repositories.OrderRepository;
import com.tje.cinema.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, ReservationRepository reservationRepository){
        this.orderRepository = orderRepository;
        this.reservationRepository = reservationRepository;
    }


    public List<Order> getOrders(){
        return this.orderRepository.findAll();
    }

    public Order getOrder(long orderId) throws RuntimeException{
        return this.orderRepository.findById(orderId).orElse(null);
    }
    @Transactional
    public void addOrder(Order order){

       this.orderRepository.save(order);
        System.out.println("Dodano Zamówienie ");

        List<Order> orderList = this.orderRepository.findOrderByDate(order.getDate());
        if(orderList.size() >= 1){
            Order orderId = orderList.get(0);
            for (Reservation reservation:order.getReservations()) {
                reservation.setOrder(orderId);
                this.reservationRepository.save(reservation);
            }
        }


    }
    @Transactional
    public void finalizeOrder(Long orderFinalizedId) throws RuntimeException{
        this.orderRepository.finalizeOrder(orderFinalizedId);
        System.out.println("Sfinalizowano Zamówienie z id: " + orderFinalizedId);
    }

    @Transactional
    public void cancelEveryOrderOfMovie(long movieId){
      this.orderRepository.cancelEveryOrderOfMovie(movieId);
      System.out.println("Canceled every order of movie with id " + movieId);
    }
    @Transactional
    public void cancelEveryOrderOfscreening(long screeningId){
       this.orderRepository.cancelEveryOrderOfscreening(screeningId);
        System.out.println("Canceled every order of screenidng with id " + screeningId);
    }


    public List<Order> getOrdersByUserId(long userId){
        return this.orderRepository.getOrdersByUserId(userId);
    }



}
