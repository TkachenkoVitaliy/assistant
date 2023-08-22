package ru.vtkachenko.assistant.util;

import com.poiorm.mapper.ExcelOrmReader;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@UtilityClass
public class ExcelUtil {

    @SneakyThrows
    public static <T> List<T> createListRows(Path excelFile, Class<T> rootType) {
        try (Workbook wb = WorkbookFactory.create(excelFile.toFile())) {
            Sheet sheet = wb.getSheetAt(0);
            List<T> rowEntities = ExcelOrmReader.fromExcel(sheet, rootType);
            return rowEntities;
        }
    }

    public static File writeWorkbook(File file, Workbook workbook) {
        try (FileOutputStream out = new FileOutputStream(file)) {
            workbook.write(out);
            return file;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Файл не существует", e);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка ввода/вывода при попытке записать книгу", e);
        }
    }
}
