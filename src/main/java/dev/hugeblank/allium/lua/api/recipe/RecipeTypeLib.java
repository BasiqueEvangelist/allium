package dev.hugeblank.allium.lua.api.recipe;

import dev.hugeblank.allium.lua.type.annotation.LuaIndex;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.squiddev.cobalt.LuaError;

public class RecipeTypeLib {
    @LuaIndex
    public RecipeType<?> index(String type) throws LuaError {
        Identifier id = new Identifier(type);

        return Registries.RECIPE_TYPE.getOrEmpty(id).orElse(null);
    }
}
