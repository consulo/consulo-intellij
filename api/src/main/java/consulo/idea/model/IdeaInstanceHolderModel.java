/*
 * Copyright 2013 must-be.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package consulo.idea.model;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;

import com.intellij.util.ReflectionUtil;

/**
 * @author VISTALL
 * @since 10:09/16.06.13
 */
public class IdeaInstanceHolderModel
{
	protected Map<Class, Object> myInstances = new LinkedHashMap<Class, Object>();

	@SuppressWarnings("unchecked")
	public <T> T getInstance(Class<T> clazz)
	{
		Object o = myInstances.get(clazz);
		if(o == null)
		{
			final Constructor<T> defaultConstructor = ReflectionUtil.getDefaultConstructor(clazz);
			myInstances.put(clazz, o = ReflectionUtil.createInstance(defaultConstructor));
		}
		return (T) o;
	}
}
