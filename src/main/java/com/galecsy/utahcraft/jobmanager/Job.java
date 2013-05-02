//
// Job.java
//
// Author: alecswan
//

package com.galecsy.utahcraft.jobmanager;

/**
 * This object describes a Job with a name and zero or one dependency on another Job.
 */
public class Job
{
    private final String name;
    private Job dependency;

    Job(String name, Job dependency) {
        this.name = name;
        this.dependency = dependency;
    }

    public String getName() {
        return name;
    }

    public Job getDependency() {
        return dependency;
    }

    public void setDependency(Job dependency) {
        this.dependency = dependency;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getFullDescription() {
        return name + " => " + dependency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Job job = (Job) o;

        if (name != null ? !name.equals(job.name) : job.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
