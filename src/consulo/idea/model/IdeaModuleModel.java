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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import consulo.idea.model.orderEnties.IdeaOrderEntryModel;
import consulo.idea.model.orderEnties.InheritedIdeaOrderEntryModel;
import consulo.idea.model.orderEnties.JdkSourceIdeaOrderEntryModel;
import consulo.idea.model.orderEnties.ModuleIdeaOrderEntryModel;
import consulo.idea.model.orderEnties.ModuleLibraryIdeaOrderEntryModel;
import consulo.idea.model.orderEnties.ModuleSourceIdeaOrderEntryModel;
import consulo.idea.model.orderEnties.ProjectLibraryIdeaOrderEntryModel;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.xpath.XPath;
import org.jetbrains.annotations.NotNull;
import lombok.SneakyThrows;
import lombok.val;

/**
 * @author VISTALL
 * @since 9:57/16.06.13
 */
public class IdeaModuleModel extends IdeaPropertyHolderModel<IdeaModuleModel> implements IdeaParseableModel
{
	private final List<IdeaContentEntryModel> myContentEntries = new ArrayList<IdeaContentEntryModel>();
	private final List<IdeaOrderEntryModel> myOrderEntries = new ArrayList<IdeaOrderEntryModel>();
	private final File myFilePath;
	private final String myGroup;
	private String myModuleType;

	public IdeaModuleModel(File filepath, String group)
	{
		myFilePath = filepath;
		myGroup = group;
	}

	public List<IdeaContentEntryModel> getContentEntries()
	{
		return myContentEntries;
	}

	public List<IdeaOrderEntryModel> getOrderEntries()
	{
		return myOrderEntries;
	}

	@SneakyThrows
	@Override
	public void load(IdeaProjectModel ideaProjectModel, File ideaProjectDir)
	{
		val moduleFile = myFilePath;
		val document = ideaProjectModel.loadDocument(moduleFile);

		IdeaProjectModel.expand("$MODULE_DIR$", moduleFile.getParentFile().getAbsolutePath(), document.getRootElement());

		myModuleType = document.getRootElement().getAttributeValue("type");
		XPath xpathExpression = XPath.newInstance("/module[@version='4']/component[@name='NewModuleRootManager']");
		final Element componentNode = (Element) xpathExpression.selectSingleNode(document);
		for(Attribute attribute : componentNode.getAttributes())
		{
			addProperty(attribute.getName(), attribute.getValue());
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
						String sourceFolderUrl = childOfContent.getAttributeValue("url");

						IdeaContentFolderModel folderModel = contentEntryModel.addFolder(sourceFolderUrl);

						for(Attribute attribute : childOfContent.getAttributes())
						{
							folderModel.addProperty(attribute.getName(), attribute.getValue());
						}
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

	@NotNull
	public File getFile()
	{
		return myFilePath;
	}

	public String getGroup()
	{
		return myGroup;
	}

	@Override
	public String toString()
	{
		return myFilePath.getPath();
	}
}
