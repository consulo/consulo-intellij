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
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.components.PathMacroMap;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;

/**
 * @author VISTALL
 * @since 9:49/16.06.13
 */
public class IdeaProjectModel extends IdeaInstanceHolderModel implements IdeaParseableModel
{
	private File myIdeaProjectDir;
	private String myName;

	public IdeaProjectModel(File dotIdeaDirectory)
	{
		myIdeaProjectDir = dotIdeaDirectory;
		getInstance(IdeaProjectLibraryTableModel.class);
		getInstance(IdeaProjectRootModel.class);
		getInstance(IdeaModuleTableModel.class);

		load(this, dotIdeaDirectory);

		File nameFile = new File(dotIdeaDirectory, ".name");
		if(nameFile.exists())
		{
			try
			{
				myName = FileUtil.loadFile(nameFile);
			}
			catch(IOException ignored)
			{
			}
		}

		if(myName == null)
		{
			myName = dotIdeaDirectory.getParent();
		}
	}

	public String getName()
	{
		return myName;
	}

	@NotNull
	public Document loadDocument(final File file) throws JDOMException, IOException
	{
		Document document = JDOMUtil.loadDocument(file);

		expand("$PROJECT_DIR$", myIdeaProjectDir.getParentFile().getAbsolutePath(), document.getRootElement());

		return document;
	}

	public static void expand(final String var, final String value, Element element)
	{
		PathMacroMap pathMacroMap = new PathMacroMap()
		{
			@Override
			public String substitute(String text, boolean caseSensitive)
			{
				return StringUtil.replace(text, var, value, !caseSensitive);
			}

			@Override
			public int hashCode()
			{
				return 1;
			}
		};
		pathMacroMap.substitute(element, false);
	}

	@Override
	public void load(IdeaProjectModel ideaProjectModel, File ideaProjectDir)
	{
		for(Object o : myInstances.values())
		{
			if(o instanceof IdeaParseableModel)
			{
				((IdeaParseableModel) o).load(this, ideaProjectDir);
			}
		}
	}
}
