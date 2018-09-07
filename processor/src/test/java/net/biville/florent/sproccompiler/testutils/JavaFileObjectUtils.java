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
package net.biville.florent.sproccompiler.testutils;

import com.google.testing.compile.JavaFileObjects;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import javax.tools.JavaFileObject;

import static org.assertj.core.api.Assertions.assertThat;

public enum JavaFileObjectUtils
{
    INSTANCE;

    private final String baseDirectory;

    JavaFileObjectUtils()
    {
        Properties properties = loadProperties( "/procedures.properties" );
        baseDirectory = properties.getProperty( "base_directory" );
        assertThat( new File( baseDirectory ) ).exists();
    }

    public JavaFileObject procedureSource( String relativePath )
    {
        return JavaFileObjects.forResource( resolveUrl( relativePath ) );
    }

    private final Properties loadProperties( String name )
    {
        try ( InputStream paths = this.getClass().getResourceAsStream( name ) )
        {
            Properties properties = new Properties();
            properties.load( paths );
            return properties;
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }

    private URL resolveUrl( String relativePath )
    {
        try
        {
            return new File( baseDirectory, relativePath ).toURI().toURL();
        }
        catch ( MalformedURLException e )
        {
            throw new RuntimeException( e.getMessage(), e );
        }
    }
}
