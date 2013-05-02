//
// CyclicDependencyException.java
//
// Author: alecswan
//

package com.galecsy.utahcraft.jobmanager;

import java.util.List;

/**
 * Thrown when a cyclic dependency is detected between a set of jobs.
 */
public class CyclicDependencyException extends RuntimeException
{
    private final List<Job> jobs;

    /**
     * Constructs a new {@code CyclicDependencyException} with the specified detail message.
     *
     * @param message the detail message
     * @param jobs    a list of jobs which have cyclic dependency
     */
    public CyclicDependencyException(String message, List<Job> jobs) {
        super(message);
        this.jobs = jobs;
    }

    @Override
    public String toString() {
        String str = null;
        for (Job job : jobs) {
            str = str == null ? job.getFullDescription() : str + ", " + job.getFullDescription();
        }
        return "Cycle detected between jobs: " + str;
    }
}
