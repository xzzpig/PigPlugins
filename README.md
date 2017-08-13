#PigRPGLore

###有效Lore:

| Lore | 说明         |  生效时机    |  Example  |
|:----:|:----:|:------:|:---------:|
| +[num]% Damagex |  攻击力翻[num]% | 攻击 | +150% Damagex |
| +[num]% Damages | [num]%攻击秒杀  | 攻击 | +1% Damages |
| +[num1]-[num2] Damaged | [num1]~[num2]的伤害(所有类型) | 攻击 | +2-20 Damaged |
| +[num1]-[num2] Damagep | 对玩家造成[num1]~[num2]的伤害(所有类型 | 攻击 | +2-20 Damagep |
| +[num1]-[num2] Damagee | 对非玩家[num1]~[num2]的伤害(所有类型 | 攻击 | +2-20 Damagee |
| +[num]% Evasion | 增加玩家[num]%的闪避几率 | 被攻击 | +5% Evasion |
| +[num]% CriticalChance | [num]%的几率造成暴击伤害 | 攻击 | +5% CriticalChance |
| +[num] CriticalDamage   | 增加暴击[num]的伤害 | 攻击 | +10 CriticalDamage   |
| +[num] LifeSteal | 生命偷取 | 攻击 | +1 LifeSteal |
| +[num] Armor | 减少[num]点伤害 | 被攻击 | +10 Armor |
| +[num] Fireball | 发射[num]个火焰弹 | 右键 | +3 Fireball |
| +[num1]-[num2] [num3] Heal | 治疗[num3]范围内的玩家[num1]-[num2]hp | 右键 | +3-5 6 Heal |
| Lv [num] | 使用者至少[num]级才能使用 | 物品Lore触发前 | Lv 5 |
| Type:[str] | 使用者拥有pigrpglore.type.[str]权限才能使用 | 物品Lore触发前 | Type:XXXX |
| RCCooldown:[num] | 右键冷却[num]ms | 右键Lore触发前 | RCCooldown:1000 |
| SwitchID [num1] [num2] ... [numn] | 右键依次更换物品ID([num1]->[numn])(ID不可重复) | 右键 | SwitchID 1 2 3 4 5 |
|  |  |  |  |