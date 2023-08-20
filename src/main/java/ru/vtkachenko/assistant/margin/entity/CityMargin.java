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
@RowObject(parent = MonthMargin.class)
public class CityMargin {
    @ExcelCell(0)
    @IdentifierField
    private String city;
    @ExcelCell(3)
    private double margin;

    @IdentifierMethod
    public static boolean check(String value) {
        if (value == null) return false;
        List<String> months = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
        String summary = "Итого";
        return !months.contains(value) && !summary.equals(value);
    }
}
