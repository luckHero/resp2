package com.itheima.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Lucky
 * @Date:2020-02-16 11:49
 */
public class OrderDetail implements Serializable {
    private String setmealId; //套餐id
    private String sex;
    private String telephone;
    private String orderDate;
    private String name;
    private String idCard;
    private String orderType;
    private String validateCode;

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getSetmealId() {
        return setmealId;
    }

    public void setSetmealId(String setmealId) {
        this.setmealId = setmealId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public OrderDetail() {
    }

    public OrderDetail(String setmealId, String sex, String telephone, String orderDate, String name, String idCard, String orderType) {
        this.setmealId = setmealId;
        this.sex = sex;
        this.telephone = telephone;
        this.orderDate = orderDate;
        this.name = name;
        this.idCard = idCard;
        this.orderType = orderType;
    }
}
