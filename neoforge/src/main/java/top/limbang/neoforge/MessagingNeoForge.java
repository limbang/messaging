package top.limbang.neoforge;

import top.limbang.Messaging;
import net.neoforged.fml.common.Mod;

@Mod(Messaging.MOD_ID)
public final class MessagingNeoForge {
    public MessagingNeoForge() {
        // Run our common setup.
        Messaging.init();
    }
}
