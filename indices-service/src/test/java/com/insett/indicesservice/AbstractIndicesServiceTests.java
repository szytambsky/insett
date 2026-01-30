package com.insett.indicesservice;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import com.insett.indicesservice.exceptions.ResourceLoadException;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractIndicesServiceTests {

    private static final Logger log = LoggerFactory.getLogger(AbstractIndicesServiceTests.class);

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ResourceLoader resourceLoader;

    protected <T> T readResource(String path, TypeReference<T> typeReference) {
        try {
            String classpath = "classpath:" + path;
            File file = this.resourceLoader.getResource(classpath).getFile();
            return mapper.readValue(file, typeReference);
        } catch (IOException e) {
            throw new ResourceLoadException("Could not load resource for type: " + typeReference.getType(), e);
        }
    }

    protected <T> Consumer<T> print() {
        return t -> log.info("{}", t);
    }
}
