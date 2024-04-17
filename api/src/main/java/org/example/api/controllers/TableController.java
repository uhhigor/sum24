package org.example.api.controllers;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/tables")
public class TableController {

    @GetMapping("/table")
    public Map<String, Object> getTableData(@RequestParam("numberOfRows") String Rows) {
        Map<String, Object> tableData = new HashMap<>();
        int numberOfRows = Integer.parseInt(Rows);
        System.out.println("Number of rows: " + numberOfRows);

        List<String> columns = Arrays.asList("Column 1", "Column 2", "Column 3");
        List<List<Integer>> rows = new ArrayList<>();

        for (int i = 1; i <= numberOfRows; i++) {
            rows.add(Arrays.asList(i, i * 2, i * 3));
        }

        tableData.put("columns", columns);
        tableData.put("rows", rows);

        return tableData;
    }
}