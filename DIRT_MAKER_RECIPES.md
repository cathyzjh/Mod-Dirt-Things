# Dirt maker recipes (Minecraft 1.12.2)

Add JSON files under `src/main/resources/assets/dirtthings/recipes/dirtmaker/`.
Forge loads them through the `dirtthings:dirt_maker` factory registered in
`recipes/_factories.json`. Machines and JEI read the same registered recipes.
Rebuild the mod and restart Minecraft after editing bundled recipes.

```json
{
  "type": "dirtthings:dirt_maker",
  "ingredient": { "item": "minecraft:dirt", "data": 0, "count": 1 },
  "result": { "item": "dirtthings:dirt_ball", "count": 2 },
  "processing_time": 200
}
```

`ingredient.count` defaults to 1 (range 1–64). `processing_time` defaults to
200 ticks (range 1–32767; 20 ticks per second). Results must fit in one stack.
Specify `data` for items with metadata; this example accepts ordinary dirt only.
Avoid overlapping input definitions. Fuel uses vanilla furnace burn times.

Recipes never match crafting grids or enter the vanilla recipe book. JEI is an
optional client integration: it displays inputs, outputs, and time in a custom
category. No recipe transfer or GUI click shortcut is registered. Install JEI
1.12.2 separately to test the display; the JEI API is not bundled in this mod.
