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

package wgextender.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtils {

	public static List<String> filterStartsWith(String part, List<String> all) {
		ArrayList<String> result = new ArrayList<>();
		for (String element : all) {
			if (element.toLowerCase().startsWith(part.toLowerCase())) {
				result.add(element);
			}
		}
		return result;
	}

	public static List<String> filterStartsWith(String part, String[] all) {
		return filterStartsWith(part, Arrays.asList(all));
	}

}
