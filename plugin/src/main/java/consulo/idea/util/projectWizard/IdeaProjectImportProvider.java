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
package consulo.idea.util.projectWizard;

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.roots.impl.libraries.ProjectLibraryTable;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VfsUtil;
import consulo.annotation.access.RequiredReadAction;
import consulo.ide.newProject.ui.UnifiedProjectOrModuleNameStep;
import consulo.idea.IdeaConstants;
import consulo.idea.impl.icon.IdeaImplIconGroup;
import consulo.idea.model.*;
import consulo.idea.model.orderEnties.IdeaOrderEntryModel;
import consulo.idea.model.orderEnties.ModuleIdeaOrderEntryModel;
import consulo.idea.model.orderEnties.ModuleLibraryIdeaOrderEntryModel;
import consulo.idea.model.orderEnties.ProjectLibraryIdeaOrderEntryModel;
import consulo.idea.util.IdeaModuleTypeConfigurationPanel;
import consulo.idea.util.IdeaModuleTypeToModuleExtensionConverter;
import consulo.module.extension.ModuleExtension;
import consulo.module.extension.ModuleExtensionWithSdk;
import consulo.moduleImport.ModuleImportProvider;
import consulo.roots.ContentFolderTypeProvider;
import consulo.roots.impl.ProductionContentFolderTypeProvider;
import consulo.roots.impl.ProductionResourceContentFolderTypeProvider;
import consulo.roots.impl.TestContentFolderTypeProvider;
import consulo.roots.impl.TestResourceContentFolderTypeProvider;
import consulo.roots.impl.property.GeneratedContentFolderPropertyProvider;
import consulo.ui.image.Image;
import consulo.ui.wizard.WizardStep;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author VISTALL
 * @since 18:48/14.06.13
 */
public class IdeaProjectImportProvider implements ModuleImportProvider<IdeaImportContext>
{
	@Nonnull
	@Override
	public String getName()
	{
		return "IntelliJ Platform";
	}

	@Nonnull
	@Override
	public Image getIcon()
	{
		return IdeaImplIconGroup.idea();
	}

	@Override
	public boolean canImport(@Nonnull File fileOrDirectory)
	{
		if(fileOrDirectory.isDirectory())
		{
			return new File(fileOrDirectory, IdeaConstants.PROJECT_DIR).exists();
		}
		return false;
	}

	@Override
	public boolean isOnlyForNewImport()
	{
		return true;
	}

	@Nonnull
	@Override
	public IdeaImportContext createContext(@Nullable Project project)
	{
		return new IdeaImportContext(project);
	}

	@Override
	public void buildSteps(@Nonnull Consumer<WizardStep<IdeaImportContext>> consumer, @Nonnull IdeaImportContext context)
	{
		Map<String, IdeaModuleTypeConfigurationPanel> configuration = context.getConfiguration();
		consumer.accept(new UnifiedProjectOrModuleNameStep<>(context));

		if(!configuration.isEmpty())
		{
			consumer.accept(new IdeaModuleConfigurationStep(configuration));
		}
	}

