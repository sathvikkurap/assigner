package com.example;

import java.util.ArrayList;

public class Station {
    double lat;
    double longi;
    ArrayList<Officer> currentStationedOfficers;
    String name;

    public Station(double lat, double longi, ArrayList<Officer> currentStationedOfficers, String name) {
        this.lat = lat;
        this.longi = longi;
        this.currentStationedOfficers = currentStationedOfficers != null ? currentStationedOfficers : new ArrayList<>();
        this.name = name;
    }

    public boolean isStationed(Officer officer) {
        return currentStationedOfficers.contains(officer);
    }

    public void addToStation(Officer officer) {
        if (!isStationed(officer)) {
            currentStationedOfficers.add(officer);
        }
    }

    public void removeFromStation(Officer officer) {
        currentStationedOfficers.remove(officer);
    }

    public int rankEmpty(int rank) {
        for (Officer officer : currentStationedOfficers) {
            if (officer.rank == rank) {
                return officer.timeInStation;
            }
        }
        return -1;
    }

    public void printStationInfo() {
        System.out.println("Station: " + name);
        System.out.println("Location: (" + lat + ", " + longi + ")");
        System.out.println("Officers:");
        for (Officer officer : currentStationedOfficers) {
            System.out.println(" - " + officer.name + ", Rank: " + officer.rank);
        }
    }

    // Example of initializing some data
    public static void main(String[] args) {
        ArrayList<Officer> officers = new ArrayList<>();
        officers.add(new Officer("John Doe", 12.9715987, 77.5945627, 12.9715987, 77.5945627, false, 1, false, true, 3, null));
        officers.add(new Officer("Jane Smith", 12.9715987, 77.5945627, 12.9715987, 77.5945627, false, 2, false, true, 5, null));
        
        Station station = new Station(12.9715987, 77.5945627, officers, "Central Station");
        station.printStationInfo();
    }
}
