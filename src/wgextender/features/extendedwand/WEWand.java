/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package wgextender.features.extendedwand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import wgextender.utils.WEUtils;

public class WEWand {

	protected static final String WAND_NAME = ChatColor.LIGHT_PURPLE + "Selection wand";

	protected static Material cachedWandMaterial;

	protected static Material getWandMaterial() {
		String weWandMaterialName = WEUtils.getWorldEditPlugin().getLocalConfiguration().wandItem.toUpperCase();
		if (cachedWandMaterial == null || !cachedWandMaterial.toString().equals(weWandMaterialName)) {
			cachedWandMaterial = Material.getMaterial(weWandMaterialName.split(":")[1]);
		}
		return cachedWandMaterial;
	}

	public static ItemStack getWand() {
		ItemStack itemstack = new ItemStack(getWandMaterial());
		ItemMeta meta = Bukkit.getItemFactory().getItemMeta(itemstack.getType());
		meta.setDisplayName(WAND_NAME);
		itemstack.setItemMeta(meta);
		return itemstack;
	}

	public static boolean isWand(ItemStack itemstack) {
		if (itemstack.getType().equals(getWandMaterial())) {
			ItemMeta im = itemstack.getItemMeta();
			if (im != null) {
				return WAND_NAME.equals(im.getDisplayName());
			}
		}
		return false;
	}

}
