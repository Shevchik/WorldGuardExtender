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
import java.util.List;

public class Transform {

	public static <T, O> List<T> toList(Iterable<O> list, Function<T, O> transform) {
		ArrayList<T> transformedlist = new ArrayList<>();
		for (O element : list) {
			transformedlist.add(transform.transform(element));
		}
		return transformedlist;
	}

	public static <T, O> List<T> toList(O[] array, Function<T, O> transform) {
		ArrayList<T> transformedlist = new ArrayList<>();
		for (O element : array) {
			transformedlist.add(transform.transform(element));
		}
		return transformedlist;
	}

	public static interface Function<T, O> {
		public T transform(O original);
	}

}
