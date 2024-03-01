package dev.hugeblank.allium.mixin.recipe;

import dev.hugeblank.allium.lua.api.recipe.RecipeLib;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.*;
import org.squiddev.cobalt.LuaError;

@Mixin(AbstractCookingRecipe.class)
public class AbstractCookingRecipeMixin {
    @Shadow @Mutable @Final protected String group;

    @Shadow @Mutable @Final protected Ingredient ingredient;

    @Shadow @Mutable @Final protected ItemStack result;

    @Shadow @Mutable @Final protected float experience;

    @Shadow @Mutable @Final protected int cookingTime;

    public Ingredient allium$getIngredient() {
        return ingredient;
    }

    public void allium$setGroup(String group) throws LuaError {
        RecipeLib.assertInModifyPhase();

        this.group = group;
    }

    public void allium$setIngredient(Ingredient ingredient) throws LuaError {
        RecipeLib.assertInModifyPhase();

        this.ingredient = ingredient;
    }

    public void allium$setResult(ItemStack result) throws LuaError {
        RecipeLib.assertInModifyPhase();

        this.result = result;
    }

    public void allium$setExperience(float experience) throws LuaError {
        RecipeLib.assertInModifyPhase();

        this.experience = experience;
    }

    public void allium$setCookingTime(int cookingTime) throws LuaError {
        RecipeLib.assertInModifyPhase();

        this.cookingTime = cookingTime;
    }
}
