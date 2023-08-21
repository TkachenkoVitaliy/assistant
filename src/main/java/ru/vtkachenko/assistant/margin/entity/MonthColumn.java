package ru.vtkachenko.assistant.margin.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MonthColumn {
    private String columnName;
    private List<Double> values = new ArrayList<>();
}
