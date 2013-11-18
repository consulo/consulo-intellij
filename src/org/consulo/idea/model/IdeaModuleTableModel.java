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
package org.consulo.idea.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;
import lombok.SneakyThrows;

/**
 * @author VISTALL
 * @since 9:57/16.06.13
 */
public class IdeaModuleTableModel implements IdeaParseableModel
{
	private final List<IdeaModuleModel> myModules = new ArrayList<IdeaModuleModel>();

	@Override
	@SneakyThrows
	public void load(IdeaProjectModel ideaProjectModel, File ideaProjectDir)
	{
		File modulesFile = new File(ideaProjectDir, "modules.xml");
		if(!modulesFile.exists())
		{
			return;
		}

		final Document document = ideaProjectModel.loadDocument(modulesFile);

		XPath xpathExpression = XPath.newInstance("/project[@version='4']/component[@name='ProjectModuleManager']/modules/*");

		final List list = xpathExpression.selectNodes(document);

		for(Object o : list)
		{
			Element element = (Element) o;

			String filepath = element.getAttributeValue("filepath");
			if(filepath == null)
			{
				continue;
			}

			final IdeaModuleModel moduleModel = new IdeaModuleModel(filepath, element.getAttributeValue("group"));
			moduleModel.load(ideaProjectModel, ideaProjectDir);
			myModules.add(moduleModel);
		}
	}

	public List<IdeaModuleModel> getModules()
	{
		return myModules;
	}
}
