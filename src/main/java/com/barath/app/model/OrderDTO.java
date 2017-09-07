package com.barath.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by barath.arivazhagan on 9/7/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDTO {

    private String productName;

    private String locationName;

    private Long orderID;

    private Long posId;

    private Long consumerId;

    private int quantity;

    private String status;

    private Double amount;

    private String tagId;

    private String uom;

    private String externalRef;

    public String getExternalRef() {
        return externalRef;
    }

    public void setExternalRef(String externalRef) {
        this.externalRef = externalRef;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public Long getPosId() {
        return posId;
    }

    public void setPosId(Long posId) {
        this.posId = posId;
    }

    public Long getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public OrderDTO(String productName, String locationName, Long orderID, Long posId, Long consumerId, int quantity, String status, String tagId, String uom, Double amount,String externalRef) {
        this.productName = productName;
        this.locationName = locationName;
        this.orderID = orderID;
        this.posId = posId;
        this.consumerId = consumerId;
        this.quantity = quantity;
        this.status = status;
        this.tagId = tagId;
        this.uom = uom;
        this.amount=amount;
        this.externalRef=externalRef;
    }

    public OrderDTO() {
    }

    public OrderDTO(String productName, String locationName, Long posId, Long consumerId, int quantity, Double amount) {
        this.amount=amount;
        this.productName = productName;
        this.locationName = locationName;
        this.posId = posId;
        this.consumerId = consumerId;
        this.quantity = quantity;
    }


}
