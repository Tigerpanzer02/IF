package com.github.stefvanschie.inventoryframework.core.gui.type;

import com.github.stefvanschie.inventoryframework.core.gui.AbstractInventoryComponent;
import com.github.stefvanschie.inventoryframework.core.gui.type.util.AbstractNamedGui;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a gui in the form of a smithing table
 *
 * @since 1.0.0
 */
public interface AbstractSmithingTableGui extends AbstractNamedGui {

    /**
     * Gets the inventory component representing the first item
     *
     * @return the first item component
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    AbstractInventoryComponent getFirstItemComponent();

    /**
     * Gets the inventory component representing the second item
     *
     * @return the second item component
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    AbstractInventoryComponent getSecondItemComponent();

    /**
     * Gets the inventory component representing the result
     *
     * @return the result component
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    AbstractInventoryComponent getResultComponent();

    /**
     * Gets the inventory component representing the player inventory
     *
     * @return the player inventory component
     * @since 1.0.0
     */
    @NotNull
    @Contract(pure = true)
    AbstractInventoryComponent getPlayerInventoryComponent();
}