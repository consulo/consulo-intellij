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

import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import com.intellij.util.containers.LinkedMultiMap;
import com.intellij.util.containers.MultiMap;

/**
 * @author VISTALL
 * @since 9:47/16.06.13
 */
public class IdeaLibraryModel
{
	private final MultiMap<IdeaOrderRootType, String> myOrderRoots = new LinkedMultiMap<IdeaOrderRootType, String>();
	private String myName;

	public IdeaLibraryModel()
	{
	}

	public void load(IdeaProjectModel ideaProjectModel, Element element)
	{
		final Element libraryElement = element.getChild("library");

		myName = libraryElement.getAttributeValue("name");
		for(Element libraryEntry : libraryElement.getChildren())
		{
			final String libraryEntryName = libraryEntry.getName();

			IdeaOrderRootType orderRootType = null;
			try
			{
				orderRootType = IdeaOrderRootType.valueOf(libraryEntryName);
			}
			catch(IllegalArgumentException ignored)
			{
			}

			if(orderRootType == null)
			{
				continue;
			}
			parse(libraryEntry, orderRootType);
		}
	}

	private void parse(Element element, @NotNull IdeaOrderRootType orderRootType)
	{
		for(Element child : element.getChildren())
		{
			final String name = child.getName();
			if("root".equals(name))
			{
				final String url = child.getAttributeValue("url");

				myOrderRoots.putValue(orderRootType, url);
			}
		}
	}

	@NotNull
	public MultiMap<IdeaOrderRootType, String> getOrderRoots()
	{
		return myOrderRoots;
	}

	public String getName()
	{
		return myName;
	}
}
