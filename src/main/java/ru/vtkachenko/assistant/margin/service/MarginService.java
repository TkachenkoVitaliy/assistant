package ru.vtkachenko.assistant.margin.service;

import com.poiorm.mapper.ExcelOrmWriter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.apache.poi.ss.formula.functions.Rows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.vtkachenko.assistant.margin.entity.*;
import ru.vtkachenko.assistant.util.ExcelUtil;
import ru.vtkachenko.assistant.filestorage.service.FileStorageService;
import ru.vtkachenko.assistant.util.OrderUtil;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarginService {

    private final FileStorageService fileStorageService;

    private final static String MARGIN_FOLDER_NAME = "Margin";
    private final static String MARGIN_REPORT_FILE_NAME = "Margin.xlsx";

    private final static String SUMMARY_TOTAL = "Общий итог";

    public File createMarginReport(
            MultipartFile transitMonthly,
            MultipartFile stockMonthly,
            MultipartFile summaryMonthly,
            MultipartFile transitAnnually,
            MultipartFile stockAnnually,
            MultipartFile summaryAnnually
    ) {
        // Удаляем предыдущий файл отчета
        fileStorageService.removeTempSubfolder(MARGIN_FOLDER_NAME);
        // Получаем файлы из контроллера.
        // Сохраняем файлы
        Path transitMonthlyPath = fileStorageService.storeFile(MARGIN_FOLDER_NAME, transitMonthly,
                "transit_monthly.xlsx");
        Path stockMonthlyPath = fileStorageService.storeFile(MARGIN_FOLDER_NAME, stockMonthly,
                "stock_monthly.xlsx");
        Path summaryMonthlyPath = fileStorageService.storeFile(MARGIN_FOLDER_NAME, summaryMonthly,
                "summary_monthly.xlsx");
        Path transitAnnuallyPath = fileStorageService.storeFile(MARGIN_FOLDER_NAME, transitAnnually,
                "transit_annually.xlsx");
        Path stockAnnuallyPath = fileStorageService.storeFile(MARGIN_FOLDER_NAME, stockAnnually,
                "stock_annually.xlsx");
        Path summaryAnnuallyPath = fileStorageService.storeFile(MARGIN_FOLDER_NAME, summaryAnnually,
                "summary_annually.xlsx");


        // TRANSIT
        List<MonthMargin> marginTransitMonthly = ExcelUtil.createListRows(transitMonthlyPath, MonthMargin.class);
        List<SummaryCityMargin> marginTransitAnnually = ExcelUtil.createListRows(transitAnnuallyPath,
                SummaryCityMargin.class);

        // STOCK
        List<MonthMargin> marginStockMonthly = ExcelUtil.createListRows(stockMonthlyPath, MonthMargin.class);
        List<SummaryCityMargin> marginStockAnnually = ExcelUtil.createListRows(stockAnnuallyPath,
                SummaryCityMargin.class);

        // SUMMARY
        List<MonthMargin> marginSummaryMonthly = ExcelUtil.createListRows(summaryMonthlyPath, MonthMargin.class);
        List<SummaryCityMargin> marginSummaryAnnually = ExcelUtil.createListRows(summaryAnnuallyPath,
                SummaryCityMargin.class);


        //PRE GET ALL CITIES NAMES
        List<String> rowNames = marginSummaryAnnually.stream()
                .map(SummaryCityMargin::getCity)
                .sorted(Comparator.comparingInt(OrderUtil::getIndexNumber))
                .toList();


        // TRANSIT STEP 2
        List<MonthColumn> transitMonthColumns = createMonthColumns(rowNames,
                marginTransitMonthly, marginTransitAnnually);

        // STOCK STEP 2
        List<MonthColumn> stockMonthColumns = createMonthColumns(rowNames,
                marginStockMonthly, marginStockAnnually);

        // SUMMARY STEP 2
        List<MonthColumn> summaryMonthColumns = createMonthColumns(rowNames,
                marginSummaryMonthly, marginSummaryAnnually);

        // SOUT для дебага
        rowNames.forEach(System.out::println);

        transitMonthColumns.forEach(System.out::println);
        stockMonthColumns.forEach(System.out::println);
        summaryMonthColumns.forEach(System.out::println);

        // ЗАПИСЬ в книгу
        XSSFWorkbook resultWorkbook = new XSSFWorkbook();
        XSSFSheet reportSheet = resultWorkbook.createSheet("Отчет");
        reportSheet.setDisplayZeros(false);
        File resultFile = new File(
                fileStorageService.getTempDirectory().resolve(MARGIN_FOLDER_NAME).resolve(MARGIN_REPORT_FILE_NAME).toUri()
        );

        // Transit
        List<LabelColumn> transitLabelColumns = List.of(new LabelColumn("2023 Транзит", rowNames));
        ExcelOrmWriter.toExcel(reportSheet, transitLabelColumns, 0, 0);
        Cell lastCell = ExcelOrmWriter.toExcel(reportSheet, transitMonthColumns, 0, 1);

        // Stock
        List<LabelColumn> stockLabelColumn = List.of(new LabelColumn("2023 Склад", rowNames));
        ExcelOrmWriter.toExcel(reportSheet, stockLabelColumn, lastCell.getRowIndex() + 2, 0);
        lastCell = ExcelOrmWriter.toExcel(reportSheet, stockMonthColumns, lastCell.getRowIndex() + 2, 1);

        // Summary
        List<LabelColumn> summaryLabelColumn = List.of(new LabelColumn("2023 Склад + транзит", rowNames));
        ExcelOrmWriter.toExcel(reportSheet, summaryLabelColumn, lastCell.getRowIndex() + 2, 0);
        lastCell = ExcelOrmWriter.toExcel(reportSheet, summaryMonthColumns, lastCell.getRowIndex() + 2, 1);

        System.out.println(
                String.format("Last cell - row index [%s] column index [%s]", lastCell.getRowIndex(), lastCell.getColumnIndex())
        );

        return ExcelUtil.writeWorkbook(resultFile, resultWorkbook);

    }
    // Обрабатываем файлы
    // Создаем файл отчета
    // Удаляем файлы полученные из контроллера
    // Возвращаем файл отчета


    public List<MonthColumn> createMonthColumns (
            List<String> rowNames,
            List<MonthMargin> monthlyData, List<SummaryCityMargin> annuallyData
    ) {
        List<String> columnsNames = createColumnNamesList(monthlyData);

        List<MonthColumn> monthColumns = new ArrayList<>();

        for (int i = 0; i < columnsNames.size(); i++) {
            String columnName = columnsNames.get(i);
            if (columnName.equalsIgnoreCase(SUMMARY_TOTAL)) {
                // Для общего итого
                Map<String, Double> citiesMargin = annuallyData.stream()
                        .collect(Collectors.toMap(SummaryCityMargin::getCity, SummaryCityMargin::getMargin));

                MonthColumn monthColumn = new MonthColumn();
                monthColumn.setColumnName(SUMMARY_TOTAL);

                rowNames.forEach(rowName -> {
                    Double value = citiesMargin.getOrDefault(rowName, null);
                    monthColumn.getValues().add(value);
                });

                monthColumns.add(monthColumn);

            } else {
                MonthMargin monthMargin = monthlyData.stream()
                        .filter(monthMarginItem -> monthMarginItem.getMonth().equals(columnName))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Не удалось найти месяц" + columnName));

                Map<String, Double> citiesMargin = monthMargin.getCities()
                        .stream()
                        .collect(Collectors.toMap(CityMargin::getCity, CityMargin::getMargin));
                citiesMargin.put("Итого", monthMargin.getMargin());

                MonthColumn monthColumn = new MonthColumn();
                monthColumn.setColumnName(monthMargin.getMonth());

                rowNames.forEach(rowName -> {
                    Double value = citiesMargin.getOrDefault(rowName, null);
                    monthColumn.getValues().add(value);
                });

                monthColumns.add(monthColumn);
            }
        }
        return monthColumns;
    }

    public List<String> createColumnNamesList(List<MonthMargin> monthlyData) {
        List<String> transitColumns = monthlyData.stream()
                .map(MonthMargin::getMonth)
                .filter(item -> !item.equalsIgnoreCase("итого"))
                .sorted(Comparator.comparingInt(Integer::parseInt))
                .collect(Collectors.toList());
        transitColumns.add(SUMMARY_TOTAL);
        return transitColumns;
    }
}
