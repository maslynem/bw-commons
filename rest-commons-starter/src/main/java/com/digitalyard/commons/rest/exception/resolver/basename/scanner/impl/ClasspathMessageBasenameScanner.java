package com.digitalyard.commons.rest.exception.resolver.basename.scanner.impl;

import com.digitalyard.commons.rest.exception.resolver.basename.scanner.MessageBasenameScanner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClasspathMessageBasenameScanner implements MessageBasenameScanner {

    private final String pattern = "classpath*:messages/*.properties";

    @Override
    public List<String> scanBasenames() {
        List<String> basenames = new ArrayList<>();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            Resource[] resources = resolver.getResources(pattern);
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                if (filename != null) {
                    String basename = extractBasename(filename);
                    String fullPath = "classpath:messages/" + basename;

                    if (!basenames.contains(fullPath)) {
                        basenames.add(fullPath);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to scan for message files", e);
        }

        return basenames;
    }

    private String extractBasename(String filename) {
        String basename = filename.replace(".properties", "");

        if (basename.contains("_")) {
            String[] parts = basename.split("_");
            String lastPart = parts[parts.length - 1];

            if (isValidLocaleCode(lastPart)) {
                basename = basename.substring(0, basename.lastIndexOf("_"));
            }
        }

        return basename;
    }

    private boolean isValidLocaleCode(String code) {
        return code.length() == 2 || (code.length() == 5 && code.contains("_"));
    }
}
