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

package consulo.idea.impl.java;

import com.intellij.java.language.LanguageLevel;
import com.intellij.java.language.projectRoots.JavaSdkType;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkModel;
import consulo.ide.setting.ShowSettingsUtil;
import consulo.idea.model.IdeaProjectModel;
import consulo.idea.model.IdeaProjectRootModel;
import consulo.idea.util.IdeaModuleTypeConfigurationPanel;
import consulo.module.ui.awt.SdkComboBox;
import consulo.ui.ex.awt.*;

import jakarta.annotation.Nonnull;
import javax.swing.*;

/**
 * @author VISTALL
 * @since 25.08.2015
 */
public class JavaConfigurationPanel implements IdeaModuleTypeConfigurationPanel
{
	private SdkComboBox mySdkComboBox;
	private ComboBox<LanguageLevel> myLanguageLevelBox;
	private IdeaProjectModel myIdeaProjectModel;

	public JavaConfigurationPanel(IdeaProjectModel ideaProjectModel)
	{
		myIdeaProjectModel = ideaProjectModel;
	}

	@Nonnull
	@Override
	public JComponent getComponent()
	{
		SdkModel model = ShowSettingsUtil.getInstance().getSdksModel();

		mySdkComboBox = new SdkComboBox(model, it -> it instanceof JavaSdkType, true);

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

		myLanguageLevelBox = new ComboBox<>(LanguageLevel.values());
		myLanguageLevelBox.setSelectedItem(LanguageLevel.HIGHEST);
		myLanguageLevelBox.setRenderer(new ColoredListCellRenderer<LanguageLevel>()
		{
			@Override
			protected void customizeCellRenderer(@Nonnull JList jList, LanguageLevel value, int i, boolean b, boolean b1)
			{
				append(value.getDescription().get());
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
		verticalLayoutPanel.addComponent(LabeledComponent.create(mySdkComboBox, "Java Project SDK"));
		verticalLayoutPanel.addComponent(LabeledComponent.create(myLanguageLevelBox, "Java Language Level"));
		addOtherComponents(verticalLayoutPanel, model);
		return verticalLayoutPanel;
	}

	protected void addOtherComponents(VerticalLayoutPanel panel, SdkModel projectSdksModel)
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
