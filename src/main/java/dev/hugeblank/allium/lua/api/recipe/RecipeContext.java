package dev.hugeblank.allium.lua.api.recipe;

import dev.hugeblank.allium.lua.type.annotation.CoerceToNative;
import dev.hugeblank.allium.lua.type.annotation.LuaWrapped;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

import java.util.Map;

public abstract class RecipeContext {
    protected final Map<RecipeType<?>, Map<Identifier, RecipeEntry<?>>> recipes;
    protected final Map<Identifier, RecipeEntry<?>> recipesById;

    public RecipeContext(Map<RecipeType<?>, Map<Identifier, RecipeEntry<?>>> recipes, Map<Identifier, RecipeEntry<?>> recipesById) {
        this.recipes = recipes;
        this.recipesById = recipesById;
    }

    @LuaWrapped
    public RecipeEntry<?> getRecipe(Identifier id) {
        return recipesById.get(id);
    }

    @LuaWrapped
    public @CoerceToNative Map<Identifier, RecipeEntry<?>> getRecipesOfType(RecipeType<?> type) {
        return recipes.get(type);
    }
}
