package org.example.api.controller;

import org.example.api.util.OpenTsdbService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/tables")
public class TableController {

    @GetMapping("/table/{id}")
    public Map<String, Object> getTableData(@RequestParam("numberOfRows") String Rows, @PathVariable String id) {
        Map<String, Object> tableData = new HashMap<>();
        OpenTsdbService openTsdbService = new OpenTsdbService(new RestTemplate());
        int numberOfRows = Integer.parseInt(Rows);

        List<String> columns = Arrays.asList("timestamp", "cpu", "storage", "memory");
        List<List<Object>> rows = new ArrayList<>();
        Map<String, List<OpenTsdbService.MetricData>> allMetrics = openTsdbService.getAllMetrics(id);
        List<OpenTsdbService.MetricData> metricDataList = new ArrayList<>();

        for (int i = 0; i < numberOfRows; i++) {
            List<Object> row = new ArrayList<>();
            for (String metric : columns) {
                if (metric.equals("timestamp")) {
                    List<OpenTsdbService.MetricData> timestampDataList = allMetrics.get("cpu");
                    if (timestampDataList != null && timestampDataList.size() > i) {
                        row.add(timestampDataList.get(i).timestamp());
                    } else {
                        row.add("0");
                    }
                } else {
                    metricDataList = allMetrics.get(metric);
                    if (metricDataList != null && metricDataList.size() > i) {
                        row.add(metricDataList.get(i).value());
                    } else {
                        row.add(0.0);
                    }
                }
            }
            // Dodaj wiersz do wierszy
            rows.add(row);
        }

        tableData.put("columns", columns);
        tableData.put("rows", rows);

        return tableData;
    }
}