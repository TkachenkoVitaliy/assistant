package ru.vtkachenko.assistant.margin.entity;

import com.poiorm.annotation.WritableCells;
import com.poiorm.annotation.WritableHeader;
import com.poiorm.annotation.WritableObject;
import com.poiorm.type.Direction;
import com.poiorm.type.WriteCellFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@WritableObject(direction = Direction.COLUMN)
public class LabelColumn {
    @WritableHeader(cellFormat = WriteCellFormat.STRING)
    private String columnName;
    @WritableCells(cellFormat = WriteCellFormat.STRING)
    private List<String> values = new ArrayList<>();
}
