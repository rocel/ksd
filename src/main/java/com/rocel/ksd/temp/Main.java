package com.rocel.ksd.temp;

import com.rocel.ksd.IWebService;
import com.rocel.ksd.WebService;

public class Main {

    public static void main(String[] args) {
        IWebService ws = new WebService();
        ws.start(null, 5000, "172.16.202.245:2181,172.16.202.248:2181,172.16.202.212:2181/kafka");
    }

}
