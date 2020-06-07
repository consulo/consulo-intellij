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

import consulo.logging.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.Filters;
import org.jdom.xpath.XPathExpression;
import org.jdom.xpath.XPathFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author VISTALL
 * @since 25.08.2015
 */
public class IdeaProjectRootModel extends IdeaPropertyHolderModel<IdeaProjectRootModel> implements IdeaParseableModel
{
	private static final Logger LOGGER = Logger.getInstance(IdeaProjectRootModel.class);

	@Override
	public void load(IdeaProjectModel ideaProjectModel, File ideaProjectDir)
	{
		try
		{
			File miscFile = new File(ideaProjectDir, "misc.xml");
			if(!miscFile.exists())
			{
				return;
			}

			final Document document = ideaProjectModel.loadDocument(miscFile);

			XPathExpression<Element> xpathExpression = XPathFactory.instance().compile("/project[@version='4']/component[@name='ProjectRootManager']", Filters.element());

			final Element element = xpathExpression.evaluateFirst(document);
			if(element == null)
			{
				return;
			}

			for(Attribute attribute : element.getAttributes())
			{
				addProperty(attribute.getName(), attribute.getValue());
			}
		}
		catch(JDOMException | IOException e)
		{
			LOGGER.error(e);
		}
	}

}
