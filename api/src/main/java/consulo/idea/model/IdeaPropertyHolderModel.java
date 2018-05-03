/*
 * Copyright 2013-2015 must-be.org
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

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 25.08.2015
 */
public abstract class IdeaPropertyHolderModel<T extends IdeaPropertyHolderModel<T>>
{
	private Map<String, String> myProperties = new HashMap<String, String>();

	@NotNull
	@SuppressWarnings("unchecked")
	public T addProperty(String name, String value)
	{
		myProperties.put(name, value);
		return (T) this;
	}

	public Map<String, String> getProperties()
	{
		return myProperties;
	}

	public boolean getBoolProperty(String name)
	{
		String property = getProperty(name);
		return property != null && Boolean.parseBoolean(property);
	}

	public String getProperty(String name)
	{
		return myProperties.get(name);
	}
}
