package com.example;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class DistanceMatrixExample {

    private static final String API_KEY = "UI9NFuKpugoYgLKnmVxjl9rfNTaoUXqcE7qrEmMRNxOhGbIMguCym2nVOiCgQUGU"; // Replace with your DistanceMatrix.ai API key

    public static void main(String[] args) {
        double lat1 = 40.7128; // Latitude of point 1
        double lon1 = -74.0060; // Longitude of point 1
        double lat2 = 34.0522; // Latitude of point 2
        double lon2 = -118.2437; // Longitude of point 2

        String distance = getDistance(lat1, lon1, lat2, lon2);

        if (distance != null) {
            System.out.println("The distance between the points is: " + distance);
        } else {
            System.out.println("Unable to get the distance for the given points.");
        }
    }

    public static String getDistance(double lat1, double lon1, double lat2, double lon2) {
        OkHttpClient client = new OkHttpClient();

        String origins = lat1 + "," + lon1;
        String destinations = lat2 + "," + lon2;
        String url = "https://api.distancematrix.ai/maps/api/distancematrix/json?origins=" + origins
                + "&destinations=" + destinations + "&key=" + API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return null;
            }

            String jsonResponse = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonResponse);

            if ("OK".equals(jsonObject.getString("status"))) {
                JSONArray rows = jsonObject.getJSONArray("rows");
                JSONObject elements = rows.getJSONObject(0).getJSONArray("elements").getJSONObject(0);
                if ("OK".equals(elements.getString("status"))) {
                    JSONObject distance = elements.getJSONObject("distance");
                    return distance.getString("text");
                } else {
                    System.out.println("Error: " + elements.getString("status"));
                }
            } else {
                System.out.println("Error: " + jsonObject.getString("status"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
