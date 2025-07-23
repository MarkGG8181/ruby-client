package io.github.ruby.storage.impl;

import com.google.common.eventbus.Subscribe;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import io.github.ruby.event.impl.KeyboardInputEvent;
import io.github.ruby.module.AbstractModule;
import io.github.ruby.module.ModuleCategory;
import io.github.ruby.module.ModuleInfo;
import io.github.ruby.storage.AbstractStorage;

import java.util.List;

public class ModuleStorage extends AbstractStorage<AbstractModule> {
    public static final ModuleStorage INSTANCE = new ModuleStorage();

    public AbstractModule lastModule;

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

    public List<AbstractModule> getByCategory(ModuleCategory category) {
        return list.stream().filter(m -> m.getCategory().equals(category)).toList();
    }

    @Subscribe
    public void onKey(KeyboardInputEvent event) {
        list.forEach(mod -> {
            if (mod.key == event.key) {
                mod.setEnabled(!mod.isEnabled());
            }
        });
    }
}