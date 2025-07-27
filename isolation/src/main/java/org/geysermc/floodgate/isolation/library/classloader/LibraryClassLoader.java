/*
 * Copyright (c) 2019-2025 GeyserMC
 * Licensed under the MIT license
 * @link https://github.com/GeyserMC/Floodgate
 */
package org.geysermc.floodgate.isolation.library.classloader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LibraryClassLoader extends URLClassLoader {
    private final Set<Path> loadedPaths = new HashSet<>();

    public LibraryClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }

    public void addPath(Path path) {
        try {
            addURL(path.toUri().toURL());
            loadedPaths.add(path);
        } catch (MalformedURLException exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    public Set<Path> loadedPaths() {
        return Collections.unmodifiableSet(loadedPaths);
    }

    public boolean isLoaded(Path path) {
        return loadedPaths.contains(path);
    }

    static {
        ClassLoader.registerAsParallelCapable();
    }
}
