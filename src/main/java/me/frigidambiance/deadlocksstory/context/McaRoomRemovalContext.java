package me.frigidambiance.deadlocksstory.context;

import net.minecraft.server.level.ServerPlayer;

public final class McaRoomRemovalContext {
    private static final ThreadLocal<ServerPlayer> REMOVING_ROOM_PLAYER = new ThreadLocal<>();

    private McaRoomRemovalContext() {
    }

    public static void start(ServerPlayer player) {
        REMOVING_ROOM_PLAYER.set(player);
    }

    public static ServerPlayer getPlayer() {
        return REMOVING_ROOM_PLAYER.get();
    }

    public static void clear() {
        REMOVING_ROOM_PLAYER.remove();
    }
}
