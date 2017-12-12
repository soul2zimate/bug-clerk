/*
 * JBoss, Home of Professional Open Source.
 * Copyright (c) 2016, Red Hat, Inc., and individual contributors
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

import static org.jboss.jbossset.bugclerk.checks.utils.AssertsHelper.assertResultsIsAsExpected;

import java.util.Collections;
import java.util.Optional;

import org.jboss.jbossset.bugclerk.AbstractCheckRunner;
import org.jboss.jbossset.bugclerk.Candidate;
import org.jboss.jbossset.bugclerk.MockUtils;
import org.jboss.jbossset.bugclerk.checks.utils.CollectionUtils;
import org.jboss.set.aphrodite.domain.Issue;
import org.jboss.set.aphrodite.domain.User;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test on check {@link IssueOnPayloadButNotAssigned}.
 * @author Romain Pelisse - belaran@redhat.com
 *
 */
public class IssueOnPayloadButNotAssigned extends AbstractCheckRunner {
    
    @Test
    public void testUserIsNotSetButReleasesNotEmpty() {
        final String bugId = "JBEAP-12345";
        final Issue mock = MockUtils.mockBzIssue(bugId, "summary");
        Mockito.when(mock.getReleases()).thenReturn(MockUtils.mockReleases("7.1.0"));
        Mockito.when(mock.getAssignee()).thenReturn(Optional.of(new User("","")));
        assertResultsIsAsExpected(engine.runCheckOnBugs(CollectionUtils.asSetOf(new Candidate(mock)), checkName), checkName, bugId, 1);
    }  
   
    @Test
    public void testNoMatchWhenUserIsSetWithEmailAndReleasesNotEmpty() {
        final String bugId = "JBEAP-12345";
        final Issue mock = MockUtils.mockBzIssue(bugId, "summary");
        Mockito.when(mock.getReleases()).thenReturn(MockUtils.mockReleases("7.2.0"));
        Mockito.when(mock.getAssignee()).thenReturn(Optional.of(User.createWithEmail("Romain Pelisse")));
        assertResultsIsAsExpected(engine.runCheckOnBugs(CollectionUtils.asSetOf(new Candidate(mock)), checkName), checkName, bugId, 0);
    }
    
    @Test
    public void testNoMatchWhenUserIsNotSetButNoReleasesEither() {
        final String bugId = "JBEAP-12345";
        final Issue mock = MockUtils.mockBzIssue(bugId, "summary");
        Mockito.when(mock.getReleases()).thenReturn(Collections.emptyList());    
        assertResultsIsAsExpected(engine.runCheckOnBugs(CollectionUtils.asSetOf(new Candidate(mock)), checkName), checkName, bugId, 0);
    }
}

