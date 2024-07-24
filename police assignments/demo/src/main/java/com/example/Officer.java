package com.example;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Officer {
    private static final String API_KEY = "UI9NFuKpugoYgLKnmVxjl9rfNTaoUXqcE7qrEmMRNxOhGbIMguCym2nVOiCgQUGU";
    double lat;
    double longi;
    double curLat;
    double curLong;
    boolean criminalHistory;
    int rank;
    boolean medicalProblems;
    boolean assigned;
    int timeInStation;
    String name;
    Station curStation;

    public Officer(String name, double lat, double longi, double curLat, double curLong, boolean criminalHistory, int rank, boolean medicalProblems, boolean assigned, int timeInStation, Station curStation) {
        this.lat = lat;
        this.longi = longi;
        this.curLat = curLat;
        this.curLong = curLong;
        this.criminalHistory = criminalHistory;
        this.rank = rank;
        this.medicalProblems = medicalProblems;
        this.assigned = assigned;
        this.timeInStation = timeInStation;
        this.name = name;
        this.curStation = curStation;
        if (curStation != null) {
            curStation.addToStation(this);
        }
    }

    public double calculatePriority(Station station) {
        double locLat = station.lat;
        double locLong = station.longi;
        if (assigned && timeInStation < 2) {
            return Double.NEGATIVE_INFINITY; // Can't be moved
        }

        double priority = 0;
        int basePriority = 50;

        if (station.rankEmpty(rank) != -1) {
            basePriority = 35;
        }

        int distFromHome = getDistance(lat, longi, locLat, locLong);
        if (distFromHome == -1) {
            distFromHome = calculateHaversineDistance(lat, longi, locLat, locLong);
            System.out.println("Fallback Distance from Home to " + station.name + " (Haversine): " + distFromHome + " meters");
        } else {
            System.out.println("Distance from Home: " + distFromHome +  "to " + station.name + " meters");
        }

        if (medicalProblems) {
            priority += 50;
        }

        if (criminalHistory) {
            priority -= 40;
        }

        priority += (10 - rank) * 5;

        if (timeInStation >= 3) {
            priority += 10;
        }

        double distanceFactor = distFromHome / 1000.0;
        if (distanceFactor > 75) {
            distanceFactor = 75;
        }
        priority -= distanceFactor;

        priority += basePriority;
        priority = Math.max(priority, 0);

        return priority;
    }

    public void reassign(Station station) {
        if (curStation != null) {
            curStation.removeFromStation(this);
        }
        station.addToStation(this);
        assigned = true;
        timeInStation = 0;
        curStation = station;
    }

    public static int getDistance(double lat1, double lon1, double lat2, double lon2) {
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
                return -1;
            }

            String jsonResponse = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonResponse);

            if ("OK".equals(jsonObject.getString("status"))) {
                JSONArray rows = jsonObject.getJSONArray("rows");
                JSONObject elements = rows.getJSONObject(0).getJSONArray("elements").getJSONObject(0);
                if ("OK".equals(elements.getString("status"))) {
                    JSONObject distance = elements.getJSONObject("distance");
                    return distance.getInt("value");
                } else {
                    System.out.println("Error: " + elements.getString("status"));
                }
            } else {
                System.out.println("Error: " + jsonObject.getString("status"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static int calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Radius of the Earth in meters
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (int) (R * c);
    }

    // New method to check if officer is eligible for reassignment
    public boolean isEligibleForReassignment() {
        return timeInStation >= 2;
    }
}
