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

import java.io.File;

import consulo.idea.model.IdeaProjectModel;
import junit.framework.TestCase;

/**
 * @author VISTALL
 * @since 17:03/18.06.13
 */
public abstract class ModuleLoaderTestCase extends TestCase
{
	private IdeaProjectModel myIdeaProjectModel;

	@Override
	protected void runTest() throws Throwable
	{
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

		final File projectDir = new File(path, getName());
		final File ideaProjectDir = new File(projectDir, IdeaConstants.PROJECT_DIR);

		assertTrue(projectDir.exists());
		assertTrue(ideaProjectDir.exists());

		myIdeaProjectModel = new IdeaProjectModel(ideaProjectDir);

		super.runTest();
	}

	public IdeaProjectModel getIdeaProjectModel()
	{
		return myIdeaProjectModel;
	}
}
