package com.example.orderservice.controller;


import com.example.orderservice.domain.Order;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.OrderResDto;
import com.example.orderservice.messagequeue.KafkaProducer;
import com.example.orderservice.messagequeue.OrderProducer;
import com.example.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order-service")
@Slf4j
public class OrderController {
    Environment environment;
    OrderService orderService;
    KafkaProducer kafkaProducer;
    OrderProducer orderProducer;

    @Autowired
    public OrderController(OrderProducer orderProducer,KafkaProducer kafkaProducer,Environment environment, OrderService orderService) {
        this.orderService  = orderService;
        this.environment = environment;
        this.kafkaProducer = kafkaProducer;
        this.orderProducer = orderProducer;
    }

    @GetMapping("/health-check")
    public String status(){

        return String.format(
                "good good vert good %s",environment.getProperty("local.server.port")
                        + ", port(server.port)=" + environment.getProperty("server.port")
                        + " ,token secret = " + environment.getProperty("token.secret"));
    }
    @PostMapping("/{userId}/orders")
    public ResponseEntity<OrderResDto> createOrder(@PathVariable("userId") String userId,
                                                   @RequestBody OrderResDto orderDetails) {
        log.info("Before add orders data");
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = mapper.map(orderDetails, OrderDto.class);
        orderDto.setUserId(userId);
//        /* jpa */
//        OrderDto createdOrder = orderService.createOrder(orderDto);
//        OrderResDto orderResDto = mapper.map(createdOrder, OrderResDto.class);
        /* kafka */
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDetails.getQty() * orderDto.getUnitPrice());

        log.info("After added orders data");
        // kafka
        kafkaProducer.send("example-catalog-topic", orderDto);
        orderProducer.send("orders", orderDto);
        OrderResDto orderResDto = mapper.map(orderDto, OrderResDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResDto);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderResDto>> getOrder(@PathVariable("userId") String userId) {

        Iterable<Order> orderList = orderService.getOrdersByUserId(userId);

        List<OrderResDto> result = new ArrayList<>();
        orderList.forEach(v -> {
            result.add(new ModelMapper().map(v, OrderResDto.class));
        });


        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
