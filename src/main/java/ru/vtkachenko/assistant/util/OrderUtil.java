package ru.vtkachenko.assistant.util;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@UtilityClass
public class OrderUtil {
    private static final Map<String, Integer> indexMap = new HashMap<>();

    static {
        indexMap.put("абакан", indexMap.size());
        indexMap.put("чита", indexMap.size());
        indexMap.put("новокузнецк", indexMap.size());
        indexMap.put("новосибирск", indexMap.size());
        indexMap.put("омск", indexMap.size());
        indexMap.put("екатеринбург", indexMap.size());
        indexMap.put("пермь", indexMap.size());
        indexMap.put("тюмень", indexMap.size());
        indexMap.put("челябинск", indexMap.size());
        indexMap.put("ижевск", indexMap.size());
        indexMap.put("казань", indexMap.size());
        indexMap.put("набережные челны", indexMap.size());
        indexMap.put("нижний новгород", indexMap.size());
        indexMap.put("уфа", indexMap.size());
        indexMap.put("краснодар", indexMap.size());
        indexMap.put("минеральные воды", indexMap.size());
        indexMap.put("ростов-на-дону", indexMap.size());
        indexMap.put("москва", indexMap.size());
        indexMap.put("санкт-петербург", indexMap.size());
        indexMap.put("итого", indexMap.size());
    }

    public static int getIndexNumber(String value) {
        String normalizedValue = value.toLowerCase();
        if (!indexMap.containsKey(normalizedValue)) {
            throw new RuntimeException(
                    String.format("Не удалось найти - [%s] в словаре индексов", value)
            );
        }
        return indexMap.get(normalizedValue);
    }
}
