# mc_mod

根据 `deep-research-report.md` 构建了 Fabric 1.20.1 的 v0.1 原型代码，包含：

- Trinkets `legs/belt` 槽位与 `sky_belt` 物品标签数据。
- 双击空格 C2S 请求、服务端飞行状态机与 S2C HUD 同步。
- `HudRenderCallback + DrawContext` 风压条绘制。

> 说明：该仓库目前只包含实现骨架与核心逻辑，未附带完整 Gradle Wrapper 与运行脚本。
