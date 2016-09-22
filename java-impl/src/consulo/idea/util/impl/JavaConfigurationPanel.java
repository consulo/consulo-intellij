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

package consulo.idea.util.impl;

import javax.swing.JComponent;
import javax.swing.JList;

import consulo.idea.model.IdeaProjectModel;
import consulo.idea.model.IdeaProjectRootModel;
import consulo.idea.util.IdeaModuleTypeConfigurationPanel;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.JavaSdk;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ui.configuration.projectRoot.ProjectSdksModel;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.util.Condition;
import com.intellij.pom.java.LanguageLevel;
import com.intellij.ui.ColoredListCellRendererWrapper;
import com.intellij.util.ui.JBUI;
import consulo.roots.ui.configuration.SdkComboBox;
import consulo.util.ui.components.VerticalLayoutPanel;

/**
* @author VISTALL
* @since 25.08.2015
*/
public class JavaConfigurationPanel implements IdeaModuleTypeConfigurationPanel
{
	private SdkComboBox mySdkComboBox;
	private ComboBox myLanguageLevelBox;
	private IdeaProjectModel myIdeaProjectModel;

	public JavaConfigurationPanel(Project project, IdeaProjectModel ideaProjectModel)
	{
		myIdeaProjectModel = ideaProjectModel;
	}

	@NotNull
	@Override
	public JComponent getComponent()
	{
		ProjectSdksModel model = new ProjectSdksModel();
		model.reset();

		mySdkComboBox = new SdkComboBox(model, new Condition<SdkTypeId>()
		{
			@Override
			public boolean value(SdkTypeId sdkTypeId)
			{
				return sdkTypeId == JavaSdk.getInstance();
			}
		}, true);

		IdeaProjectRootModel projectRootModel = myIdeaProjectModel.getInstance(IdeaProjectRootModel.class);

		String jdkProperty = projectRootModel.getProperty("project-jdk-name");
		if(jdkProperty != null)
		{
			Sdk jdk = model.findSdk(jdkProperty);
			if(jdk == null)
			{
				mySdkComboBox.setInvalidSdk(jdkProperty);
			}
			else
			{
				mySdkComboBox.setSelectedSdk(jdk);
			}
		}

		myLanguageLevelBox = new ComboBox(LanguageLevel.values());
		myLanguageLevelBox.setSelectedItem(LanguageLevel.HIGHEST);
		myLanguageLevelBox.setRenderer(new ColoredListCellRendererWrapper<LanguageLevel>()
		{
			@Override
			protected void doCustomize(JList list,
					LanguageLevel value,
					int index,
					boolean selected,
					boolean hasFocus)
			{
				append(value.getShortText());
			}
		});

		String languageLevelProperty = projectRootModel.getProperty("languageLevel");
		if(languageLevelProperty != null)
		{
			try
			{
				LanguageLevel languageLevel = LanguageLevel.valueOf(languageLevelProperty);
				myLanguageLevelBox.setSelectedItem(languageLevel);

			}
			catch(IllegalArgumentException ignored)
			{
			}
		}

		VerticalLayoutPanel verticalLayoutPanel = JBUI.Panels.verticalPanel();
		verticalLayoutPanel.addComponent(LabeledComponent.left(mySdkComboBox, "Java Project SDK"));
		verticalLayoutPanel.addComponent(LabeledComponent.left(myLanguageLevelBox, "Java Language Level"));
		addOtherComponents(verticalLayoutPanel, model);
		return verticalLayoutPanel;
	}

	protected void addOtherComponents(VerticalLayoutPanel panel, ProjectSdksModel projectSdksModel)
	{
	}

	public SdkComboBox getSdkComboBox()
	{
		return mySdkComboBox;
	}

	public ComboBox getLanguageLevelBox()
	{
		return myLanguageLevelBox;
	}
}