	@RequiredReadAction
	@Override
	public void process(@Nonnull IdeaImportContext context, @Nonnull Project project, @Nonnull ModifiableModuleModel newModel, @Nonnull Consumer<Module> consumer)
	{
		IdeaProjectModel ideaProjectModel = context.getIdeaProjectModel();

		if(ideaProjectModel == null)
		{
			return;
		}

		List<IdeaModuleModel> ideaModuleModels = ideaProjectModel.getInstance(IdeaModuleTableModel.class).getModules();

		Map<String, IdeaModuleTypeConfigurationPanel> map = context.getConfiguration();

		for(IdeaModuleModel ideaModuleModel : ideaModuleModels)
		{
			File moduleFile = ideaModuleModel.getFile();

			String nameWithoutExtension = FileUtil.getNameWithoutExtension(moduleFile);

			List<IdeaContentEntryModel> contentEntries = ideaModuleModel.getContentEntries();
			String modulePath = null;
			if(!contentEntries.isEmpty())
			{
				modulePath = VfsUtil.urlToPath(contentEntries.get(0).getUrl());
			}

			Module module = newModel.newModule(nameWithoutExtension, modulePath);

			consumer.accept(module);

			final ModifiableRootModel modifiableModel = ModuleRootManager.getInstance(module).getModifiableModel();

			String group = ideaModuleModel.getGroup();
			if(group != null)
			{
				newModel.setModuleGroupPath(module, group.split("/"));
			}

			for(IdeaContentEntryModel ideaContentEntryModel : contentEntries)
			{
				ContentEntry contentEntry = modifiableModel.addContentEntry(ideaContentEntryModel.getUrl());

				for(IdeaContentFolderModel entry : ideaContentEntryModel.getContentFolders())
				{
					ContentFolderTypeProvider provider = ProductionContentFolderTypeProvider.getInstance();
					if(entry.getBoolProperty("isTestSource"))
					{
						provider = TestContentFolderTypeProvider.getInstance();
					}

					String type = entry.getProperty("type");
					if("java-resource".equals(type))
					{
						provider = ProductionResourceContentFolderTypeProvider.getInstance();
					}
					else if("java-test-resource".equals(type))
					{
						provider = TestResourceContentFolderTypeProvider.getInstance();
					}

					ContentFolder contentFolder = contentEntry.addFolder(entry.getUrl(), provider);

					if(entry.getBoolProperty("generated"))
					{
						contentFolder.setPropertyValue(GeneratedContentFolderPropertyProvider.IS_GENERATED, Boolean.TRUE);
					}
				}
			}

			for(IdeaOrderEntryModel orderEntryModel : ideaModuleModel.getOrderEntries())
			{
				OrderEntry orderEntry = null;
				if(orderEntryModel instanceof ModuleIdeaOrderEntryModel)
				{
					orderEntry = modifiableModel.addInvalidModuleEntry(((ModuleIdeaOrderEntryModel) orderEntryModel).getModuleName());
				}
				else if(orderEntryModel instanceof ProjectLibraryIdeaOrderEntryModel)
				{
					orderEntry = modifiableModel.addInvalidLibrary(((ProjectLibraryIdeaOrderEntryModel) orderEntryModel).getLibraryName(), "project");
				}
				else if(orderEntryModel instanceof ModuleLibraryIdeaOrderEntryModel)
				{
					IdeaLibraryModel libraryModel = ((ModuleLibraryIdeaOrderEntryModel) orderEntryModel).getLibraryModel();

					Library library = modifiableModel.getModuleLibraryTable().createLibrary(libraryModel.getName());

					convertLibrary(library, libraryModel);

					orderEntry = modifiableModel.findLibraryOrderEntry(library);
				}

				//noinspection ConstantConditions
				if(orderEntry instanceof ExportableOrderEntry)
				{
					((ExportableOrderEntry) orderEntry).setExported(orderEntryModel.isExported());
				}
			}

			String moduleType = ideaModuleModel.getModuleType();

			IdeaModuleTypeConfigurationPanel ideaModuleTypeConfigurationPanel = map.get(moduleType);
			if(ideaModuleTypeConfigurationPanel != null)
			{
				IdeaModuleTypeToModuleExtensionConverter converter = IdeaModuleTypeToModuleExtensionConverter.EP.findSingle(moduleType);
				assert converter != null;
				//noinspection unchecked
				converter.convertTypeToExtension(modifiableModel, ideaModuleModel, ideaModuleTypeConfigurationPanel);
			}

			for(ModuleExtension moduleExtension : modifiableModel.getExtensions())
			{
				if(moduleExtension instanceof ModuleExtensionWithSdk)
				{
					if(((ModuleExtensionWithSdk) moduleExtension).getInheritableSdk().isNull())
					{
						continue;
					}
					modifiableModel.addModuleExtensionSdkEntry((ModuleExtensionWithSdk<?>) moduleExtension);
				}
			}

			WriteAction.run(modifiableModel::commit);
		}

		LibraryTable libraryTable = ProjectLibraryTable.getInstance(project);

		LibraryTable.ModifiableModel libraryTableModifiableModel = libraryTable.getModifiableModel();

		for(IdeaLibraryModel ideaLibraryModel : ideaProjectModel.getInstance(IdeaProjectLibraryTableModel.class).getLibraries())
		{
			Library library = libraryTableModifiableModel.createLibrary(ideaLibraryModel.getName());

			convertLibrary(library, ideaLibraryModel);
		}

		WriteAction.run(libraryTableModifiableModel::commit);
	}

	private static void convertLibrary(Library library, IdeaLibraryModel ideaLibraryModel)
	{
		Library.ModifiableModel modifiableModel = library.getModifiableModel();
		for(Map.Entry<IdeaOrderRootType, Collection<String>> entry : ideaLibraryModel.getOrderRoots().entrySet())
		{
			for(String url : entry.getValue())
			{
				modifiableModel.addRoot(url, entry.getKey().getOrderRootType());
			}
		}

		WriteAction.run(modifiableModel::commit);
	}

	@Override
	public String getFileSample()
	{
		return "<b>IntelliJ Platform</b> project directory (.idea)";
	}
}
