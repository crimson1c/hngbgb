package com.petwhistle;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public class PetWhistle implements ModInitializer {
    public static final Item WHISTLE = Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath("petwhistle", "whistle"), new WhistleItem(new Item.Properties().durability(128)));
    @Override public void onInitialize() { Config.load(); ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> entries.accept(WHISTLE)); }
}
