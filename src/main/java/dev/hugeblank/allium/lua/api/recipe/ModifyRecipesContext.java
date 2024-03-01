package dev.hugeblank.allium.lua.api.recipe;

import dev.hugeblank.allium.lua.type.annotation.CoerceToNative;
import dev.hugeblank.allium.lua.type.annotation.LuaWrapped;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import org.squiddev.cobalt.LuaError;

import java.util.HashMap;
import java.util.Map;

@LuaWrapped
public class ModifyRecipesContext extends RecipeContext {
    public ModifyRecipesContext(Map<RecipeType<?>, Map<Identifier, RecipeEntry<?>>> recipes, Map<Identifier, RecipeEntry<?>> recipesById) {
        super(recipes, recipesById);
    }

    @LuaWrapped
    public void replaceRecipe(Identifier id, RecipeEntry<?> newRecipe) throws LuaError {
        var oldRecipe = recipesById.put(id, newRecipe);

        if (oldRecipe == null)
            throw new LuaError("recipe '" + id + "' doesn't exist");
        else if (oldRecipe.value().getType() != newRecipe.value().getType())
            throw new LuaError("old recipe and new recipe's types don't match");

        recipes.computeIfAbsent(newRecipe.value().getType(), unused -> new HashMap<>()).put(id, newRecipe);
    }

    public interface Handler {
        void modifyRecipes(ModifyRecipesContext ctx);
    }
}
