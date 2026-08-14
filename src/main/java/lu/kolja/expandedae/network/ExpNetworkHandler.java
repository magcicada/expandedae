package lu.kolja.expandedae.network;

import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.network.implementations.HighlightDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.Supplier;

public class ExpNetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    private static int id = 0;

    public static final ExpNetworkHandler HANDLER = new ExpNetworkHandler();
    private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            Expandedae.makeId("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public <MSG> void sendToClient(MSG message, Player player) {
        INSTANCE.sendTo(message, ((ServerPlayer) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    /**
     * Will register any packets, be it c2s or s2c as the network direction is specified in the {@link PacketInfo} annotation on the class
     */
    public static void registerPackets() {
        register(HighlightDataPacket.class, HighlightDataPacket::new);
        register(HighlightDataPacket.HighlightWhat.class, HighlightDataPacket.HighlightWhat::new);
    }

    private static <T extends ExpPacket<T>> void register(Class<T> clazz, Supplier<T> factory) {
        PacketInfo info = clazz.getAnnotation(PacketInfo.class);
        if (info == null) {
            throw new IllegalArgumentException("Packet class must have @PacketInfo annotation");
        }

        try {
            T instance = factory.get();

            INSTANCE.registerMessage(
                    id++,
                    clazz,
                    instance::encode,
                    instance::decode,
                    instance::handle,
                    Optional.of(info.value())
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to register packet: " + clazz.getName(), e);
        }
    }
}
