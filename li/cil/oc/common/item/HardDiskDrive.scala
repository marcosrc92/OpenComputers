package li.cil.oc.common.item

import java.util
import li.cil.oc.Config
import net.minecraft.client.renderer.texture.IconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

class HardDiskDrive(val parent: Delegator, val tier: Int) extends Delegate {
  val unlocalizedName = "HardDiskDrive"

  val kiloBytes = Config.hddSizes(tier)

  override def addInformation(stack: ItemStack, player: EntityPlayer, tooltip: util.List[String], advanced: Boolean) = {
    if (stack.hasTagCompound) {
      val nbt = stack.getTagCompound
      if (nbt.hasKey(Config.namespace + "data")) {
        val data = nbt.getCompoundTag(Config.namespace + "data")
        if (data.hasKey(Config.namespace + "fs.label")) {
          tooltip.add(data.getString(Config.namespace + "fs.label"))
        }
        if (advanced && data.hasKey("fs")) {
          val fsNbt = data.getCompoundTag("fs")
          if (fsNbt.hasKey("capacity.used")) {
            val used = fsNbt.getLong("capacity.used")
            tooltip.add("Disk usage: %d/%d Byte".format(used, kiloBytes * 1024))
          }
        }
      }
    }
    super.addInformation(stack, player, tooltip, advanced)
  }

  override def getItemDisplayName(stack: ItemStack) = {
    val localizedName = parent.getItemStackDisplayName(stack)
    Some(if (kiloBytes >= 1024) {
      localizedName + " (%dMB)".format(kiloBytes / 1024)
    }
    else {
      localizedName + " (%dKB)".format(kiloBytes)
    })
  }

  override def registerIcons(iconRegister: IconRegister) {
    super.registerIcons(iconRegister)

    icon = iconRegister.registerIcon(Config.resourceDomain + ":hdd" + tier)
  }
}