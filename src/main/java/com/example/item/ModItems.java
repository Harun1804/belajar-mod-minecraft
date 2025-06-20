package com.example.item;

import com.example.TutorialMod;
import com.example.item.custom.ChiselItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item PINK_GARNET = registerItem("pink_garnet", new Item(new Item.Settings()));
    public static final Item RAW_PINK_GARNET = registerItem("raw_pink_garnet", new Item(new Item.Settings()));
    public static final Item CHISEL = registerItem("chisel", new ChiselItem(new Item.Settings().maxDamage(32)));
    public static final Item CAULIFLOWER = registerItem("cauliflower", new Item(new Item.Settings().food(ModFoodComponents.CAULIFLOWER)));
    public static final Item STARLIGHT_ASHES = registerItem("starlight_ashes", new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        // This method would typically register the item with the game's registry.
        // For example, you might use Registry.register(Registry.ITEM, new Identifier(TutorialMod.MOD_ID, name), item);
        // Here we just log the registration for demonstration purposes.
        TutorialMod.LOGGER.info("Registering item: " + name);
        return Registry.register(Registries.ITEM, Identifier.of(TutorialMod.MOD_ID, name), item);
    }

    public static void registerItems() {
        // Register your items here
        // Example: Registry.register(Registry.ITEM, new Identifier("modid", "item_name"), new Item(new Item.Settings()));
        TutorialMod.LOGGER.info("Registering items for " + TutorialMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entire -> {
            entire.add(PINK_GARNET);
            entire.add(RAW_PINK_GARNET);
        });
    }
}
