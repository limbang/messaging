/*
 * Copyright 2022 limbang and contributors.
 *
 * 此源代码的使用受 GNU AGPLv3 许可证的约束，该许可证可在"LICENSE"文件中找到。
 * Use of this source code is governed by the GNU AGPLv3 license that can be found in the "LICENSE" file.
 */

package top.limbang.forge;

import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.limbang.Messaging;

@Mod(Messaging.MOD_ID)
public class MessagingForge {
    public MessagingForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(Messaging.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Messaging.init();
    }
}
