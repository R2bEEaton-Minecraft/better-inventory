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
Container textures copied from Minecraft 26.2 for direct editing.

Each PNG other than `inventory.png` keeps its vanilla canvas dimensions. Better Inventory finds the first non-transparent pixel from the bottom at x=4, then duplicates the 18-pixel band beginning 29 pixels above that visible edge into the transparent space directly below it, using nearest-neighbor pixels. This gives Better Inventory a vanilla-style placeholder row while keeping every texture editable.
