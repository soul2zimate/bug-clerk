/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2015, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.jbossset.bugclerk.checks;

import static org.hamcrest.CoreMatchers.is;
import static org.jboss.jbossset.bugclerk.checks.utils.AssertsHelper.assertResultsIsAsExpected;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.jboss.jbossset.bugclerk.AbstractCheckRunner;
import org.jboss.jbossset.bugclerk.Candidate;
import org.jboss.jbossset.bugclerk.MockUtils;
import org.jboss.jbossset.bugclerk.checks.utils.CollectionUtils;
import org.jboss.set.aphrodite.domain.Issue;
import org.jboss.set.aphrodite.domain.IssueStatus;
import org.jboss.set.aphrodite.domain.IssueType;
import org.jboss.set.aphrodite.domain.Release;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class MilestonesSanityCheck extends AbstractCheckRunner {

    private String summary;
    private String bugId;

    @Before
    public void resetMockData() {
        bugId = "143794";
        summary = "A string containing 6.4.0 !";
    }

    @Test
    public void noViolationIfReleasesNotEmpty() {
        Issue mock = MockUtils.mockBug(bugId, summary);
        Mockito.when(mock.getReleases()).thenReturn(MockUtils.mockReleases("6.3.0", ""));
        Mockito.when(mock.getType()).thenReturn(IssueType.BUG);
        Mockito.when(mock.getStatus()).thenReturn(IssueStatus.POST);

        assertResultsIsAsExpected(engine.runCheckOnBugs(CollectionUtils.asSetOf(new Candidate(mock)), checkName), checkName, bugId, 0);
    }

    @Test
    public void noViolationIfNotModified() {
        Issue mock = MockUtils.mockBug(bugId, summary);
        Mockito.when(mock.getReleases()).thenReturn(new ArrayList<Release>(0));
        Mockito.when(mock.getType()).thenReturn(IssueType.BUG);
        Mockito.when(mock.getStatus()).thenReturn(IssueStatus.POST);
        
        assertResultsIsAsExpected(engine.runCheckOnBugs(CollectionUtils.asSetOf(new Candidate(mock)), checkName), checkName, bugId,0);
    }

    @Test
    public void noViolationIfTypeIsComponentUpgrade() {
        Issue mock = MockUtils.mockBug(bugId, summary);
        Mockito.when(mock.getReleases()).thenReturn(new ArrayList<Release>(0));
        Mockito.when(mock.getType()).thenReturn(IssueType.UPGRADE);
        Mockito.when(mock.getStatus()).thenReturn(IssueStatus.MODIFIED);

        assertResultsIsAsExpected(engine.runCheckOnBugs(CollectionUtils.asSetOf(new Candidate(mock)), checkName), checkName, bugId, 0);
    }

    @Test
    public void violation() {
        Issue mock = MockUtils.mockBug(bugId, summary);
        Mockito.when(mock.getReleases()).thenReturn(new ArrayList<Release>(0));
        Mockito.when(mock.getType()).thenReturn(IssueType.BUG);
        Mockito.when(mock.getStatus()).thenReturn(IssueStatus.MODIFIED);
        assertThat(engine.runCheckOnBugs(CollectionUtils.asSetOf(new Candidate(mock)), checkName).size(), is(1));
    }

    
}