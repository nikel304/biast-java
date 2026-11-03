package com.biast.mod;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(BiastMod.MODID)
public class BiastMod {
    public static final String MODID = "biast";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);
    public static final RegistryObject<SoundEvent> LMS_THEME = SOUNDS.register("lms_theme", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "lms_theme")));
    public static final RegistryObject<Item> COMMAND_BOOK = ITEMS.register("command_book", () -> new BiastGuideBook(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> GOLDEN_TEAPOT = ITEMS.register("golden_teapot",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MEDKIT = ITEMS.register("medkit", () -> new BiastSupplyItem(BiastSupplyItem.Kind.MEDKIT));
    public static final RegistryObject<Item> STUN_CHARM = ITEMS.register("stun_charm", () -> new BiastSupplyItem(BiastSupplyItem.Kind.STUN_CHARM));
    public static final RegistryObject<Item> DASH_CHARGE = ITEMS.register("dash_charge", () -> new BiastSupplyItem(BiastSupplyItem.Kind.DASH_CHARGE));
    public static final RegistryObject<Item> VOID_RELIC = ITEMS.register("void_relic", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<EntityType<SlackerEntity>> SLACKER = ENTITY_TYPES.register("slacker",
            () -> EntityType.Builder.of(SlackerEntity::new, MobCategory.MONSTER)
                    .sized(0.7F, 2.0F).clientTrackingRange(8).build("slacker"));

    public static final RegistryObject<EntityType<BiastCreatureEntity>> OBSERVER = ENTITY_TYPES.register("observer",
            () -> EntityType.Builder.of(BiastCreatureEntity::new, MobCategory.MONSTER).sized(0.7F, 2.0F).clientTrackingRange(8).build("observer"));
    public static final RegistryObject<EntityType<BiastCreatureEntity>> CUBED_HEXA22 = ENTITY_TYPES.register("cubedhexa22",
            () -> EntityType.Builder.of(BiastCreatureEntity::new, MobCategory.MONSTER).sized(0.8F, 2.0F).clientTrackingRange(8).build("cubedhexa22"));
    public static final RegistryObject<EntityType<BiastCreatureEntity>> ASHLE = ENTITY_TYPES.register("ashle",
            () -> EntityType.Builder.of(BiastCreatureEntity::new, MobCategory.MONSTER).sized(0.9F, 2.2F).clientTrackingRange(8).build("ashle"));

    public static final RegistryObject<Item> OBSERVER_SPAWN_EGG = ITEMS.register("observer_spawn_egg", () -> new SpawnEggItem(OBSERVER, 0x222222, 0x777777, new Item.Properties()));
    public static final RegistryObject<Item> CUBED_SPAWN_EGG = ITEMS.register("cubedhexa22_spawn_egg", () -> new SpawnEggItem(CUBED_HEXA22, 0x3333AA, 0xAAAAFF, new Item.Properties()));
    public static final RegistryObject<Item> ASHLE_SPAWN_EGG = ITEMS.register("ashle_spawn_egg", () -> new SpawnEggItem(ASHLE, 0x551111, 0xDD5555, new Item.Properties()));

    public static final RegistryObject<Item> SLACKER_SPAWN_EGG = ITEMS.register("slacker_spawn_egg",
            () -> new SpawnEggItem(SLACKER, 0x343434, 0x9B1C1C, new Item.Properties()));

    public BiastMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        ENTITY_TYPES.register(bus);
        SOUNDS.register(bus);
        bus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(GOLDEN_TEAPOT);
            event.accept(MEDKIT);
            event.accept(STUN_CHARM);
            event.accept(DASH_CHARGE);
            event.accept(VOID_RELIC);
            event.accept(COMMAND_BOOK);
        }
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(SLACKER_SPAWN_EGG);
            event.accept(OBSERVER_SPAWN_EGG);
            event.accept(CUBED_SPAWN_EGG);
            event.accept(ASHLE_SPAWN_EGG);
        }
    }
}
