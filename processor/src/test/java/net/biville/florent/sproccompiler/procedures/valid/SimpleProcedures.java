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
package net.biville.florent.sproccompiler.procedures.valid;

import org.neo4j.procedure.Description;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.PerformsWrites;
import org.neo4j.procedure.Procedure;

public class SimpleProcedures
{

    @Procedure( deprecatedBy = "doSomething2" )
    @PerformsWrites
    public void doSomething( @Name( "foo" ) int foo )
    {

    }

    @Procedure( mode = Mode.SCHEMA )
    @Description( "Much better than the former version" )
    public void doSomething2( @Name( "bar" ) long bar )
    {

    }

    @Procedure( mode = Mode.SCHEMA )
    @Description( "Much better with records" )
    public void doSomething3( @Name( "bar" ) Records.LongWrapper bar )
    {

    }
}
