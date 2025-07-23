package io.github.ruby.storage.impl;

import io.github.ruby.storage.AbstractStorage;
import io.github.ruby.util.IConstants;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ResourceStorage extends AbstractStorage<String> {
    public static final ResourceStorage INSTANCE = new ResourceStorage("/assets/" + IConstants.ID + "/");

    private final String basePath;

    public ResourceStorage(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public void init() {
    }

    public InputStream getInputStream(String path) throws FileNotFoundException {
        InputStream stream = getClass().getResourceAsStream(basePath + path);

        if (stream == null) {
            throw new FileNotFoundException("Resource not found: " + path);
        }
        return stream;
    }

    public List<String> get(String directory) throws IOException {
        List<String> files = new ArrayList<>();
        URL dirUrl = getClass().getResource(basePath + directory);
        if (dirUrl == null) return files;

        if (dirUrl.getProtocol().equals("file")) {
            File dir = new File(dirUrl.getPath());
            if (dir.exists() && dir.isDirectory()) {
                for (File file : Objects.requireNonNull(dir.listFiles())) {
                    files.add(file.getName());
                }
            }
        } else if (dirUrl.getProtocol().equals("jar")) {
            String jarPath = dirUrl.getPath().substring(5, dirUrl.getPath().indexOf("!"));
            String normalizedBase = basePath.startsWith("/") ? basePath.substring(1) : basePath;

            try (JarFile jarFile = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8))) {
                Enumeration<JarEntry> entries = jarFile.entries();
                String targetDir = (normalizedBase + directory).replaceAll("^/+", "").replaceAll("/+$", "") + "/";

                Set<String> found = new HashSet<>();
                while (entries.hasMoreElements()) {
                    String entryName = entries.nextElement().getName();
                    if (entryName.startsWith(targetDir)) {
                        String sub = entryName.substring(targetDir.length());
                        if (sub.isBlank()) continue;
                        int slash = sub.indexOf('/');
                        if (slash == -1) {
                            found.add(sub);
                        } else if (slash > 0) {
                            found.add(sub.substring(0, slash));
                        }
                    }
                }

                files.addAll(found);
            }
        }

        return files;
    }
}