package io.github.ruby.module.impl.render;

import io.github.ruby.module.AbstractModule;
import io.github.ruby.module.ModuleCategory;
import io.github.ruby.module.ModuleInfo;
import io.github.ruby.module.setting.impl.StringSetting;

@ModuleInfo(name = "Hud", description = "Renders the heads-up-display", category = ModuleCategory.RENDER)
public class HudModule extends AbstractModule {
    private final StringSetting design = StringSetting.builder()
            .name("Design")
            .description("Choose the HUD design style.")
            .value("Simple")
            .otherValues("Text", "Image")
            .build();
}