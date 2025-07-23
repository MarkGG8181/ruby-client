package io.github.ruby.storage.impl;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import io.github.ruby.module.AbstractModule;
import io.github.ruby.module.ModuleInfo;
import io.github.ruby.storage.AbstractStorage;

public class ModuleStorage extends AbstractStorage<AbstractModule> {
    public static final ModuleStorage INSTANCE = new ModuleStorage();

    @Override
    public void init() {
        BUS.register(this);

        try (ScanResult result = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .scan()) {

            ClassInfoList classInfos = result.getClassesWithAnnotation(ModuleInfo.class.getName());

            for (ClassInfo classInfo : classInfos) {
                Class<?> clazz = classInfo.loadClass();

                try {
                    Object instance = clazz.getDeclaredConstructor().newInstance();
                    add((AbstractModule) instance);
                } catch (Exception e) {
                    LOGGER.error("Failed to instantiate {}: {}", clazz.getName(), e);
                }
            }
        }
    }
}