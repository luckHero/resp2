package com.itheima.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Lucky
 * @Date:2020-02-11 0:33
 */
/*
    封装预约人数的实体类
 */

public class Reservations implements Serializable {
    private Integer date;
    private Long number;
    private Long reservations;

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getReservations() {
        return reservations;
    }

    public void setReservations(Long reservations) {
        this.reservations = reservations;
    }

    public Reservations(Integer date, Long number, Long reservations) {
        this.date = date;
        this.number = number;
        this.reservations = reservations;
    }

    public Reservations() {
    }
}
