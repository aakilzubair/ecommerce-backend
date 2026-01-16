package com.ecommerce_backend.service;

import com.ecommerce_backend.dto.CartItemVIewRequestDTO;
import com.ecommerce_backend.dto.CartItemViewResponseDTO;
import com.ecommerce_backend.dto.CartRequestDTO;
import com.ecommerce_backend.dto.CartResponseDTO;
import com.ecommerce_backend.entity.Cart;
import com.ecommerce_backend.entity.CartItem;
import com.ecommerce_backend.entity.Product;
import com.ecommerce_backend.entity.User;
import com.ecommerce_backend.exception.ResourceNotFoundException;
import com.ecommerce_backend.repository.CartItemRepository;
import com.ecommerce_backend.repository.CartRepository;
import com.ecommerce_backend.repository.ProductRepository;
import com.ecommerce_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       UserRepository userRepository,
                       ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }
    public CartResponseDTO addToCart(CartRequestDTO dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // 1️⃣ Find or create cart
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setUser(user);
                    c.setTotalPrice(0.0);
                    return cartRepository.save(c);
                });

        // 2️⃣ Find or create cart item
        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), product.getId())
                .orElseGet(() -> {
                    CartItem ci = new CartItem();
                    ci.setCart(cart);
                    ci.setProduct(product);
                    ci.setQuantity(0);
                    ci.setPrice(0.0);
                    return ci;
                });

        // 3️⃣ Update quantity & price
        int updatedQty = item.getQuantity() + dto.getQuantity();
        item.setQuantity(updatedQty);
        item.setPrice(product.getPrice() * updatedQty);
        cartItemRepository.save(item);

        // 4️⃣ Update cart total
        double total = cartItemRepository.findByCartId(cart.getId())
                .stream()
                .mapToDouble(CartItem::getPrice)
                .sum();

        cart.setTotalPrice(total);
        cartRepository.save(cart);

        // 5️⃣ Response DTO
        CartResponseDTO response = new CartResponseDTO();
        response.setCartId(cart.getId());
        response.setTotalPrice(cart.getTotalPrice());

        return response;
    }





    public List<CartItemViewResponseDTO> viewCart(CartItemVIewRequestDTO requestDTO) {

        // 1. Find cart by userId
        Cart cart = cartRepository.findByUserId(requestDTO.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found"));

        // 2. Fetch cart items
        List<CartItem> cartItems =
                cartItemRepository.findByCartId(cart.getId());

        // 3. Map to response DTO
        List<CartItemViewResponseDTO> responseList = new ArrayList<>();

        for (CartItem item : cartItems) {
            CartItemViewResponseDTO dto = new CartItemViewResponseDTO();
            dto.setCartItemId(item.getId());
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
            dto.setQuantity(item.getQuantity());
            dto.setPrice(item.getPrice());

            responseList.add(dto);
        }

        return responseList;
    }

    public void updateQuantity(CartItemVIewRequestDTO requestDTO) {

        CartItem item = cartItemRepository.findById(requestDTO.getCartItemId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart item not found"));

        item.setQuantity(requestDTO.getQuantity());
        item.setPrice(item.getProduct().getPrice() * requestDTO.getQuantity());
        cartItemRepository.save(item);

        // update cart total
        Cart cart = item.getCart();
        double total = cartItemRepository.findByCartId(cart.getId())
                .stream()
                .mapToDouble(CartItem::getPrice)
                .sum();

        cart.setTotalPrice(total);
        cartRepository.save(cart);
    }


    // 2️⃣ REMOVE ITEM
    public void removeItem(Long cartItemId) {

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart item not found"));

        Cart cart = item.getCart();
        cartItemRepository.delete(item);

        // update cart total
        double total = cartItemRepository.findByCartId(cart.getId())
                .stream()
                .mapToDouble(CartItem::getPrice)
                .sum();

        cart.setTotalPrice(total);
        cartRepository.save(cart);
    }




}
