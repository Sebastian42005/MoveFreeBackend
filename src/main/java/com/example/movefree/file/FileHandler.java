package com.example.movefree.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
public class FileHandler<T> {

    private static final String PATH = "files/time-tables/";

    public static <T> FileHandler<T> getInstance() {
        return new FileHandler<>();
    }


    private FileHandler() {}

    public T toObject(String fileName, Class<T> clazz) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(PATH + fileName + ".txt"), clazz);
        } catch (IOException e) {
            return null;
        }
    }

    public void writeFile(T object, String toFile) {
        File file = new File(PATH + toFile + ".txt");
        try {
            if (file.createNewFile()) {
                writeFile(object, toFile);
                return;
            }

            FileWriter fileWriter = new FileWriter(file);

            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.writeValueAsString(object);

            fileWriter.write(content);

            fileWriter.close();
        }catch (IOException exception) {
            log.error("IOException: " + exception.getMessage());
        }
    }

}
