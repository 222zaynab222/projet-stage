package com.example.servercheck;

import org.springframework.web.bind.annotation.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ServerCheckController {

    @GetMapping("/check")
    public Map<String, Object> checkServer(@RequestParam String serverName) {
        Map<String, Object> result = new HashMap<>();
        try (Connection connection = ConnectionDBB.getConnection()) {
            FetchAAS fetchAAS = new FetchAAS(connection);
            FetchAPS fetchAPS = new FetchAPS(connection);
            boolean hasAAS = fetchAAS.existsAAS(serverName);
            int apsCount = fetchAPS.countAPS(serverName);
            boolean hasAPS = apsCount > 0;

            result.put("serverName", serverName);
            result.put("hasAAS", hasAAS);
            result.put("hasAPS", hasAPS);
            result.put("apsCount", apsCount);
        } catch (SQLException e) {
            result.put("error", e.getMessage());
        }
        return result;
    }

    @PostMapping("/addAAS")
    public Map<String, Object> addAAS(@RequestParam String serverName) {
        Map<String, Object> result = new HashMap<>();
        try (Connection connection = ConnectionDBB.getConnection()) {
            String propKey = "aas." + serverName;
            String query = "INSERT INTO au_property (prop_key) VALUES (?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, propKey);
                int rows = stmt.executeUpdate();
                result.put("success", rows > 0);
            }
        } catch (SQLException e) {
            result.put("error", e.getMessage());
        }
        return result;
    }

    @PostMapping("/addAPS")
    public Map<String, Object> addAPS(@RequestParam String serverName, @RequestBody APSRequest apsRequest) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        try (Connection connection = ConnectionDBB.getConnection()) {
            String query = "INSERT INTO au_property (prop_key) VALUES (?)";
            for (APSSuffix suffix : apsRequest.suffixes) {
                String propKey = String.format("presentation-%s.%s.%s", serverName, suffix.part1, suffix.part2);
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, propKey);
                    int rows = stmt.executeUpdate();
                    if (rows > 0) successCount++;
                }
            }
            result.put("successCount", successCount);
        } catch (SQLException e) {
            result.put("error", e.getMessage());
        }
        return result;
    }

    // Helper classes for APS request body
    public static class APSRequest {
        public APSSuffix[] suffixes;
    }
    public static class APSSuffix {
        public String part1;
        public String part2;
    }
}