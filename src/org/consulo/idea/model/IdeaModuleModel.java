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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.consulo.idea.model.orderEnties.IdeaOrderEntryModel;
import org.consulo.idea.model.orderEnties.InheritedIdeaOrderEntryModel;
import org.consulo.idea.model.orderEnties.JdkSourceIdeaOrderEntryModel;
import org.consulo.idea.model.orderEnties.ModuleIdeaOrderEntryModel;
import org.consulo.idea.model.orderEnties.ModuleLibraryIdeaOrderEntryModel;
import org.consulo.idea.model.orderEnties.ModuleSourceIdeaOrderEntryModel;
import org.consulo.idea.model.orderEnties.ProjectLibraryIdeaOrderEntryModel;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.xpath.XPath;
import org.mustbe.consulo.roots.impl.ProductionContentFolderTypeProvider;
import org.mustbe.consulo.roots.impl.TestContentFolderTypeProvider;
import lombok.SneakyThrows;
import lombok.val;

/**
 * @author VISTALL
 * @since 9:57/16.06.13
 */
public class IdeaModuleModel implements IdeaParseableModel
{
	private final List<IdeaContentEntryModel> myContentEntries = new ArrayList<IdeaContentEntryModel>();
	private final List<IdeaOrderEntryModel> myOrderEntries = new ArrayList<IdeaOrderEntryModel>();
	private final Map<String, String> myComponentAttributes = new HashMap<String, String>();
	private final String myFilePath;
	private String myModuleType;

	public IdeaModuleModel(String filepath)
	{
		myFilePath = filepath;
	}

	public List<IdeaContentEntryModel> getContentEntries()
	{
		return myContentEntries;
	}

	public List<IdeaOrderEntryModel> getOrderEntries()
	{
		return myOrderEntries;
	}

	public Map<String, String> getComponentAttributes()
	{
		return myComponentAttributes;
	}

	@SneakyThrows
	@Override
	public void load(IdeaProjectModel ideaProjectModel, File ideaProjectDir)
	{
		val moduleFile = new File(myFilePath);
		val document = ideaProjectModel.loadDocument(moduleFile);

		IdeaProjectModel.expand("$MODULE_DIR$", moduleFile.getParentFile().getAbsolutePath(), document.getRootElement());

		myModuleType = document.getRootElement().getAttributeValue("type");
		XPath xpathExpression = XPath.newInstance("/module[@version='4']/component[@name='NewModuleRootManager']");
		final Element componentNode = (Element) xpathExpression.selectSingleNode(document);
		for(Attribute attribute : componentNode.getAttributes())
		{
			myComponentAttributes.put(attribute.getName(), attribute.getValue());
		}

		for(Element element : componentNode.getChildren())
		{
			final String name = element.getName();
			if("content".equals(name))
			{
				final String url = element.getAttributeValue("url");

				final IdeaContentEntryModel contentEntryModel = new IdeaContentEntryModel(url);
				myContentEntries.add(contentEntryModel);

				for(Element childOfContent : element.getChildren())
				{
					final String nameChildOfContent = childOfContent.getName();
					if("sourceFolder".equals(nameChildOfContent))
					{
						String url2 = childOfContent.getAttributeValue("url");
						boolean isTestSource = Boolean.valueOf(childOfContent.getAttributeValue("isTestSource"));

						contentEntryModel.addFolder(url2, isTestSource ? TestContentFolderTypeProvider.getInstance() :
								ProductionContentFolderTypeProvider.getInstance());
					}
				}
			}
			else if("orderEntry".equals(name))
			{
				IdeaOrderEntryModel orderEntryModel = null;
				String type = element.getAttributeValue("type");
				if("module".equals(type))
				{
					String moduleName = element.getAttributeValue("module-name");

					orderEntryModel = new ModuleIdeaOrderEntryModel(moduleName);
				}
				else if("sourceFolder".equals(type))
				{
					orderEntryModel = new ModuleSourceIdeaOrderEntryModel();
				}
				else if("inheritedJdk".equals(type))
				{
					orderEntryModel = new InheritedIdeaOrderEntryModel();
				}
				else if("module-library".equals(type))
				{
					IdeaLibraryModel libraryModel = new IdeaLibraryModel();
					libraryModel.load(ideaProjectModel, element);

					orderEntryModel = new ModuleLibraryIdeaOrderEntryModel(libraryModel);
				}
				else if("jdk".equals(type))
				{
					orderEntryModel = new JdkSourceIdeaOrderEntryModel(element.getAttributeValue("jdkName"));
				}
				else if("library".equals(type))
				{
					final String level = element.getAttributeValue("level");
					if("project".equals(level))
					{
						orderEntryModel = new ProjectLibraryIdeaOrderEntryModel(element.getAttributeValue("name"));
					}
				}

				if(orderEntryModel != null)
				{
					myOrderEntries.add(orderEntryModel);

					orderEntryModel.setExported(element.getAttribute("exported") != null);
				}
			}
		}
	}

	public String getModuleType()
	{
		return myModuleType;
	}

	public String getFilePath()
	{
		return myFilePath;
	}
}
