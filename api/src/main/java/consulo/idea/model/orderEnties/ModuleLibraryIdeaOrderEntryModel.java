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
package consulo.idea.model.orderEnties;

import consulo.idea.model.IdeaLibraryModel;

/**
 * @author VISTALL
 * @since 10:17/16.06.13
 */
public class ModuleLibraryIdeaOrderEntryModel extends IdeaOrderEntryModel {
    private final IdeaLibraryModel myLibraryModel;

    public ModuleLibraryIdeaOrderEntryModel(IdeaLibraryModel libraryModel) {
        myLibraryModel = libraryModel;
    }

    public IdeaLibraryModel getLibraryModel() {
        return myLibraryModel;
    }
}
