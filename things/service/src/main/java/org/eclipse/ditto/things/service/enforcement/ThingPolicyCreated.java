/*
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.ditto.things.service.enforcement;


import org.eclipse.ditto.base.model.headers.DittoHeaders;
import org.eclipse.ditto.policies.model.PolicyId;
import org.eclipse.ditto.things.model.ThingId;


public record ThingPolicyCreated(ThingId thingId, PolicyId policyId, DittoHeaders dittoHeaders) {

    public static ThingPolicyCreated of(final ThingId thingId, final PolicyId policyId, final DittoHeaders dittoHeaders) {
        return new ThingPolicyCreated(thingId, policyId, dittoHeaders);
    }

}
