package dev.hugeblank.allium.loader.resources;

import net.minecraft.resource.*;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class AlliumResourcePackProvider implements ResourcePackProvider {
    @Override
    public void register(Consumer<ResourcePackProfile> profileAdder) {
        AlliumResourcePack pack = AlliumResourcePack.create("Allium Generated");
        profileAdder.accept(ResourcePackProfile.create(
            "allium_generated",
            Text.literal("Allium Resources"),
            true,
            new ResourcePackProfile.PackFactory() {
                @Override
                public ResourcePack open(String name) {
                    return pack;
                }

                @Override
                public ResourcePack openWithOverlays(String name, ResourcePackProfile.Metadata metadata) {
                    return pack;
                }
            },
            // TODO: make this work on both res types
            ResourceType.CLIENT_RESOURCES,
            ResourcePackProfile.InsertionPosition.TOP,
            ResourcePackSource.BUILTIN
        ));
    }
}