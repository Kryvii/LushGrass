<p align="center">
  <img src="src/main/resources/icon.png" width="180" alt="Lush Grass 图标">
</p>

# Lush Grass

Lush Grass 是一个用于 Minecraft 1.21.1 NeoForge 的轻量原版风格视觉与世界生成模组。

## 功能

- 让原版草方块使用连续的生物群系染色草面，同时保留泥土底面。
- 原版 `snowy` 状态生效时，所有暴露的草贴图面都会被雪覆盖。
- 在上方无遮挡的草方块上渲染生物群系染色短草，并将几何加入区块模型缓存。
- 添加高度为原版矮草丛四分之三的可放置方块 `lush_grass:low_grass`。
- 复用原版矮草丛的生存、替换、骨粉、剪刀、种子掉落、声音、偏移和堆肥行为。
- 替换常见的原版草丛生成特征；在适用位置，生成的矮草丛约有一半会变为低矮草丛。
- 支持 NeoForge 标准配置界面，并在受支持的 Iris/Sodium 光影渲染流程中将低矮草丛识别为原版矮草丛。

所有视觉附加内容都属于区块几何。模组不添加实体、方块实体、tick、逐帧方块扫描或逐方块绘制调用。

## 运行要求

- Minecraft 1.21.1
- 适用于 Minecraft 1.21.1 的 NeoForge 21.1.152 或更高版本
- Java 21

Iris 和 Sodium 均为可选模组。

## 配置

客户端配置文件位于 `config/lush_grass-client.toml`。

| 配置项 | 默认值 | 说明 |
| --- | --- | --- |
| `visuals.full_grass_block_coverage` | `true` | 使用连续草面改进原版草方块外观。 |
| `visuals.render_grass_tufts` | `true` | 在上方无遮挡的原版草方块上渲染短草。 |

修改任一配置项只会触发一次区块重建。关闭配置后，模型会委托给原始的草方块烘焙模型。

## 资源包与数据包

资源包无需编写代码即可自定义低矮草丛：

- 覆盖 `assets/lush_grass/blockstates/low_grass.json`，可以指定单个模型或多个带权重模型。
- 覆盖 `assets/lush_grass/models/block/low_grass.json` 和
  `assets/lush_grass/models/item/low_grass.json`，可以分别修改方块与物品外观。
- 覆盖 `assets/lush_grass/models/block/grass_tuft.json`，可以修改草方块附加短草所使用的模型。

资源包可以为现有低矮草丛制作视觉变种；如果需要独立方块 ID，仍然需要模组注册。

数据包可以覆盖 `data/lush_grass/worldgen/configured_feature` 中的配置特征，以及
`data/lush_grass/neoforge/biome_modifier` 中的生物群系修改器。这些内容控制生成位置与频率，
不会注册新方块，也不会修改客户端模型。

启用 `visuals.full_grass_block_coverage` 时，Lush Grass 会使用自己的全覆盖草方块模型。
如果连接纹理资源包需要直接控制原版草方块模型，请关闭该选项。

## 开发

```powershell
.\gradlew.bat runData
.\gradlew.bat build
.\gradlew.bat runClient
```

生成的数据位于 `src/generated/resources` 并提交到仓库，其中 `.cache` 目录会被忽略。

## 许可证

- 源代码：[BSD 3-Clause](LICENSE)
- 原创非代码资源：[CC BY-NC-SA 4.0](LICENSE-ASSETS)
- 第三方声明：[NOTICE](NOTICE)
