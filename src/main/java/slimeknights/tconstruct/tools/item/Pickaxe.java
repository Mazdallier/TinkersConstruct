package slimeknights.tconstruct.tools.item;

import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

import slimeknights.tconstruct.library.materials.ToolMaterialStats;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.ToolBuilder;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.tools.TinkerTools;

public class Pickaxe extends ToolCore {

  // Pick-head, binding, tool-rod
  public Pickaxe() {
    super(new PartMaterialType.ToolPartType(TinkerTools.pickHead),
          new PartMaterialType.ToolPartType(TinkerTools.toolRod),
          new PartMaterialType.ToolPartType(TinkerTools.binding));

    addCategory(Category.HARVEST);

    // set the toolclass, actual harvestlevel is done by the overridden callback
    this.setHarvestLevel("pickaxe", 0);
  }

  @Override
  public NBTTagCompound buildTag(List<Material> materials) {
    if(materials.size() != requiredComponents.length) {
      return new NBTTagCompound();
    }

    ToolMaterialStats head = materials.get(0).getStats(ToolMaterialStats.TYPE);
    ToolMaterialStats handle = materials.get(1).getStats(ToolMaterialStats.TYPE);
    ToolMaterialStats binding = materials.get(2).getStats(ToolMaterialStats.TYPE);

    ToolNBT data = new ToolNBT(head);

    // handle influences durability
    // binding quality influences how well the handle interacts with the head
    data.durability *= 0.2f + 0.8f*(handle.handleQuality * (1 + binding.extraQuality)/2);
    // handle also influences mining speed a bit (0-20% change)
    data.speed *= 0.8f + handle.handleQuality*0.2f;
    // binding adds a bit to the speed
    data.speed += (binding.miningspeed * binding.extraQuality)*0.14f;

    // 3 free modifiers
    data.modifiers = 3;

    return data.get();
  }
}