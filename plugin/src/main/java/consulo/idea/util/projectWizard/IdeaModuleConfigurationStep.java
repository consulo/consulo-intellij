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

package consulo.idea.util.projectWizard;

import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.IdeBorderFactory;
import consulo.disposer.Disposable;
import consulo.idea.util.IdeaModuleTypeConfigurationPanel;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.wizard.WizardStep;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * @author VISTALL
 * @since 25.08.2015
 */
public class IdeaModuleConfigurationStep implements WizardStep<IdeaImportContext>
{
	private Map<String, IdeaModuleTypeConfigurationPanel> myMap;

	public IdeaModuleConfigurationStep(Map<String, IdeaModuleTypeConfigurationPanel> map)
	{
		myMap = map;
	}

	@RequiredUIAccess
	@Nonnull
	@Override
	public consulo.ui.Component getComponent(@Nonnull Disposable uiDisposable)
	{
		throw new UnsupportedOperationException();
	}

	@RequiredUIAccess
	@Nonnull
	@Override
	public Component getSwingComponent(@Nonnull Disposable uiDisposable)
	{
		JPanel panel = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 0, true, false));
		for(Map.Entry<String, IdeaModuleTypeConfigurationPanel> entry : myMap.entrySet())
		{
			JPanel somePanel = new JPanel(new BorderLayout());
			somePanel.setBorder(IdeBorderFactory.createTitledBorder(entry.getKey()));
			somePanel.add(entry.getValue().getComponent(), BorderLayout.CENTER);

			panel.add(somePanel);
		}
		return panel;
	}
}
