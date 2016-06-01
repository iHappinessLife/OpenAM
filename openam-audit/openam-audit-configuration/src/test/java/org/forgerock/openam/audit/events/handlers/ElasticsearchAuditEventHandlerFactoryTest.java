/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * Copyright 2016 ForgeRock AS.
 */
package org.forgerock.openam.audit.events.handlers;

import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.forgerock.audit.AuditException;
import org.forgerock.audit.events.EventTopicsMetaData;
import org.forgerock.audit.events.EventTopicsMetaDataBuilder;
import org.forgerock.audit.events.handlers.AuditEventHandler;
import org.forgerock.audit.handlers.elasticsearch.ElasticsearchAuditEventHandler;
import org.forgerock.openam.audit.AuditEventHandlerFactory;
import org.forgerock.openam.audit.configuration.AuditEventHandlerConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test the ElasticsearchAuditEventHandlerFactory class.
 *
 * @since 13.5.0
 */
public class ElasticsearchAuditEventHandlerFactoryTest {

    private AuditEventHandlerFactory factory;
    private EventTopicsMetaData eventTopicsMetaData;
    private Map<String, Set<String>> configAttributes;

    @BeforeMethod
    public void setUp() {
        factory = new ElasticsearchAuditEventHandlerFactory();
        eventTopicsMetaData = EventTopicsMetaDataBuilder.coreTopicSchemas().build();

        configAttributes = new HashMap<>();
    }

    @Test
    private void canCreateElasticSearchAuditHandlerWithBareMinimumFields() throws AuditException {
        // Given
        configAttributes.put("enabled", singleton("true"));
        configAttributes.put("topics", singleton("access"));
        configAttributes.put("batchSize", singleton("500"));
        configAttributes.put("maxEvents", singleton("10000"));

        AuditEventHandlerConfiguration configuration = AuditEventHandlerConfiguration.builder()
                .withName("Elasticsearch Handler")
                .withAttributes(configAttributes)
                .withEventTopicsMetaData(eventTopicsMetaData).build();

        // When
        AuditEventHandler handler = factory.create(configuration);

        // Then
        assertThat(handler).isInstanceOf(ElasticsearchAuditEventHandler.class);
        assertThat(handler.getName()).isEqualTo("Elasticsearch Handler");
        assertThat(handler.isEnabled()).isTrue();
        assertThat(handler.getHandledTopics()).containsExactly("access");
    }
}
