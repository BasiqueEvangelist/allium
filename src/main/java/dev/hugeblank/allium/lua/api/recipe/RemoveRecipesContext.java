package dev.hugeblank.allium.lua.api.recipe;

import dev.hugeblank.allium.lua.type.annotation.LuaWrapped;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import org.squiddev.cobalt.LuaError;

import java.util.Map;

@LuaWrapped
public class RemoveRecipesContext extends RecipeContext {
    public RemoveRecipesContext(Map<RecipeType<?>, Map<Identifier, RecipeEntry<?>>> recipes, Map<Identifier, RecipeEntry<?>> recipesById) {
        super(recipes, recipesById);
    }

    @LuaWrapped
    public void removeRecipe(Identifier id) throws LuaError {
        var oldRecipe = recipesById.remove(id);

        if (oldRecipe == null)
            throw new LuaError("recipe '" + id + "' doesn't exist");

        recipes.get(oldRecipe.value().getType()).remove(id);
    }

    @LuaWrapped
    public void removeRecipe(RecipeEntry<?> recipe) throws LuaError {
        removeRecipe(recipe.id());
    }

    public interface Handler {
        void removeRecipes(RemoveRecipesContext ctx);
    }
}
