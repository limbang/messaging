<div align="center">

![](https://img.shields.io/github/downloads/limbang/messaging/total)
[![](https://img.shields.io/github/license/limbang/mirai-console-mcsm-plugin)](https://github.com/limbang/messaging/blob/master/LICENSE)

![](https://img.shields.io/badge/forge-1.12.2-green)
![](https://img.shields.io/badge/forge--fabric-1.16.5-green)
![](https://img.shields.io/badge/forge--fabric-1.18.2-green)
![](https://img.shields.io/badge/forge--fabric-1.19.2-green)


本项目是基于 [Architectury API](https://github.com/architectury/architectury-api) 编写的 [Minecraft](https://www.minecraft.net/) Mod

使用 WebSocket 转发聊天的消息,配合 [Mirai](https://github.com/mamoe/mirai) 的插件 [messaging](https://github.com/limbang/mirai-console-mcmod-plugin) 可实现和QQ群交互 
</div>

### 使用方法
添加 mod 到游戏 mod文件夹即可,注意依赖 [architectury](https://www.curseforge.com/minecraft/mc-mods/architectury-api) 也需要添加 (1.12.2不需要)

WebSocket 默认使用 2333 端口

在需要转发的消息前面加`群`即可转发所有连接到 WebSocket 的客户端


