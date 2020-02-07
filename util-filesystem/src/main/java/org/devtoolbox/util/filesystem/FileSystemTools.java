/*
 * MIT License
 *
 * Copyright © 2020 dev-toolbox.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.devtoolbox.util.filesystem;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;


/**
 * Utility class to handle nio filesystems and paths.
 *
 * @author Arnaud Lecollaire
 */
public class FileSystemTools {

    /**
     * Recursively find all paths matching the specified filter.
     *
     * @param filter the pattern to look for (see {@link FileSystem#getPathMatcher getPathMatcher})
     */
    public static Collection<? extends Path> findFilePaths(final FileSystem fileSystem, final Path rootPath, final String filter) throws IOException {
        final Collection<Path> paths = new ArrayList<>();
        Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
                final FileVisitResult result = super.preVisitDirectory(dir, attrs);
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, filter)) {
                    for (final Path path : stream) {
                        paths.add(path);
                    }
                }
                return result;
            }
        });
        return paths;
    }

    /**
     * Provides a temporary file to the consumer until its task is done, then delete it.
     *
     * @param consumer the temporary file consumer
     * @throws IOException if an I/O error occurs when trying to create/delete the temporary file, or while invoking the consumer
     */
    public static void doWithTemporaryFile(final String prefix, final String suffix, final Consumer<Path> consumer) throws IOException {
        final Path testFile = Files.createTempFile(prefix, suffix);
        try {
            consumer.accept(testFile);
        } finally {
            Files.delete(testFile);
        }
    }
}