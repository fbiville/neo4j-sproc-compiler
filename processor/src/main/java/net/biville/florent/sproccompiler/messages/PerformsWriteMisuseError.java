/*
 * Copyright 2016-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.biville.florent.sproccompiler.messages;

import javax.lang.model.element.Element;

public class PerformsWriteMisuseError implements CompilationMessage
{
    private final Element method;
    private final String errorMessage;

    public PerformsWriteMisuseError( Element method, String message, Object... args )
    {

        this.method = method;
        this.errorMessage = String.format( message, args );
    }

    @Override
    public Element getElement()
    {
        return method;
    }

    @Override
    public String getContents()
    {
        return errorMessage;
    }
}
