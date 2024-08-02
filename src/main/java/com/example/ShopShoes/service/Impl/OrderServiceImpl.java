package com.example.ShopShoes.service.Impl;

import com.example.ShopShoes.dto.OrderDTO;
import com.example.ShopShoes.entity.Order;
import com.example.ShopShoes.repository.OrderRepository;
import com.example.ShopShoes.service.OrderService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    private ModelMapper modelMapper;

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        return convertToDTO(order);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = convertToEntity(orderDTO);
        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }

    @Override
    public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        order.setProductName(orderDTO.getProductName());
        order.setQuantity(orderDTO.getQuantity());
        order.setStatus(orderDTO.getStatus());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setAddress(orderDTO.getAddress());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setMessage(orderDTO.getMessage());
        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }

    @Override
    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new RuntimeException("Order not found");
        }
        orderRepository.deleteById(orderId);
    }

    @Override
    public Page<OrderDTO> getAllOrder(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(this::convertToDTO);
    }

    @Override
    public Page<OrderDTO> searchOrderByStatus(String keyword, Pageable pageable) {
        Page<Order> orders = orderRepository.findByStatusContaining(keyword, pageable);
        return orders.map(this::convertToDTO);
    }

    private OrderDTO convertToDTO(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }

    private Order convertToEntity(OrderDTO orderDTO) {
        return modelMapper.map(orderDTO, Order.class);
    }

    @Override
    public List<OrderDTO> getAllOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        order.setStatus("Hủy");
        orderRepository.save(order);
    }

    @Override
    public List<OrderDTO> getOrderRecently() {
        List<Order> orders = orderRepository.findTop7ByOrderByCreatedAtDesc();
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
