package com.rocel.ksd.temp;

import com.rocel.ksd.IWebService;
import com.rocel.ksd.WebService;

public class Main {

    public static void main(String[] args) {
        IWebService ws = new WebService();
        ws.start(null, "localhost", 5000 );
    }

}
