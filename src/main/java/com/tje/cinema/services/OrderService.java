package com.tje.cinema.services;

import com.tje.cinema.domain.Order;
import com.tje.cinema.domain.Reservation;
import com.tje.cinema.repositories.OrderRepository;
import com.tje.cinema.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
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
    public List<Order> getOrdersByDate(LocalDate date){
        return this.orderRepository.findOrderByDate(date);
    }


    public Order addOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        List<Reservation> resWithId  = new ArrayList<>();
        for (Reservation reservation : order.getReservations()) {
            reservation.setOrder(savedOrder);
            Reservation savedRes =  reservationRepository.save(reservation);
            resWithId.add(savedRes);

        }
        savedOrder.setReservations(resWithId);

        return savedOrder;
    }

    public String finalizeOrder(Long orderFinalizedId) throws RuntimeException{
        this.orderRepository.finalizeOrder(orderFinalizedId);
        System.out.println("Sfinalizowano Zamówienie z id: " + orderFinalizedId);
        return "Finalised order with id: " + orderFinalizedId;
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
