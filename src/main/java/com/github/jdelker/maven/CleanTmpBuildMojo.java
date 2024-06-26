package com.github.jdelker.maven;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
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
import java.io.File;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Goal to unlink build directory.
 */
@Mojo(name = "clean", defaultPhase = LifecyclePhase.CLEAN)
public class CleanTmpBuildMojo extends AbstractTmpBuildMojo {

  @Override
  public void execute() throws MojoExecutionException {

    try {
      // remove symbolic link
      if (Files.isSymbolicLink(directory.toPath())) {
        getLog().info("Removing build directory link " + directory);
        Files.delete(directory.toPath());
      }

      // make sure, that the linkDirectory is removed also
      Path targetPath = getTargetPath();
      if (targetPath != null && Files.isDirectory(targetPath)) {
        getLog().info("Purging build directory "+ targetPath);
        Files.walk(targetPath)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
      }

    } catch (IOException ex) {
      getLog().error("Unable to unlink build directory " + directory, ex);
    }

  }
}
