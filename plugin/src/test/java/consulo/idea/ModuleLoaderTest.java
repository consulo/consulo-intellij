/*
 * Copyright 2013 Consulo.org
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
package consulo.idea;

import java.util.List;

import consulo.idea.model.IdeaContentEntryModel;
import consulo.idea.model.IdeaContentFolderModel;
import consulo.idea.model.IdeaLibraryModel;
import consulo.idea.model.IdeaModuleModel;
import consulo.idea.model.IdeaModuleTableModel;
import consulo.idea.model.IdeaOrderRootType;
import consulo.idea.model.IdeaProjectLibraryTableModel;
import consulo.idea.model.IdeaProjectModel;
import consulo.idea.model.orderEnties.InheritedIdeaOrderEntryModel;
import consulo.idea.model.orderEnties.JdkSourceIdeaOrderEntryModel;
import consulo.idea.model.orderEnties.ModuleIdeaOrderEntryModel;
import consulo.idea.model.orderEnties.ModuleLibraryIdeaOrderEntryModel;
import consulo.idea.model.orderEnties.ModuleSourceIdeaOrderEntryModel;
import consulo.idea.model.orderEnties.ProjectLibraryIdeaOrderEntryModel;

/**
 * @author VISTALL
 * @since 16:54/18.06.13
 */
public class ModuleLoaderTest extends ModuleLoaderTestCase
{

	public void testProject1()
	{
		final IdeaProjectModel ideaProjectModel = getIdeaProjectModel();

		final IdeaProjectLibraryTableModel libraryTableModel = ideaProjectModel.getInstance
				(IdeaProjectLibraryTableModel.class);
		final List<IdeaLibraryModel> libraries = libraryTableModel.getLibraries();
		assertEquals(libraries.size(), 1);
		assertEquals(libraries.get(0).getName(), "concierge-1.0.0");
		assertEquals(libraries.get(0).getOrderRoots().get(IdeaOrderRootType.CLASSES).size(), 1);

		final IdeaModuleTableModel moduleTableModel = ideaProjectModel.getInstance(IdeaModuleTableModel.class);
		assertEquals(moduleTableModel.getModules().size(), 2);

		// test first module
		final IdeaModuleModel firstModule = moduleTableModel.getModules().get(0);
		assertEquals(firstModule.getModuleType(), "JAVA_MODULE");
		assertEquals(firstModule.getContentEntries().size(), 1);
		assertEquals(firstModule.getProperties().size(), 2);
		assertEquals(firstModule.getProperty("name"), "NewModuleRootManager");
		assertEquals(firstModule.getProperty("inherit-compiler-output"), "true");

		IdeaContentEntryModel contentEntryModel = firstModule.getContentEntries().get(0);

		//assertEquals(contentEntryModel.getUrl(), "file://$MODULE_DIR$");
		List<IdeaContentFolderModel> contentFolders = contentEntryModel.getContentFolders();
		assertEquals(contentFolders.size(), 2);
		//assertEquals(contentFolders.get(0).getUrl(), "file://$MODULE_DIR$/src");
		//assertEquals(contentFolders.get(1).getUrl(), "file://$MODULE_DIR$/testSrc");
		assertTrue(contentFolders.get(1).getBoolProperty("isTestSource"));

		assertEquals(firstModule.getOrderEntries().size(), 5);
		assertEquals(firstModule.getOrderEntries().get(0).getClass(), InheritedIdeaOrderEntryModel.class);
		assertEquals(firstModule.getOrderEntries().get(1).getClass(), ModuleSourceIdeaOrderEntryModel.class);
		assertEquals(firstModule.getOrderEntries().get(2).getClass(), ModuleLibraryIdeaOrderEntryModel.class);
		assertEquals(firstModule.getOrderEntries().get(3).getClass(), ModuleLibraryIdeaOrderEntryModel.class);
		assertEquals(firstModule.getOrderEntries().get(4).getClass(), ProjectLibraryIdeaOrderEntryModel.class);

		final IdeaModuleModel secondModule = moduleTableModel.getModules().get(1);
		assertEquals(secondModule.getModuleType(), "JAVA_MODULE");
		assertEquals(secondModule.getContentEntries().size(), 1);
		assertEquals(secondModule.getProperties().size(), 3);
		assertEquals(secondModule.getProperty("name"), "NewModuleRootManager");
		assertEquals(secondModule.getProperty("inherit-compiler-output"), "true");
		assertEquals(secondModule.getProperty("LANGUAGE_LEVEL"), "JDK_1_7");

		contentEntryModel = secondModule.getContentEntries().get(0);

		//assertEquals(contentEntryModel.getUrl(), "file://$MODULE_DIR$");
		contentFolders = contentEntryModel.getContentFolders();
		assertEquals(contentFolders.size(), 1);
		//assertEquals(contentFolders.get(0).getUrl(), "file://$MODULE_DIR$/src");

		assertEquals(secondModule.getOrderEntries().size(), 4);
		assertEquals(secondModule.getOrderEntries().get(0).getClass(), JdkSourceIdeaOrderEntryModel.class);
		assertEquals(secondModule.getOrderEntries().get(1).getClass(), ModuleSourceIdeaOrderEntryModel.class);
		assertEquals(secondModule.getOrderEntries().get(2).getClass(), ModuleIdeaOrderEntryModel.class);
		assertEquals(secondModule.getOrderEntries().get(3).getClass(), ModuleLibraryIdeaOrderEntryModel.class);
	}
}
