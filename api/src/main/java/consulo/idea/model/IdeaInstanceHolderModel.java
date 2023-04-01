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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author VISTALL
 * @since 10:09/16.06.13
 */
public class IdeaInstanceHolderModel
{
	protected Map<Class, Object> myInstances = new LinkedHashMap<>();

	// skip deprecation - in future version method will not be deprecated
	@SuppressWarnings({"unchecked", "deprecation"})
	public <T> T getInstance(Class<T> clazz)
	{
		return (T) myInstances.computeIfAbsent(clazz, aClass ->
		{
			try
			{
				return aClass.newInstance();
			}
			catch(InstantiationException | IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}

		});
	}
}
