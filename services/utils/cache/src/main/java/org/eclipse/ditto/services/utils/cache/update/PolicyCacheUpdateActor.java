/*
 * Copyright (c) 2017-2018 Bosch Software Innovations GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/index.php
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.ditto.services.utils.cache.update;

import static java.util.Objects.requireNonNull;

import java.util.Collections;

import org.eclipse.ditto.model.enforcers.Enforcer;
import org.eclipse.ditto.model.policies.Policy;
import org.eclipse.ditto.services.models.caching.EntityId;
import org.eclipse.ditto.services.models.caching.Entry;
import org.eclipse.ditto.services.utils.cache.Cache;
import org.eclipse.ditto.signals.commands.policies.PolicyCommand;
import org.eclipse.ditto.signals.events.policies.PolicyEvent;

import akka.actor.ActorRef;
import akka.actor.Props;

/**
 * An actor which subscribes to Policy Events and updates caches when necessary.
 */
public class PolicyCacheUpdateActor extends AbstractPubSubListenerActor {

    /**
     * The name of this actor.
     */
    public static final String ACTOR_NAME = "policyCacheUpdater";

    private final Cache<EntityId, Entry<Policy>> policyCache;
    private final Cache<EntityId, Entry<Enforcer>> policyEnforcerCache;

    private PolicyCacheUpdateActor(
            final Cache<EntityId, Entry<Policy>> policyCache,
            final Cache<EntityId, Entry<Enforcer>> policyEnforcerCache,
            final ActorRef pubSubMediator, final String instanceIndex) {

        super(pubSubMediator, Collections.singleton(PolicyEvent.TYPE_PREFIX), instanceIndex);
        this.policyCache = requireNonNull(policyCache);
        this.policyEnforcerCache = requireNonNull(policyEnforcerCache);
    }

    /**
     * Create an Akka {@code Props} object for this actor.
     *
     * @param policyCache the policy cache.
     * @param policyEnforcerCache the policy-enforcer cache.
     * @param pubSubMediator Akka pub-sub mediator.
     * @param instanceIndex the index of this service instance.
     * @return Akka {@code Props} object.
     */
    public static Props props(
            final Cache<EntityId, Entry<Policy>> policyCache,
            final Cache<EntityId, Entry<Enforcer>> policyEnforcerCache,
            final ActorRef pubSubMediator, final String instanceIndex) {
        requireNonNull(policyEnforcerCache);
        requireNonNull(pubSubMediator);

        return Props.create(PolicyCacheUpdateActor.class,
                () -> new PolicyCacheUpdateActor(policyCache, policyEnforcerCache, pubSubMediator, instanceIndex));
    }

    @Override
    protected Receive handleEvents() {
        return receiveBuilder().match(PolicyEvent.class, this::handleEvent).build();
    }

    private void handleEvent(final PolicyEvent policyEvent) {
        final EntityId key = EntityId.of(PolicyCommand.RESOURCE_TYPE, policyEvent.getId());
        policyCache.invalidate(key);
        policyEnforcerCache.invalidate(key);
    }
}