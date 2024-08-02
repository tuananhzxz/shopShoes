package com.example.ShopShoes.service;

import com.example.ShopShoes.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderDTO getOrderById(Long orderId);
    List<OrderDTO> getAllOrders();
    OrderDTO createOrder(OrderDTO orderDTO);
    OrderDTO updateOrder(Long orderId, OrderDTO orderDTO);
    void deleteOrder(Long orderId);
    Page<OrderDTO> getAllOrder(Pageable pageable);
    List<OrderDTO> getAllOrdersByUserId(Long userId);
    Page<OrderDTO> searchOrderByStatus(String keyword, Pageable pageable);
    void cancelOrder(Long orderId);

    List<OrderDTO> getOrderRecently();
}
