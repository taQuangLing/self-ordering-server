package hust.server.domain.orders.service;

import hust.server.app.exception.ApiException;
import hust.server.domain.orders.dto.request.CartItemCreationRequest;
import hust.server.domain.orders.dto.request.CartUpdateNoteRequest;
import hust.server.domain.orders.dto.request.CheckCartItemRequest;
import hust.server.domain.orders.dto.request.QuantityCartItemUpdateRequest;
import hust.server.domain.orders.dto.response.CashierCartResponse;
import hust.server.domain.orders.dto.response.CustomerCartResponse;
import hust.server.domain.orders.entity.Cart;
import hust.server.domain.orders.entity.CartItem;
import hust.server.domain.orders.entity.Order;
import hust.server.domain.orders.repository.CartItemRepository;
import hust.server.domain.orders.repository.CartRepository;
import hust.server.domain.orders.repository.OrderRepository;
import hust.server.domain.products.entity.Product;
import hust.server.domain.products.repository.ProductRepository;
import hust.server.infrastructure.enums.MessageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<CustomerCartResponse>   guestGetCartList(String userId) {
        Cart cart = cartRepository.getByUserId(userId).orElse(null);
        if (cart == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "userId = " + userId);

        List<CartItem> customerCarts = cart.getCartItemList();
        customerCarts.sort(new Comparator<CartItem>() {
            @Override
            public int compare(CartItem o1, CartItem o2) {
                return o2.getCreatedAt().compareTo(o1.getCreatedAt());
            }
        });
        List<CustomerCartResponse> response = new ArrayList<>();
        customerCarts.forEach(item -> response.add(item.toCustomerCartItemResponse()));
        return response;
    }

    public CashierCartResponse cashierGetCartList(String userId) {
        Cart cart = cartRepository.getByUserId(userId).orElse(null);
        if (cart == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "userId = " + userId);

        return cart.toCashierCartResponse();
    }

    public MessageCode checkCart(CheckCartItemRequest request) {
        CartItem cartItem = cartItemRepository.getById(request.getCartItemId()).orElse(null);

        if (cartItem == null)throw new ApiException(MessageCode.ID_NOT_FOUND);
        cartItem.setIsCheck(request.getIsCheck());
        try{
            cartItemRepository.save(cartItem);
            return MessageCode.SUCCESS;
        }catch (Exception e){
            throw new ApiException(MessageCode.ERROR);
        }
    }

    public MessageCode updateQuantity(QuantityCartItemUpdateRequest request) {
        CartItem cartItem = cartItemRepository.getById(request.getCartItemId()).orElse(null);

        if (cartItem == null)throw new ApiException(MessageCode.ID_NOT_FOUND);
        cartItem.setQuantity(request.getQuantity());
        try{
            cartItemRepository.save(cartItem);
            return MessageCode.SUCCESS;
        }catch (Exception e){
            throw new ApiException(MessageCode.ERROR);
        }
    }

    public MessageCode addCartItem(CartItemCreationRequest request) {
        Cart cart = cartRepository.getByUserId(request.getUserId()).orElse(null);
        if (cart == null){
            cart = new Cart();
            cart.setUserId(request.getUserId());
        }

        Product product = productRepository.getById(request.getProductId()).orElse(null);
        if (product == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "productId = " + request.getProductId());

        CartItem cartItem = request.toCustomerCartItemEntity();
        cartItem.setProduct(product);
        try {
            cartItemRepository.save(cartItem);
            cart.getCartItemList().add(cartItem);
            cartRepository.save(cart);
            return MessageCode.SUCCESS;
        }catch (Exception e){
            throw new ApiException(e, MessageCode.ERROR);
        }
    }

    public MessageCode deleteCartItem(Long id) {
        CartItem cartItem = cartItemRepository.getById(id).orElse(null);
        if (cartItem == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "Id = " + id);

        try{
            cartItemRepository.delete(cartItem);
            return MessageCode.SUCCESS;
        }catch (Exception e){
            throw new ApiException(e, MessageCode.ERROR);
        }
    }

    public CashierCartResponse mapOrderToCart(Long orderId) {
        Order order = orderRepository.getById(orderId).orElse(null);
        if (order == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "orderId = "+ orderId);

        Cart cart = cartRepository.getByUserId(order.getUserId()).orElse(null);
        if (cart == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "get Cart by userId = " + order.getUserId());

        cart.setUserId(order.getUserId());
        cart.setPayments(order.getPayments());
        cart.setIsOrderAtTable(order.getIsOrderAtTable());
        cart.setOrderId(orderId);
        order.getOrderItemList().forEach(item -> {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(item.getProduct());
            cartItem.setQuantity(item.getQuantity());
            cartItem.setSizeSelectedId(item.getSizeSelectedId());
            cart.getCartItemList().add(cartItem);
            try {
                cartItemRepository.save(cartItem);
            }catch (Exception e){
                throw new ApiException(MessageCode.ERROR);
            }
            cart.getCartItemList().add(cartItem);
        });
        try {
            cartRepository.save(cart);
        }catch (Exception e){
            throw new ApiException(MessageCode.ERROR);
        }
        return cart.toCashierCartResponse();
    }

    public MessageCode deleteCart(String userId) {
        Cart cart = cartRepository.getByUserId(userId).orElse(null);
        if (cart == null)throw new ApiException(MessageCode.ID_NOT_FOUND, "userId: " + userId);
        try {
            cartRepository.delete(cart);
            return MessageCode.SUCCESS;
        }catch(Exception e){
            throw new ApiException(e, MessageCode.ERROR);
        }
    }

    public MessageCode updateNote(CartUpdateNoteRequest request) {
        CartItem cartItem = cartItemRepository.getById(request.getId()).orElse(null);

        if (cartItem == null)throw new ApiException(MessageCode.ID_NOT_FOUND);
        cartItem.setNote(request.getNote());
        try{
            cartItemRepository.save(cartItem);
            return MessageCode.SUCCESS;
        }catch (Exception e){
            throw new ApiException(MessageCode.ERROR);
        }
    }
}
