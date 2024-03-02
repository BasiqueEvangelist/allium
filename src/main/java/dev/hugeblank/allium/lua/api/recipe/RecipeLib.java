package dev.hugeblank.allium.lua.api.recipe;

import dev.hugeblank.allium.lua.api.WrappedLuaLibrary;
import dev.hugeblank.allium.lua.type.annotation.CoerceToBound;
import dev.hugeblank.allium.lua.event.SimpleEventType;
import dev.hugeblank.allium.lua.type.annotation.LuaWrapped;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import org.squiddev.cobalt.LuaError;

import java.util.Map;

/**
 * Library for modifying, adding and removing recipes with Lua.
 */
@LuaWrapped(name = "recipe")
public class RecipeLib implements WrappedLuaLibrary {
    private static boolean IN_MODIFY_PHASE = false;

    /**
     * Event for adding new recipes.
     */
    @LuaWrapped
    public static final SimpleEventType<AddRecipesContext.Handler> ADD = new SimpleEventType<>(new Identifier("allium:recipe/adding_recipes"));

    /**
     * Event for modifying and replacing recipes.
     *
     * <p>Setting fields of recipes is only allowed in this phase.
     */
    @LuaWrapped
    public static final SimpleEventType<ModifyRecipesContext.Handler> MODIFY = new SimpleEventType<>(new Identifier("allium:recipe/modifying_recipes"));

    /**
     * Event for removing recipes.
     */
    @LuaWrapped
    public static final SimpleEventType<RemoveRecipesContext.Handler> REMOVE = new SimpleEventType<>(new Identifier("allium:recipe/remove_recipes"));

    /**
     * All registered recipe types
     */
    @LuaWrapped(name = "types")
    public static final @CoerceToBound RecipeTypeLib TYPES = new RecipeTypeLib();

    public static void assertInModifyPhase() throws LuaError {
        if (!IN_MODIFY_PHASE) {
            throw new LuaError("tried to modify recipe not in modify phase");
        }
    }

    public static void runRecipeEvents(Map<RecipeType<?>, Map<Identifier, RecipeEntry<?>>> recipes, Map<Identifier, RecipeEntry<?>> recipesById) {
        AddRecipesContext addCtx = new AddRecipesContext(recipes, recipesById);

        ADD.invoker().addRecipes(addCtx);

        ModifyRecipesContext modifyCtx = new ModifyRecipesContext(recipes, recipesById);

        IN_MODIFY_PHASE = true;
        MODIFY.invoker().modifyRecipes(modifyCtx);
        IN_MODIFY_PHASE = false;

        RemoveRecipesContext removeCtx = new RemoveRecipesContext(recipes, recipesById);

        REMOVE.invoker().removeRecipes(removeCtx);
    }
}
