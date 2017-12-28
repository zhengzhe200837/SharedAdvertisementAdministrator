package com.wind.main.util;

import com.wind.main.network.model.OrderInfo;

import java.util.List;

/**
 * Created by xiaoxiao on 2017/12/22.
 */

public class OrderInfoUtil {
    private static List<OrderInfo> orderInfos;
    public static void setOrders(OrderInfo orderInfo){
        orderInfos.add(orderInfo);
    }

    public static List<OrderInfo> getOrders(){
        return orderInfos;
    }
}
