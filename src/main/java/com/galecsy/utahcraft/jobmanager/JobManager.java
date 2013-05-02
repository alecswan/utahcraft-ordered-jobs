//
// JobManager.java
//
// Author: alecswan
//

package com.galecsy.utahcraft.jobmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * This object provides methods for managing Jobs.
 */
public class JobManager
{
    private final Set<Job> jobs = new LinkedHashSet<Job>();

    public JobManager() {

    }

    public Job createJob(String name) {
        return createJob(name, null);
    }

    public Job createJob(String name, Job dep) {
        Job foundJob = null;
        for (Job job : jobs) {
            if (job.getName().equals(name)) {
                foundJob = job;
                break;
            }
        }
        if (foundJob == null) {
            foundJob = new Job(name, dep);
            jobs.add(foundJob);
        }

        if (dep != null) {
            foundJob.setDependency(dep);
        }

        return foundJob;
    }

    /**
     * Parses the given string into a list of jobs. Each line in the string contains job name with an optional dependency as follows:
     * JobName => (JobName)?
     *
     * @param s - string to parse
     * @return - list of jobs
     * @throws IOException
     */
    List<Job> parse(String s) throws IOException {
        List<Job> jobs = new ArrayList<Job>();
        BufferedReader reader = new BufferedReader(new StringReader(s.trim()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] split = line.split("=>");
            String jobName = split[0].trim();
            Job jobDep = null;
            if (split.length > 1) {
                jobDep = createJob(split[1].trim());
            }

            jobs.add(createJob(jobName, jobDep));
        }

        return jobs;
    }

    /**
     * Returns a string representation of all known job in the order where each job is printed after all its dependencies.
     *
     * @return serialized job names
     * @throws CyclicDependencyException if a cyclic dependency has been detected
     */
    public String jobsToString() {
        String str = "";
        List<Job> printed = new ArrayList<Job>();
        while (printed.size() < jobs.size()) {
            int numOfPrinted = printed.size();
            for (Job job : jobs) {
                if (!printed.contains(job)  && (job.getDependency() == null || printed.contains(job.getDependency()))) {
                    str += job.getName();
                    printed.add(job);
                }
            }

            if (numOfPrinted == printed.size()) {
                List<Job> cyclicJobs = new ArrayList<Job>(jobs);
                cyclicJobs.removeAll(printed);
                throw new CyclicDependencyException("Detected a cycle.", cyclicJobs);
            }
        }
        return str;
    }
}
