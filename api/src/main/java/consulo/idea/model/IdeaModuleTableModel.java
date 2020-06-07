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

import com.intellij.openapi.diagnostic.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.Filters;
import org.jdom.xpath.XPathExpression;
import org.jdom.xpath.XPathFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 9:57/16.06.13
 */
public class IdeaModuleTableModel implements IdeaParseableModel
{
	private static final Logger LOGGER = Logger.getInstance(IdeaModuleTableModel.class);

	private final List<IdeaModuleModel> myModules = new ArrayList<IdeaModuleModel>();

	@Override
	public void load(IdeaProjectModel ideaProjectModel, File ideaProjectDir)
	{
		try
		{
			File modulesFile = new File(ideaProjectDir, "modules.xml");
			if(!modulesFile.exists())
			{
				return;
			}

			final Document document = ideaProjectModel.loadDocument(modulesFile);

			XPathExpression<Element> xpathExpression = XPathFactory.instance().compile("/project[@version='4']/component[@name='ProjectModuleManager']/modules/*", Filters.element());

			//noinspection unchecked
			final List<Element> list = xpathExpression.evaluate(document);

			for(Element element : list)
			{
				String filepath = element.getAttributeValue("filepath");
				if(filepath == null)
				{
					continue;
				}

				File file = new File(filepath);
				if(!file.exists())
				{
					continue;
				}

				final IdeaModuleModel moduleModel = new IdeaModuleModel(file, element.getAttributeValue("group"));
				moduleModel.load(ideaProjectModel, ideaProjectDir);
				myModules.add(moduleModel);
			}
		}
		catch(JDOMException | IOException e)
		{
			LOGGER.error(e);
		}
	}

	public List<IdeaModuleModel> getModules()
	{
		return myModules;
	}
}
