package com.gkvk.patangasuchaka.util;

import android.location.Geocoder;
import android.util.Log;

import java.sql.Timestamp;

public class TestClass {
    public static void main(String[] args) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp.getTime());
    }
}
