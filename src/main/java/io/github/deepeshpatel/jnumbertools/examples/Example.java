/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples;

import java.util.Objects;

/**
 * Metadata and entry point of one runnable example.
 *
 * @param id      short unique id used on the command line, e.g. {@code quick-tour}
 * @param domain  grouping shown in the listing, e.g. {@code basics}
 * @param summary one-line description of the real-life problem solved
 * @param runner  prints the example's output to {@code System.out}
 */
public record Example(String id, String domain, String summary, Runnable runner) {

    public Example {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(domain, "domain");
        Objects.requireNonNull(summary, "summary");
        Objects.requireNonNull(runner, "runner");
        if (id.isBlank() || summary.isBlank()) {
            throw new IllegalArgumentException("id and summary must not be blank");
        }
    }
}
