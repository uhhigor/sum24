package org.example.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.api.util.OpenTsdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tables")
public class TableController {

    OpenTsdbService openTsdbService;

    @Autowired
    public TableController(OpenTsdbService openTsdbService) {
        this.openTsdbService = openTsdbService;
    }

    @GetMapping("/table/{id}")
    public Map<String, Object> getTableData(@RequestParam("numberOfRows") String Rows, @PathVariable Integer id) {
        Map<String, Object> tableData = new HashMap<>();
        int numberOfRows = Integer.parseInt(Rows);

        String allMetricsJson = openTsdbService.getAllMetricsOfService(id);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode metricsArray = objectMapper.readTree(allMetricsJson);

            Set<String> columns = new LinkedHashSet<>();
            Map<Long, Map<String, Object>> rowsData = new HashMap<>();

            for (JsonNode metricNode : metricsArray) {
                String metricName = metricNode.path("tags").path("metricName").asText();
                JsonNode dps = metricNode.path("dps");

                for (Iterator<String> it = dps.fieldNames(); it.hasNext(); ) {
                    String timestampStr = it.next();
                    Long timestamp = Long.parseLong(timestampStr);
                    Object value = dps.path(timestampStr).asDouble(); // Assuming all values are doubles

                    rowsData.computeIfAbsent(timestamp, k -> new HashMap<>()).put(metricName, value);
                }

                columns.add(metricName);
            }

            columns.add("timeunix");
            List<String> columnsList = new ArrayList<>(columns);
            if (columnsList.remove("timeunix")) {
                columnsList.add(0, "timeunix");
            }

            for (Long timestamp : rowsData.keySet()) {
                rowsData.get(timestamp).put("timeunix", timestamp);
            }

            List<Map<String, Object>> limitedRows = rowsData.entrySet().stream()
                    .sorted((e1, e2) -> {
                        Long time1 = (Long) e1.getValue().get("timeunix");
                        Long time2 = (Long) e2.getValue().get("timeunix");
                        return time2.compareTo(time1);
                    })
                    .limit(numberOfRows)
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());

            tableData.put("columns", columnsList);
            tableData.put("rows", limitedRows);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tableData;
    }
}