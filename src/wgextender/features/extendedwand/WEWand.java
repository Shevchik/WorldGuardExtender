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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import wgextender.WGExtender;

public class WEWand {

	private static final String WAND_NAME = ChatColor.LIGHT_PURPLE+"Selection wand";

	@SuppressWarnings("deprecation")
	public static ItemStack getWand() {
		ItemStack itemstack = new ItemStack(WGExtender.getWorldEdit().getLocalConfiguration().wandItem);
		ItemMeta meta = Bukkit.getItemFactory().getItemMeta(itemstack.getType());
		meta.setDisplayName(WAND_NAME);
		itemstack.setItemMeta(meta);
		return itemstack;
	}

	@SuppressWarnings("deprecation")
	public static boolean isWand(ItemStack itemstack) {
		if (itemstack.getTypeId() == WGExtender.getWorldEdit().getLocalConfiguration().wandItem) {
			ItemMeta im = itemstack.getItemMeta();
			if (im != null) {
				return WAND_NAME.equals(im.getDisplayName());
			}
		}
		return false;
	}

}
