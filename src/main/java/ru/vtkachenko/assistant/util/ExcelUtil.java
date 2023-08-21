package ru.vtkachenko.assistant.util;

import com.poiorm.mapper.ExcelOrm;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.nio.file.Path;
import java.util.List;

@UtilityClass
public class ExcelUtil {

    @SneakyThrows
    public static <T> List<T> createListRows(Path excelFile, Class<T> rootType) {
        try (Workbook wb = WorkbookFactory.create(excelFile.toFile())) {
            Sheet sheet = wb.getSheetAt(0);
            List<T> rowEntities = ExcelOrm.fromExcel(sheet, rootType);
            return rowEntities;
        }
    }
}
