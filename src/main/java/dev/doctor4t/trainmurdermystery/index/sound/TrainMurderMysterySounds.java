package dev.doctor4t.trainmurdermystery.index.sound;

import dev.doctor4t.trainmurdermystery.TrainMurderMystery;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public interface TrainMurderMysterySounds {
    // Block generic sounds
    SoundEvent BLOCK_VENT_SHAFT_BREAK = create("block.vent_shaft.break");
    SoundEvent BLOCK_VENT_SHAFT_STEP = create("block.vent_shaft.step");
    SoundEvent BLOCK_VENT_SHAFT_PLACE = create("block.vent_shaft.place");
    SoundEvent BLOCK_VENT_SHAFT_HIT = create("block.vent_shaft.hit");
    SoundEvent BLOCK_VENT_SHAFT_FALL = create("block.vent_shaft.fall");

    // Block special sounds
    SoundEvent BLOCK_CARGO_BOX_OPEN = create("block.cargo_box.open");
    SoundEvent BLOCK_CARGO_BOX_CLOSE = create("block.cargo_box.close");
    SoundEvent BLOCK_LIGHT_TOGGLE = create("block.light.toggle");
    SoundEvent BLOCK_PRIVACY_PANEL_TOGGLE = create("block.privacy_panel.toggle");
    SoundEvent BLOCK_SPACE_BUTTON_TOGGLE = create("block.space_button.toggle");
    SoundEvent BLOCK_BUTTON_TOGGLE_NO_POWER = create("block.button.toggle_no_power");
    SoundEvent BLOCK_PIPE_EXTEND = create("block.pipe.extend");
    SoundEvent BLOCK_ICHOR_GULP = create("block.ichor.gulp");
    SoundEvent BLOCK_MAUVE_PLUSH_HONK = create("block.mauve_plush.honk");
    SoundEvent BLOCK_MARSHMALLOW_CAN_STORE = create("block.marshmallow_can.store");
    SoundEvent BLOCK_MARSHMALLOW_CAN_TAKE = create("block.marshmallow_can.take");
    SoundEvent BLOCK_DOOR_TOGGLE = create("block.door.toggle");
    SoundEvent BLOCK_SPRINKLER_RUN = create("block.sprinkler.run");
    SoundEvent BLOCK_PUMP_RUN = create("block.pump.run");
    SoundEvent BLOCK_AIRLOCK_CYCLE = create("block.airlock.cycle");

    // Items
    SoundEvent ITEM_STIM_USE = create("item.stim.use");
    SoundEvent ITEM_TELESCOPE_ZOOM_IN = create("item.telescope.zoom_in");
    SoundEvent ITEM_TELESCOPE_ZOOM_OUT = create("item.telescope.zoom_out");

    // Entities
    SoundEvent ENTITY_FLASHLIGHT_TOGGLE = create("entity.flashlight.toggle");
    SoundEvent ENTITY_JETPACK_FLY = create("entity.jetpack.fly");
    SoundEvent ENTITY_JETPACK_FLY_SELF = create("entity.jetpack.fly_self");
    SoundEvent ENTITY_JETPACK_START = create("entity.jetpack.start");
    SoundEvent ENTITY_JETPACK_START_SELF = create("entity.jetpack.start_self");
    SoundEvent ENTITY_PLAYER_BREATHE = create("entity.player.breathe");
    SoundEvent ENTITY_PLAYER_CHOKE = create("entity.player.choke");
    SoundEvent ENTITY_PLAYER_REVIVE = create("entity.player.revive");

    // Ambience
    SoundEvent AMBIENT_TRAIN_INSIDE = create("ambient.train.inside");
    SoundEvent AMBIENT_TRAIN_OUTSIDE = create("ambient.train.outside");

    // UI
    SoundEvent UI_WARNING_LOW = create("ui.warning.low");
    SoundEvent UI_WARNING_CRITICAL = create("ui.warning.critical");


    private static SoundEvent create(String name) {
        Identifier id = TrainMurderMystery.id(name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    private static BlockSoundGroup createBlockSoundGroup(String name, float volume, float pitch) {
        return new BlockSoundGroup(volume, pitch,
                create("block." + name + ".break"),
                create("block." + name + ".step"),
                create("block." + name + ".place"),
                create("block." + name + ".hit"),
                create("block." + name + ".fall"));
    }

    private static BlockSoundGroup copyBlockSoundGroup(BlockSoundGroup blockSoundGroup, float volume, float pitch) {
        return new BlockSoundGroup(volume, pitch,
                blockSoundGroup.getBreakSound(),
                blockSoundGroup.getStepSound(),
                blockSoundGroup.getPlaceSound(),
                blockSoundGroup.getHitSound(),
                blockSoundGroup.getFallSound());
    }

    static void initialize() {
    }

}
