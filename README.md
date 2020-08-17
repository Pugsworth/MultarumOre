# MultarumOre
A new ore is in town. Somehow, all the ores of the world have come together and decided to settle their differences for a common goal. To supply the hero with the materials necessary to defeat the evil of the world... Those thieving Endermen!


This is an ore that will randomly give you any ore from the overworld (sorry nether-beings!). Be careful, though. Greed can be some bad ju-ju.
Defaults to generating between levels 8-32. There's a handy config to customize everything!


## Config Options

tntChance: Either a percentage chance from 0.0-1.0 of spawning when broken or 0-100
generationType: one of 3 options (for now). "cross", "vein", "single"
  - *cross* (default) is a cross of 6 blocks with the center hollow. Could the center hold something special? No, probably not.
  - *vein* is the standard way minecraft does ores. Nothing fancy.
  - *single* is similar to how emerald ore generates. Just a single block vibing in the stone. Really fun to up the veinCount with this one!

vein*: These options control how the ore is generating. Unrelated to the **vein** option.
  - Size refers to the general size of the vein. Only suitable for **vein** generationType.
  - Count is the amount of times to try to generate the ore per chunk. Sometimes less
  - Bottom is the lower layer to start generation.
  - Top is something that I haven't quite figured out yet.
  - Maximum is the top level to generate the ore at.
  
These are subject to change when I decide of better names or more options.
