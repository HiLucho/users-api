package com.luis.project.cl.users;

import lombok.experimental.UtilityClass;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@UtilityClass
public class JsonUtils {

    public String loadJsonPayload(String fileName) throws IOException {
        File file = ResourceUtils.getFile("classpath:" + fileName);
        return Files.readString(file.toPath(), StandardCharsets.UTF_8);
    }

}
