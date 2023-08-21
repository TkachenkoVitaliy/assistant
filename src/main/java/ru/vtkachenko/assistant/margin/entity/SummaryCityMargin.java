package ru.vtkachenko.assistant.margin.entity;

import com.poiorm.annotation.ExcelCell;
import com.poiorm.annotation.IdentifierField;
import com.poiorm.annotation.IdentifierMethod;
import com.poiorm.annotation.RowObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RowObject(startRowIndex = 5)
public class SummaryCityMargin {
    @ExcelCell(0)
    @IdentifierField
    private String city;
    @ExcelCell(3)
    private double margin;

    @IdentifierMethod
    public static boolean check(String value) {
        if (value == null) return false;
        return value.length() > 2 && value.length() < 20;
    }
}
