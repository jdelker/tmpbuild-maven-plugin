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
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Goal to create a linked build directory.
 */
@Mojo(name = "link", defaultPhase = LifecyclePhase.INITIALIZE)
public class LinkTmpBuildMojo extends AbstractTmpBuildMojo {

  @Override
  public void execute() throws MojoExecutionException {

    // skip execution if no linkDirectory was provided
    if (linkDirectory == null) {
      getLog().debug("Not linking build directory");
      return;
    }

    // compute link target
    Path target = getTargetPath();

    if (!target.toFile().exists()) {
      getLog().info("Creating tmpbuild directory " + target);
      target.toFile().mkdirs();
    }

    // Check if build directory exists.
    // (This will accept any still existing link, pointing to an existent
    // directory)
    if (directory.exists()) {
      getLog().info("Nothing to do. Build directory already exists.");
      return;
    }

    try {
      // create parent dirs if necessary
      File parent = directory.getParentFile();
      if (!parent.exists()) {
        parent.mkdirs();
      }

      // create link
      getLog().info("Linking build directory to " + target);

      Files.createSymbolicLink(directory.toPath(), target);

    } catch (IOException ex) {
      getLog().error("Unable to link build directory to " + target);
    }

  }
}
