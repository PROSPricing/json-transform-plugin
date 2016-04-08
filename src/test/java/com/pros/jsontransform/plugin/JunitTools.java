/*
 * Copyright (c) 2016 PROS, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package com.pros.jsontransform.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class JunitTools
{
    public static void setUpBeforeClass()
    {
        Logger rootLogger = Logger.getRootLogger();
        rootLogger.setLevel(Level.INFO);
        rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
    }

    public static String readFile(String path) throws IOException
    {
        List<String> lines = Files.readAllLines(Paths.get(path), Charset.forName("UTF-8"));
        return String.join("\n", lines);
    }

    public static String getProjectHome() throws IOException
    {
        File currentFolder = new File(".");
        return currentFolder.getAbsolutePath();
    }

    public static String getPathToPlugins() throws IOException
    {
        String pathToPlugins = System.getProperty("testPathToPlugins");
        if (pathToPlugins == null)
        {
            pathToPlugins = getProjectHome() + "/gradle/build/libs";
        }
        return pathToPlugins;
    }

    public static String getPathToExamples() throws IOException
    {
        String pathToExamples = System.getProperty("testPathToExamples");
        if (pathToExamples == null)
        {
            pathToExamples = getProjectHome() + "/src/test/java/com/pros/jsontransform/plugin";
        }
        return pathToExamples;
    }
}
