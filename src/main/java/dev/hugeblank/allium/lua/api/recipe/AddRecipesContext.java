package dev.hugeblank.allium.lua.api.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import dev.hugeblank.allium.lua.api.JsonLib;
import dev.hugeblank.allium.lua.type.annotation.LuaWrapped;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.squiddev.cobalt.LuaError;
import org.squiddev.cobalt.LuaValue;

import java.util.HashMap;
import java.util.Map;

@LuaWrapped
public class AddRecipesContext extends RecipeContext {
    public AddRecipesContext(Map<RecipeType<?>, Map<Identifier, RecipeEntry<?>>> recipes, Map<Identifier, RecipeEntry<?>> recipesById) {
        super(recipes, recipesById);
    }

    @LuaWrapped
    public void addRecipe(Identifier id, String json) throws LuaError {
        addRecipe(id, JsonParser.parseString(json).getAsJsonObject());
    }

    @LuaWrapped
    public void addRecipe(Identifier id, JsonObject el) throws LuaError {
        var recipe = Util.getResult(Recipe.CODEC.parse(JsonOps.INSTANCE, el), JsonParseException::new);
        addRecipe(new RecipeEntry<Recipe<?>>(id, recipe));
    }

    @LuaWrapped
    public void addRecipe(Identifier id, LuaValue val) throws LuaError {
        addRecipe(id, JsonLib.toJsonElement(val).getAsJsonObject());
    }

    @LuaWrapped
    public void addRecipe(RecipeEntry<?> entry) throws LuaError {
        if (recipesById.put(entry.id(), entry) != null) {
            throw new LuaError("recipe '" + entry.id() + "' already exists");
        }

         recipes.computeIfAbsent(entry.value().getType(), unused -> new HashMap<>()).put(entry.id(), entry);
    }

    public interface Handler {
        void addRecipes(AddRecipesContext ctx);
    }
}
