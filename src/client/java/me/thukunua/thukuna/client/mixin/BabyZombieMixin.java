package me.thukunua.thukuna.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class BabyZombieMixin {

    private static final Identifier ZOMBIE_AMBIENT = Identifier.of("minecraft", "entity.zombie.ambient");
    private static final Identifier BABY_ZOMBIE_ID = Identifier.of("thukuna", "baby_zombie");

    @Inject(method = "onPlaySound", at = @At("HEAD"), cancellable = true)
    private void onPlaySound(PlaySoundS2CPacket packet, CallbackInfo ci) {
        Identifier soundId = packet.getSound().value().id();
        if (!ZOMBIE_AMBIENT.equals(soundId)) return;
        if (packet.getPitch() <= 1.0f) return;

        ci.cancel();

        SoundEvent babyZombie = SoundEvent.of(BABY_ZOMBIE_ID);
        AbstractSoundInstance instance = new AbstractSoundInstance(babyZombie, SoundCategory.HOSTILE, Random.create()) {
            @Override public float getVolume() { return 1.0f; }
            @Override public float getPitch()  { return 1.0f; }
            @Override public boolean isRepeatable() { return false; }
        };
        MinecraftClient.getInstance().getSoundManager().play(instance);
    }
}