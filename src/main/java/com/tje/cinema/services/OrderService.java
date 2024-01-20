package com.tje.cinema.services;

import com.tje.cinema.domain.Order;
import com.tje.cinema.domain.Reservation;
import com.tje.cinema.repositories.OrderRepository;
import com.tje.cinema.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final ReservationRepository reservationRepository;
    private final EntityManager entityManager;

    @Autowired
    public OrderService(EntityManager entityManager,OrderRepository orderRepository, ReservationRepository reservationRepository){
        this.orderRepository = orderRepository;
        this.reservationRepository = reservationRepository;
        this.entityManager = entityManager;
    }


    public List<Order> getOrders(){
        return this.orderRepository.findAll();
    }

    public Order getOrder(long orderId) throws RuntimeException{
        return this.orderRepository.findById(orderId).orElse(null);
    }

    public Order addOrder(Order order) {
        for (Reservation reservation : order.getReservations()) {
            entityManager.merge(reservation);
        }
        Order newOrder = this.orderRepository.save(order);
        System.out.println("Dodano Zamówienie ");



        return newOrder;
    }

    public void finalizeOrder(Long orderFinalizedId) throws RuntimeException{
        this.orderRepository.finalizeOrder(orderFinalizedId);
        System.out.println("Sfinalizowano Zamówienie z id: " + orderFinalizedId);
    }


    public void cancelEveryOrderOfMovie(long movieId){
      this.orderRepository.cancelEveryOrderOfMovie(movieId);
      System.out.println("Canceled every order of movie with id " + movieId);
    }

    public void cancelEveryOrderOfscreening(long screeningId){
       this.orderRepository.cancelEveryOrderOfscreening(screeningId);
        System.out.println("Canceled every order of screenidng with id " + screeningId);
    }

    public void removeOrderById(Long id){
        orderRepository.deleteById(id);
    }


    public List<Order> getOrdersByUserId(long userId){
        return this.orderRepository.getOrdersByUserId(userId);
    }



}
