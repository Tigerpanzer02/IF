package com.github.stefvanschie.inventoryframework.util;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.util.Locale;

public class EnchantmentUtil {
    private EnchantmentUtil() {}

    @SuppressWarnings("deprecation")
    public static Enchantment getEnchantment(String name) {
        name = name.toUpperCase(Locale.getDefault());
        if (VersionUtil.CURRENT.getMinor() >= 13) {
            return Enchantment.getByKey(NamespacedKey.minecraft(name));
        } else {
            // noinspection deprecation
            return Enchantment.getByName(name);
        }
    }
}
