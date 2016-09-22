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

import java.io.File;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;
import lombok.SneakyThrows;

/**
 * @author VISTALL
 * @since 25.08.2015
 */
public class IdeaProjectRootModel extends IdeaPropertyHolderModel<IdeaProjectRootModel> implements IdeaParseableModel
{
	@Override
	@SneakyThrows
	public void load(IdeaProjectModel ideaProjectModel, File ideaProjectDir)
	{
		File miscFile = new File(ideaProjectDir, "misc.xml");
		if(!miscFile.exists())
		{
			return;
		}

		final Document document = ideaProjectModel.loadDocument(miscFile);

		XPath xpathExpression = XPath.newInstance("/project[@version='4']/component[@name='ProjectRootManager']");

		final Element element = (Element) xpathExpression.selectSingleNode(document);
		if(element == null)
		{
			return;
		}

		for(Attribute attribute : element.getAttributes())
		{
			addProperty(attribute.getName(), attribute.getValue());
		}
	}

}
