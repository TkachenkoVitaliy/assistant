package ru.vtkachenko.assistant.margin.entity;

import com.poiorm.annotation.WritableCells;
import com.poiorm.annotation.WritableHeader;
import com.poiorm.annotation.WritableObject;
import com.poiorm.type.Direction;
import com.poiorm.type.WriteCellFormat;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@WritableObject(direction = Direction.COLUMN)
public class MonthColumn {
    @WritableHeader(cellFormat = WriteCellFormat.STRING)
    private String columnName;
    @WritableCells(cellFormat = WriteCellFormat.PERCENTAGE)
    private List<Double> values = new ArrayList<>();
}
