package com.biast.mod;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BiastMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(BiastMod.SLACKER.get(), SlackerEntity.createAttributes().build());
        event.put(BiastMod.OBSERVER.get(), BiastCreatureEntity.observerAttributes().build());
        event.put(BiastMod.CUBED_HEXA22.get(), BiastCreatureEntity.cubedAttributes().build());
        event.put(BiastMod.ASHLE.get(), BiastCreatureEntity.ashleAttributes().build());
    }

    @Mod.EventBusSubscriber(modid = BiastMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(BiastMod.OBSERVER.get(), context -> new BiastRenderer<>(context, new BiastEntityModel<>(context.bakeLayer(BiastEntityModel.OBSERVER)), "observer"));
            event.registerEntityRenderer(BiastMod.CUBED_HEXA22.get(), context -> new BiastRenderer<>(context, new BiastEntityModel<>(context.bakeLayer(BiastEntityModel.CUBED)), "cubedhexa22"));
            event.registerEntityRenderer(BiastMod.ASHLE.get(), context -> new BiastRenderer<>(context, new BiastEntityModel<>(context.bakeLayer(BiastEntityModel.ASHLE)), "ashle"));
            event.registerEntityRenderer(BiastMod.SLACKER.get(), context -> new BiastRenderer<>(context, new BiastEntityModel<>(context.bakeLayer(BiastEntityModel.SLACKER)), "slacker"));
        }
    }

    @Mod.EventBusSubscriber(modid = BiastMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModelLayersEvents {
        @SubscribeEvent public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(BiastEntityModel.OBSERVER, () -> BiastEntityModel.createLayer(0));
            event.registerLayerDefinition(BiastEntityModel.CUBED, () -> BiastEntityModel.createLayer(1));
            event.registerLayerDefinition(BiastEntityModel.ASHLE, () -> BiastEntityModel.createLayer(2));
            event.registerLayerDefinition(BiastEntityModel.SLACKER, () -> BiastEntityModel.createLayer(3));
        }
    }

    private static class BiastRenderer<T extends net.minecraft.world.entity.Mob> extends MobRenderer<T, BiastEntityModel<T>> {
        private final ResourceLocation texture;
        BiastRenderer(net.minecraft.client.renderer.entity.EntityRendererProvider.Context context, BiastEntityModel<T> model, String id) {
            super(context, model, 0.45F);
            this.texture = new ResourceLocation(BiastMod.MODID, "textures/entity/" + id + ".png");
        }
        @Override public ResourceLocation getTextureLocation(T entity) { return texture; }
    }
}
