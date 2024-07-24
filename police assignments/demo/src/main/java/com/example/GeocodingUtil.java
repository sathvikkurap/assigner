package com.example;

import java.io.IOException;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GeocodingUtil {
    private static final String GEOCODING_API_KEY = System.getenv("GEOCODING_API_KEY");

    public static double[] getCoordinates(String address) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + GEOCODING_API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String jsonResponse = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject location = jsonObject.getJSONArray("results")
                                             .getJSONObject(0)
                                             .getJSONObject("geometry")
                                             .getJSONObject("location");

            double lat = location.getDouble("lat");
            double lon = location.getDouble("lng");
            return new double[]{lat, lon};
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
