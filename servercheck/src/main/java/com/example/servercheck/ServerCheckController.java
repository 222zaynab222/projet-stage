package com.example.servercheck;

import org.springframework.web.bind.annotation.*;
import java.sql.Connection;
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
}