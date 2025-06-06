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

import consulo.logging.Logger;
import consulo.util.io.FileUtil;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 9:49/16.06.13
 */
public class IdeaProjectLibraryTableModel extends IdeaLibraryTableModel implements IdeaParseableModel {
    private static final Logger LOGGER = Logger.getInstance(IdeaProjectLibraryTableModel.class);

    private final List<IdeaLibraryModel> myLibraries = new ArrayList<IdeaLibraryModel>();

    @Override
    public void load(IdeaProjectModel ideaProjectModel, File ideaProjectDir) {
        try {
            File file = new File(ideaProjectDir, "libraries");
            if (!file.exists()) {
                return;
            }

            final FilenameFilter filter = (dir, name) -> FileUtil.getExtension(name).equalsIgnoreCase("xml");

            for (File child : file.listFiles(filter)) {
                final Document document = ideaProjectModel.loadDocument(child);

                final Element rootElement = document.getRootElement();
                final String attributeValue = rootElement.getAttributeValue("name");
                if ("libraryTable".equals(attributeValue)) {
                    final Element libraryElement = rootElement.getChild("library");
                    if (libraryElement != null) {
                        IdeaLibraryModel libraryModel = new IdeaLibraryModel();
                        libraryModel.load(ideaProjectModel, rootElement);
                        myLibraries.add(libraryModel);
                    }
                }
            }
        }
        catch (JDOMException | IOException e) {
            LOGGER.error(e);
        }
    }

    public List<IdeaLibraryModel> getLibraries() {
        return myLibraries;
    }
}
