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

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

public class ReflectionUtils {

	@SuppressWarnings("unchecked")
	public static <T> T getField(Object obj, String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		return (T) getField(obj.getClass(), fieldName).get(obj);
	}

	public static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException, SecurityException {
		return setAccessible(clazz.getDeclaredField(fieldName));
	}

	public static <T extends AccessibleObject> T setAccessible(T object) {
		object.setAccessible(true);
		return object;
	}

}
