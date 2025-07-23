package io.github.ruby.module.impl.render;

import com.google.common.eventbus.Subscribe;
import io.github.ruby.event.impl.Render2DEvent;
import io.github.ruby.module.AbstractModule;
import io.github.ruby.module.ModuleCategory;
import io.github.ruby.module.ModuleInfo;
import io.github.ruby.module.setting.impl.StringSetting;
import io.github.ruby.storage.impl.FontStorage;
import io.github.ruby.util.IConstants;
import io.github.ruby.util.game.font.impl.CustomFontRenderer;
import io.github.ruby.util.game.shader.ShaderUtil;

@ModuleInfo(name = "Hud", description = "Renders the heads-up-display", category = ModuleCategory.RENDER)
public class HudModule extends AbstractModule {
    private final CustomFontRenderer font = FontStorage.INSTANCE.getFont("Montserrat-Medium", 30);

    private final StringSetting design = StringSetting.builder()
            .name("Design")
            .description("Choose the HUD design style.")
            .value("Image")
            .otherValues("Text", "Image")
            .build();

    @Subscribe
    public void onRender(Render2DEvent event) {
        switch (design.getValue()) {
            case "Simple" -> font.drawStringWithShadow(IConstants.NAME, 5, 5, -1);
            case "Text" -> mc.fontRendererObj.drawStringWithShadow(IConstants.NAME, 5, 5, -1);
            case "Image" -> ShaderUtil.image("icons8-ruby-100.png", 5, 5, 30, 30, 0);
        }
    }
}