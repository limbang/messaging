/*
 * Copyright 2022 limbang and contributors.
 *
 * 此源代码的使用受 GNU AGPLv3 许可证的约束，该许可证可在"LICENSE"文件中找到。
 * Use of this source code is governed by the GNU AGPLv3 license that can be found in the "LICENSE" file.
 */

package top.limbang.quilt;

import top.limbang.fabriclike.MessagingFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class MessagingQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        MessagingFabricLike.init();
    }
}
