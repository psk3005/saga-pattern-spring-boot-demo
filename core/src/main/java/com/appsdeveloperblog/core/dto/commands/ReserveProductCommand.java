package com.appsdeveloperblog.core.dto.commands;

import java.util.UUID;

public class ReserveProductCommand {

    private UUID productId;
    private Integer prodcutQuantity;
    private UUID orderId;

    public ReserveProductCommand() {
    }

    public ReserveProductCommand(UUID productId, Integer prodcutQuantity, UUID orderId) {
        this.productId = productId;
        this.prodcutQuantity = prodcutQuantity;
        this.orderId = orderId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Integer getProdcutQuantity() {
        return prodcutQuantity;
    }

    public void setProdcutQuantity(Integer prodcutQuantity) {
        this.prodcutQuantity = prodcutQuantity;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
}
