package com.searoute;

import eu.europa.ec.eurostat.jgiscotools.feature.Feature;
import eu.europa.ec.eurostat.searoute.SeaRouting;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RouteController {

    private SeaRouting seaRouting;

    @PostConstruct
    public void init() {
        // Resolution 20km — good balance of accuracy vs speed
        seaRouting = new SeaRouting(20);
    }

    @GetMapping("/searoute")
    public Map<String, Object> getRoute(
        @RequestParam double olon,
        @RequestParam double olat,
        @RequestParam double dlon,
        @RequestParam double dlat
    ) {
        Feature route = seaRouting.getRoute(olon, olat, dlon, dlat);

        double lengthKm = 0.0;
        Object distAttr = route.getAttribute("distKM");

        if (distAttr != null) {
            lengthKm = Double.parseDouble(distAttr.toString());
        } else {
            // Direct route returned (no network needed) — compute haversine
            lengthKm = haversineKm(olat, olon, dlat, dlon);
        }

        Map<String, Object> properties = new HashMap<>();
        properties.put("length", lengthKm);

        Map<String, Object> response = new HashMap<>();
        response.put("type", "Feature");
        response.put("properties", properties);

        return response;
    }

    private double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                 * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
