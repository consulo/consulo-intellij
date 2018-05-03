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

import java.awt.BorderLayout;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.IdeBorderFactory;
import consulo.idea.util.IdeaModuleTypeConfigurationPanel;

/**
 * @author VISTALL
 * @since 25.08.2015
 */
public class IdeaModuleConfigurationStep extends ModuleWizardStep
{
	private Map<String, IdeaModuleTypeConfigurationPanel> myMap;

	public IdeaModuleConfigurationStep(Map<String, IdeaModuleTypeConfigurationPanel> map)
	{
		myMap = map;
	}

	@Override
	public JComponent getComponent()
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

	@Override
	public void updateDataModel()
	{
	}
}
