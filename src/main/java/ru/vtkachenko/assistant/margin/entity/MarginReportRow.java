package ru.vtkachenko.assistant.margin.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MarginReportRow {
    private String label;
    private List<Double> values;
}
