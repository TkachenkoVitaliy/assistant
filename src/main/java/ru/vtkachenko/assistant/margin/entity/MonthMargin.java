package ru.vtkachenko.assistant.margin.entity;

import com.poiorm.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RowObject(startRowIndex = 6)
public class MonthMargin {
    @ExcelCell(0)
    @IdentifierField
    private String month;
    @ExcelCell(3)
    private double margin;
    @InnerRowObject
    private List<CityMargin> cities = new ArrayList<>();

    @IdentifierMethod
    public static boolean check(String value) {
        if (value == null) return false;
        List<String> months = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
        String summary = "Итого";
//        return months.contains(value);
        return months.contains(value) || summary.equals(value);
    }
}
