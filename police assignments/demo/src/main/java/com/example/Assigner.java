package com.example;

import java.util.ArrayList;

public class Assigner {
    ArrayList<Station> stations;
    ArrayList<Officer> officers;

    public Assigner(ArrayList<Station> stations, ArrayList<Officer> officers) {
        this.stations = stations;
        this.officers = officers;
    }

    public void assignNewOfficer(Officer officer, Station station) {
        officer.reassign(station);
    }

    public ArrayList<Station> emptyStations(Officer officer) {
        ArrayList<Station> emptyStations = new ArrayList<>();
        for (Station station : stations) {
            int temp = station.rankEmpty(officer.rank);
            if (temp == -1 || temp >= 2) {
                emptyStations.add(station);
            }
        }
        return emptyStations;
    }

    public Station assign(Officer officer) {
        ArrayList<Station> candidates = emptyStations(officer);
        if (candidates.isEmpty()) {
            candidates = stations;
        }

        Station bestStation = null;
        double maxPriority = 0;
        for (Station station : candidates) {
            double priority = officer.calculatePriority(station);
            if (priority > maxPriority) {
                maxPriority = priority;
                bestStation = station;
            }
        }

        return bestStation;
    }

    private Officer findOfficerAtStation(Station station, int rank) {
        for (Officer officer : station.currentStationedOfficers) {
            if (officer.rank == rank) {
                return officer;
            }
        }
        return null;
    }
}
