package hust.server.domain.orders.entity;

import hust.server.app.exception.ApiException;
import hust.server.domain.BaseEntity;
import hust.server.domain.orders.dto.response.AdminOrderDetailsResponse;
import hust.server.domain.orders.dto.response.AdminOrderResponse;
import hust.server.domain.orders.dto.response.CashierOrderResponse;
import hust.server.domain.orders.dto.response.GuestOrderResponse;
import hust.server.infrastructure.enums.MessageCode;
import hust.server.infrastructure.utilies.Utility;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

import static hust.server.infrastructure.utilies.Utility.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "table_number")
    Integer tableNumber;

    @Column(name = "token")
    String token;

    @Column(name = "status")
    Integer status; // 0: Chờ xác nhận, 1: Đang pha chế, 2: Đã xong, 3: Đã hủy

    @Column(name = "branch_id")
    Long branchId;

    @Column(name = "total")
    Long total;

    @Column(name = "user_id")
    String userId;

    @Column(name = "grand_total")
    Long grandTotal;

    @Column(name = "discount")
    Long discount;

    @Column(name = "promotion_id")
    Integer promotionId;

    @OneToMany
    @JoinColumn(name = "order_id")
    List<OrderItem> orderItemList;

    @Column
    Integer payments; // 0: Chuyển khảon

    @Column(name = "is_order_at_table")
    Integer isOrderAtTable;

    @Column(name = "is_customer_order")
    Integer isCustomerOrder;

    @Column
    String code;

    @Column(name = "updated_by")
    String updatedBy;

    @Column(name = "created_by")
    String createdBy;

    @Column
    String note;



    public GuestOrderResponse toGuestOrderResponse(){
        return GuestOrderResponse.builder()
                .id(id)
                .orderAt(toLocalDateTime(createdAt, null))
                .orderItemList(orderItemList.stream().map(OrderItem::toGuestOrderItemResponse).collect(Collectors.toList()))
                .status(convertStatus())
                .build();
    }

    public CashierOrderResponse toCashierOrderResponse(){
        boolean isExpanded;
        switch (status){
            case 0:
            case 1:
                isExpanded = true;
                break;
            case 2:
            case 3:
                isExpanded = false;
                break;
            default:
                throw new ApiException(MessageCode.ERROR, "status = default");
        }

        return CashierOrderResponse.builder()
                .orderAt(toLocalDateTime(createdAt, null))
                .orderItemList(orderItemList.stream().map(OrderItem::toCashierOrderItemResponse).collect(Collectors.toList()))
                .status(convertStatus())
                .code(code)
                .isOrderAtTable(convertDelivery())
                .payments(converPayments())
                .id(id)
                .total(formatCurrency(total))
                .isExpanded(isExpanded)
                .build();
    }
    public AdminOrderResponse toAdminOrderResponse(){
        return AdminOrderResponse.builder()
                .id(id)
                .datetime(toLocalDateTime(createdAt, ""))
                .payments(converPayments())
                .status(convertStatus())
                .total(formatCurrency(total))
                .branchId(branchId)
                .note(note)
                .build();
    }

    public AdminOrderDetailsResponse toAdminOrderDetailResponse() {
        return AdminOrderDetailsResponse.builder()
                .id(id)
                .datetime(Utility.toLocalDateTime(createdAt, ""))
                .payments(converPayments())
                .status(convertStatus())
                .orderItem(orderItemList.stream().map(OrderItem::toAdminOrderItemResponse).collect(Collectors.toList()))
                .note(note)
                .build();
    }

    private String convertStatus(){
        switch (status){
            case 0:
                return  "Chờ xác nhận";
            case 1:
                return "Đang pha chế";
            case 2:
                return "Hoàn thành";
            case 3:
                return "Đã hủy";
            default:
                throw new ApiException(MessageCode.ERROR, "status = default");
        }
    }

    private String converPayments(){
        switch (payments){
            case 0:
                return "Tiền mặt";
            case 1:
                return "Chuyển khoản";
            case 2:
                return "VNPay";
            default:
                throw new ApiException(MessageCode.ERROR, "payment = default");
        }
    }

    private String convertDelivery(){
        switch (isOrderAtTable){
            case 0:
                return "Mang đi";
            case 1:
                return "Tại bàn";
            default:
                throw new ApiException(MessageCode.ERROR, "isOrderAtTable = default");
        }
    }
}
