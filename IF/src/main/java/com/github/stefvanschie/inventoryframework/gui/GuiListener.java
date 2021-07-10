package com.github.stefvanschie.inventoryframework.gui;

import com.github.stefvanschie.inventoryframework.gui.type.*;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.util.VersionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Listens to events for {@link Gui}s. Only one instance of this class gets constructed.
 * (One instance per plugin, but plugins are supposed to shade and relocate IF.)
 *
 * @since 0.5.4
 */
public class GuiListener implements Listener {

    /**
     * A collection of all {@link Gui} instances that have at least one viewer.
     */
    @NotNull
    private final Set<Gui> activeGuiInstances = new HashSet<>();

    /**
     * Handles clicks in inventories
     *
     * @param event the event fired
     * @since 0.5.4
     */
    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        Gui gui = getGui(event.getInventory());

        if (gui == null) {
            return;
        }

        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
            gui.callOnOutsideClick(event);
            return;
        }

        int rawSlot = event.getRawSlot();
        int eventSlot = event.getSlot();

        gui.callOnGlobalClick(event);
        if (rawSlot == eventSlot) {
            gui.callOnTopClick(event);
        } else {
            gui.callOnBottomClick(event);
        }

        gui.click(event);
    }

    /**
     * Resets the items into the correct positions for anvil guis
     *
     * @param event the event fired
     * @since 0.8.0
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void resetItemsAnvil(@NotNull InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();

        if (!(holder instanceof AnvilGui) || !(event.getWhoClicked() instanceof Player)) {
            return;
        }

        ((AnvilGui) holder).handleClickEvent(event);
    }

    /**
     * Resets the items into the correct positions for beacon guis
     *
     * @param event the event fired
     * @since 0.8.0
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void resetItemsBeacon(@NotNull InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();

        if (!(holder instanceof BeaconGui) || !(event.getWhoClicked() instanceof Player)) {
            return;
        }

        ((BeaconGui) holder).handleClickEvent(event);
    }

    /**
     * Resets the items into the correct positions for cartography table guis
     *
     * @param event the event fired
     * @since 0.8.0
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void resetItemsCartographyTable(@NotNull InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();

        if (!(holder instanceof CartographyTableGui) || !(event.getWhoClicked() instanceof Player)) {
            return;
        }

        ((CartographyTableGui) holder).handleClickEvent(event);
    }

    /**
     * Resets the items into the correct positions for enchanting table guis
     *
     * @param event the event fired
     * @since 0.8.0
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void resetItemsEnchantingTable(@NotNull InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();

        if (!(holder instanceof EnchantingTableGui) || !(event.getWhoClicked() instanceof Player)) {
            return;
        }

        ((EnchantingTableGui) holder).handleClickEvent(event);
    }

    /**
     * Resets the items into the correct positions for grindstone guis
     *
     * @param event the event fired
     * @since 0.8.0
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void resetItemsGrindstone(@NotNull InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();

        if (!(holder instanceof GrindstoneGui) || !(event.getWhoClicked() instanceof Player)) {
            return;
        }

        ((GrindstoneGui) holder).handleClickEvent(event);
    }

    /**
     * Resets the items into the correct positions for stonecutter guis
     *
     * @param event the event fired
     * @since 0.8.0
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void resetItemsStonecutter(@NotNull InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();

        if (!(holder instanceof StonecutterGui) || !(event.getWhoClicked() instanceof Player)) {
            return;
        }

        ((StonecutterGui) holder).handleClickEvent(event);
    }

    /**
     * Resets the items into the correct positions for smithing table guis
     *
     * @param event the event fired
     * @since 0.8.0
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void resetItemsSmithingTable(@NotNull InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();

        if (!(holder instanceof SmithingTableGui) || !(event.getWhoClicked() instanceof Player)) {
            return;
        }

        ((SmithingTableGui) holder).handleClickEvent(event);
    }

    /**
     * Handles drag events
     *
     * @param event the event fired
     * @since 0.6.1
     */
    @EventHandler
    public void onInventoryDrag(@NotNull InventoryDragEvent event) {
        Gui gui = getGui(event.getInventory());

        if (gui == null) {
            return;
        }

        InventoryView view = event.getView();
        Set<Integer> inventorySlots = event.getInventorySlots();

        if (inventorySlots.size() > 1) {
            boolean top = false, bottom = false;

            for (int inventorySlot : event.getRawSlots()) {
                int convertedSlot = view.convertSlot(inventorySlot);

                if (inventorySlot == convertedSlot) {
                    top = true;
                } else {
                    bottom = true;
                }

                if (top && bottom) {
                    break;
                }
            }

            gui.callOnGlobalDrag(event);

            if (top) {
                gui.callOnTopDrag(event);
            }

            if (bottom) {
                gui.callOnBottomDrag(event);
            }
        } else {
            if (VersionUtil.CURRENT.getMinor() < 13) {
                event.setCancelled(true);
                return;
            }
            int index = inventorySlots.toArray(new Integer[0])[0];
            InventoryType.SlotType slotType = view.getSlotType(index);

            boolean even = event.getType() == DragType.EVEN;

            ClickType clickType = even ? ClickType.LEFT : ClickType.RIGHT;
            InventoryAction inventoryAction = even ? InventoryAction.PLACE_SOME : InventoryAction.PLACE_ONE;

            //this is a fake click event, firing this may cause other plugins to function incorrectly, so keep it local
            InventoryClickEvent inventoryClickEvent = new InventoryClickEvent(view, slotType, index, clickType,
                    inventoryAction);

            onInventoryClick(inventoryClickEvent);

            event.setCancelled(inventoryClickEvent.isCancelled());
        }
    }

    /**
     * Handles closing in inventories
     *
     * @param event the event fired
     * @since 0.5.4
     */
    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(@NotNull InventoryCloseEvent event) {
        Gui gui = getGui(event.getInventory());

        if (gui == null) {
            return;
        }

        HumanEntity humanEntity = event.getPlayer();
        PlayerInventory playerInventory = humanEntity.getInventory();

        //due to a client issue off-hand items appear as ghost items, this updates the off-hand correctly client-side
        playerInventory.setItemInOffHand(playerInventory.getItemInOffHand());

        if (!gui.isUpdating()) {//this is a hack to remove items correctly when players press the x button in a beacon
            Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(getClass()), () -> {
                gui.callOnClose(event);

                if (humanEntity.getOpenInventory().getTopInventory() instanceof PlayerInventory) {
                    humanEntity.closeInventory();
                }
            });
        }

        //delay because merchants put items in slots back in the player inventory
        Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(getClass()), () -> {
            gui.getHumanEntityCache().restoreAndForget(humanEntity);

            if (gui.getViewerCount() == 1) {
                activeGuiInstances.remove(gui);
            }

            if (gui instanceof MerchantGui) {
                ((MerchantGui) gui).handleClose(humanEntity);
            }
        });
    }

    /**
     * Registers newly opened inventories
     *
     * @param event the event fired
     * @since 0.5.19
     */
    @EventHandler(ignoreCancelled = true)
    public void onInventoryOpen(@NotNull InventoryOpenEvent event) {
        Gui gui = getGui(event.getInventory());

        if (gui == null) {
            return;
        }

        activeGuiInstances.add(gui);
    }

    /**
     * Handles the disabling of the plugin
     *
     * @param event the event fired
     * @since 0.5.19
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPluginDisable(@NotNull PluginDisableEvent event) {
        Plugin thisPlugin = JavaPlugin.getProvidingPlugin(getClass());
        if (event.getPlugin() != thisPlugin) {
            return;
        }

        int counter = 0; //callbacks might open GUIs, eg. in nested menus
		int maxCount = 10;
        while (!activeGuiInstances.isEmpty() && counter++ < maxCount) {
            for (Gui gui : new ArrayList<>(activeGuiInstances)) {
                for (HumanEntity viewer : gui.getViewers()) {
                    viewer.closeInventory();
                }
            }
        }

        if (counter == maxCount) {
			thisPlugin.getLogger().warning("Unable to close GUIs on plugin disable: they keep getting opened "
					+ "(tried: " + maxCount + " times)");
		}
    }

    /**
     * Gets the gui from the inventory or null if the inventory isn't a gui
     *
     * @param inventory the inventory to get the gui from
     * @return the gui or null if the inventory doesn't have a gui
     * @since 0.8.1
     */
    @Nullable
    @Contract(pure = true)
    public static Gui getGui(@NotNull Inventory inventory) {
        Gui gui = Gui.getGui(inventory);

        if (gui != null) {
            return gui;
        }

        InventoryHolder holder = inventory.getHolder();

        if (holder instanceof Gui) {
            return (Gui) holder;
        }

        return null;
    }
}
