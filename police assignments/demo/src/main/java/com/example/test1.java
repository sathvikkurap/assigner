package com.example;

import java.util.ArrayList;

public class test1 {
    public static void main(String[] args) {
        // Initialize stations
        ArrayList<Station> stations = new ArrayList<>();
        ArrayList<Officer> officers = new ArrayList<>();
        
        Station mali = new Station(14.43468, -9.75586, new ArrayList<>(), "Mali");
        Station chad = new Station(16.63619, 20.6543, new ArrayList<>(), "Chad");
        Station drc = new Station(-0.70311, 16.25977, new ArrayList<>(), "DRC");
        Station algeria = new Station(22.26876, 4.6582, new ArrayList<>(), "Algeria");
        Station kazak = new Station(49.95122, 69.52148, new ArrayList<>(), "Kazakhstan");
        Station australia = new Station(-21.25054, 126.43294, new ArrayList<>(), "Australia");
        
        stations.add(mali);
        stations.add(chad);
        stations.add(drc);
        stations.add(algeria);
        stations.add(kazak);
        stations.add(australia);

        // Initialize officers
        Officer vinod = new Officer("Vinod", 50.34598, 90.77018, -300, -300, false, 3, false, false, 0, null);
        // Officer rahul = new Officer("Rahul", 21.20822, 102.72331, 14.43468, -9.75586, false, 3, true, true, 0, mali);
        // Officer priya = new Officer("Priya", 27.29442, 13.25065, 22.26876, 4.6582, false, 3, false, true, 0, algeria);
        // Officer rohan = new Officer("Rohan", 31.27925, 41.72721, -0.70311, 16.25977, false, 3, false, true, 0, drc);
        Officer priyanka = new Officer("Priyanka", 43.00525, 19.93034, -21.25054, 126.43294, false, 3, true, true, 2, australia);
        Officer raja = new Officer("Raja", 8.32102, 24.85221, -300, -300, false, 3, false, false, 0, null);

        officers.add(vinod);
        // officers.add(rahul);
        // officers.add(priya);
        // officers.add(rohan);
        officers.add(priyanka);
        officers.add(raja);

        // Create Assigner instance
        Assigner assigner = new Assigner(stations, officers);

        // Assign officers and print results
        Station assignedStationVinod = assigner.assign(vinod);
        System.out.println("Officer Vinod is assigned to: " + assignedStationVinod.name);
        vinod.reassign(assignedStationVinod);
        Station assignedStationRaja = assigner.assign(raja);
        System.out.println("Officer Raja is assigned to: " + assignedStationRaja.name);
    }
}
