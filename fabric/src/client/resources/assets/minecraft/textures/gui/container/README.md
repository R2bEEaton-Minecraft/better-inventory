# Editable GUI placeholders

These PNGs are unmodified Minecraft 26.2 copies placed under the `minecraft`
namespace so Better Inventory can override the exact textures that the vanilla
screens already request.

- `inventory.png` — survival player inventory background.
- `creative_inventory/tab_inventory.png` — Creative inventory tab.
- `creative_inventory/tab_item_search.png` — Creative search tab.
- `creative_inventory/tab_items.png` — Creative category tabs.

Add one 18-pixel-tall inventory row at the bottom of each relevant background.
Keep the canvas width and the existing texture coordinates unless the matching
screen-rendering code is updated as well.
