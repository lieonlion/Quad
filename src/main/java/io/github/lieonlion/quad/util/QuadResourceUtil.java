package io.github.lieonlion.quad.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.ArrayList;
import java.util.List;

public class QuadResourceUtil {
    public static List<String> getElementsAsList(JsonElement elements) {
        if (elements instanceof JsonArray elementsArray) {
            return elementsArray.asList().stream().map(JsonElement::toString).toList();
        } else {
            List<String> list = new ArrayList<>();
            list.add(elements.getAsString());
            return list;
        }
    }

    public static <T> List<String> getElementsAsListWithoutTags(JsonElement elements, ResourceKey<Registry<T>> registryKey) {
        List<String> baseList = getElementsAsList(elements);

        baseList.forEach(element -> {
            if (!element.contains("#")) return;

            baseList.remove(element);

            TagKey<T> tagKey = TagKey.create(registryKey, ResourceLocation.tryParse(element.replace("#", "")));
            Registry<T> registry = Minecraft.getInstance().getSingleplayerServer().registryAccess().registryOrThrow(registryKey);
            HolderSet.Named<T> tag = registry.getOrCreateTag(tagKey);

            tag.stream().map(Holder::value).map(Object::toString).forEach(baseList::add);
        });

        return baseList;
    }
}
