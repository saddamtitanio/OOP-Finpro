package com.game.backend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class JsonFileUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> List<T> readList(String path, Class<T[]> clazz) {
        try {
            File file = new File(path);
            if (!file.exists()) return List.of();
            return Arrays.asList(mapper.readValue(file, clazz));
        } catch (Exception e) {
            return List.of();
        }
    }

    public static void write(String path, Object data) {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(path), data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
