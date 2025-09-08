package ru.boardworld.commons.utils.yaml;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.Properties;

public class CustomConfigPropertiesReaderFactory implements PropertySourceFactory {

    @Override
    public @NonNull PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) throws IOException {
        Resource resource = encodedResource.getResource();
        String filename = resource.getFilename();
        String sourceName = (filename != null) ? filename : name;

        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource);

        try {
            factory.afterPropertiesSet();
        } catch (Exception e) {
            throw new IOException("Failed to load YAML properties from resource: " + resource, e);
        }

        Properties props = factory.getObject();
        if (props == null) {
            throw new IOException("No properties loaded from YAML resource: " + resource);
        }

        return new PropertiesPropertySource((sourceName != null) ? sourceName : "application.yml", props);
    }
}
