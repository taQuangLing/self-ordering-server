package hust.server.domain.orders.entity;

import hust.server.app.exception.ApiException;
import hust.server.domain.BaseEntity;
import hust.server.domain.orders.dto.response.AdminOrderItemResponse;
import hust.server.domain.orders.dto.response.CashierOrderItemResponse;
import hust.server.domain.orders.dto.response.GuestOrderItemResponse;
import hust.server.domain.products.entity.Product;
import hust.server.domain.products.entity.ProductSize;
import hust.server.infrastructure.enums.MessageCode;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

import static hust.server.infrastructure.utilies.Utility.formatCurrency;

@Data
@Entity
@Builder
@Table(name = "order_item")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    Product product;

    @Column(name = "discount", precision = 10, scale = 2)
    Long discount;

    @Column(name = "quantity")
    Integer quantity;

    @Column(name = "size_selected_id")
    Long sizeSelectedId;

    @Column(name = "product_price")
    Long productPrice;

    @Column
    String note;

    public GuestOrderItemResponse toGuestOrderItemResponse(){
        String sizeStr = null;
        if (product.getHasSize() == 1){
            ProductSize productSize = product.getSizeList().stream().filter(size -> size.getId().equals(sizeSelectedId)).findAny().orElse(null);
            if (productSize == null)throw new ApiException(MessageCode.ERROR);
            sizeStr = productSize.getSize();
        }
        if (note.isEmpty() || note == null)note = "<none>";
        return GuestOrderItemResponse.builder()
                .productName(product.getName())
                .productImg(product.getImg())
                .productPrice(formatCurrency(productPrice))
                .quantity(quantity)
                .hasSize(product.getHasSize())
                .sizeSelected(sizeStr)
                .note(note)
                .id(id)
                .build();
    }

    public CashierOrderItemResponse toCashierOrderItemResponse(){
        String sizeStr = null;
        if (product.getHasSize() == 1){
            ProductSize productSize = product.getSizeList().stream().filter(size -> size.getId().equals(sizeSelectedId)).findAny().orElse(null);
            if (productSize == null)throw new ApiException(MessageCode.ERROR);
            sizeStr = productSize.getSize();
        }

        return CashierOrderItemResponse.builder()
                .productName(product.getName())
                .productPrice(formatCurrency(productPrice))
                .quantity(quantity)
                .sizeSelected(sizeStr)
                .note(note)
                .id(id)
                .build();
    }

    public AdminOrderItemResponse toAdminOrderItemResponse() {
        String sizeStr = "";
        if (product.getHasSize() == 1){
            ProductSize productSize = product.getSizeList().stream().filter(size -> size.getId().equals(sizeSelectedId)).findAny().orElse(null);
            if (productSize == null)throw new ApiException(MessageCode.ERROR);
            sizeStr = productSize.getSize();
        }
        return AdminOrderItemResponse.builder()
                .id(id)
                .name(product.getName())
                .price(productPrice)
                .priceDisplay(formatCurrency(productPrice).substring(0, formatCurrency(productPrice).length() - 2))
                .quantity(quantity)
                .size(sizeStr)
                .build();
    }
}
