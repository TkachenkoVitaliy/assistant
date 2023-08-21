package ru.vtkachenko.assistant.margin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.vtkachenko.assistant.util.ExcelUtil;
import ru.vtkachenko.assistant.filestorage.service.FileStorageService;
import ru.vtkachenko.assistant.margin.entity.MarginReportRow;
import ru.vtkachenko.assistant.margin.entity.MonthMargin;
import ru.vtkachenko.assistant.margin.entity.SummaryCityMargin;
import ru.vtkachenko.assistant.util.OrderUtil;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarginService {

    private final FileStorageService fileStorageService;

    private final static String MARGIN_FOLDER_NAME = "Margin";

    public Resource createMarginReport(
            MultipartFile transitMonthly,
            MultipartFile stockMonthly,
            MultipartFile summaryMonthly,
            MultipartFile transitAnnually,
            MultipartFile stockAnnually,
            MultipartFile summaryAnnually
    ) {
        // Удаляем предыдущий файл отчета
        fileStorageService.removeTempSubfolder(MARGIN_FOLDER_NAME);
        // Получаем файлы из контроллера
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

        List<MonthMargin> marginTransitMonthly = ExcelUtil.createListRows(transitMonthlyPath, MonthMargin.class);
        List<SummaryCityMargin> marginTransitAnnually = ExcelUtil.createListRows(transitAnnuallyPath,
                SummaryCityMargin.class);

        List<MarginReportRow> reportRowsTransit = marginTransitAnnually.stream()
                .map(SummaryCityMargin::getCity)
                .sorted(Comparator.comparingInt(OrderUtil::getIndexNumber))
                .map(label -> {
                    List<Double> valuesCity = marginTransitMonthly.stream()
                            .map(
                                    monthMargin -> monthMargin.getCities().stream()
                                            .filter(cityMargin -> cityMargin.getCity().equals(label))
                                            .findAny()
                            )
                            .map(
                                    optionalCityMargin -> optionalCityMargin.isPresent() ?
                                            optionalCityMargin.get().getMargin() : null
                            )
                            .collect(Collectors.toList());

                    return MarginReportRow.builder()
                            .label(label)
                            .values(valuesCity)
                            .build();
                })
                .toList();

        marginTransitAnnually.forEach(
                mta -> reportRowsTransit.stream()
                        .filter(row -> row.getLabel().equals(mta.getCity()))
                        .findFirst()
                        .ifPresent(cityRow -> cityRow.getValues().add(mta.getMargin()))
        );


        System.out.println("REPORT ROWS TRANSIT");
        reportRowsTransit.forEach(System.out::println);


        List<MonthMargin> marginStockMonthly = ExcelUtil.createListRows(stockMonthlyPath, MonthMargin.class);
        List<SummaryCityMargin> marginStockAnnually = ExcelUtil.createListRows(stockAnnuallyPath,
                SummaryCityMargin.class);

        List<MonthMargin> marginSummaryMonthly = ExcelUtil.createListRows(summaryMonthlyPath, MonthMargin.class);
        List<SummaryCityMargin> marginSummaryAnnually = ExcelUtil.createListRows(summaryAnnuallyPath,
                SummaryCityMargin.class);



        return null;
    }
    // Обрабатываем файлы
    // Создаем файл отчета
    // Удаляем файлы полученные из контроллера
    // Возвращаем файл отчета
}
