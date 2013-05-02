//
// JobManagerTest.java
//
// Author: alecswan
//

package com.galecsy.utahcraft.jobmanager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class JobManagerTest
{
    private JobManager manager;

    @Before
    public void before_test() {
        manager = new JobManager();
    }

    @After
    public void after_test() {
        try {
            System.out.println(manager.jobsToString());
        } catch (CyclicDependencyException e) {
            System.out.println(e);
        }
    }

    private Job createJob(String a) {
        return manager.createJob(a, null);
    }

    private Job createJob(String a, Job dep) {
        return manager.createJob(a, dep);
    }

    @Test
    public void test_empty() throws IOException {
        String def = "";
        assertThat(manager.parse(def), equalTo(Collections.EMPTY_LIST));
        assertThat(manager.jobsToString(), equalTo(""));
    }

    @Test
    public void test_single() throws IOException {
        String def = "a";
        assertThat(manager.parse(def), equalTo(asList(createJob("a", null))));
        assertThat(manager.jobsToString(), equalTo("a"));
    }

    @Test
    public void test_multiple() throws IOException {
        assertThat(manager.parse("a =>\n b => c\nc =>"),
            equalTo(asList(
                createJob("a", null),
                createJob("b", createJob("c")),
                createJob("c"))));
        assertThat(manager.jobsToString(), equalTo("acb"));
    }

    @Test
    public void test_multiple_complex() throws IOException {
        assertThat(manager.parse("a =>\n b => c\nc => f\nd => a\ne => b\nf =>"),
            equalTo(asList(
                createJob("a", null),
                createJob("b", createJob("c")),
                createJob("c", createJob("f")),
                createJob("d", createJob("a")),
                createJob("e", createJob("b")),
                createJob("f", null)))
        );
        assertThat(manager.jobsToString(), equalTo("afdcbe"));
    }

    @Test(expected = CyclicDependencyException.class)
    public void test_multiple_detectCycle() throws IOException {
        assertThat(manager.parse("f =>\na =>\n b => c\nc => a"),
            equalTo(asList(
                createJob("f"),
                createJob("a", createJob("b")),
                createJob("b", createJob("c")),
                createJob("c", createJob("a"))))
        );
        assertThat(manager.jobsToString(), equalTo(""));
    }
}
