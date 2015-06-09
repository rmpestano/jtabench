/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apacheextras.eebench.jtabench.jta;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:struberg@yahoo.de">Mark Struberg</a>
 */
@ApplicationScoped
public class EntityManagerProducer
{
    private EntityManagerFactory emf;

    @PostConstruct
    public void init() {
        Map<String, String> emfProps = new HashMap<String, String>();
        emfProps.put("javax.persistence.jdbc.driver", "org.h2.Driver");
        emfProps.put("javax.persistence.jdbc.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        emfProps.put("javax.persistence.jdbc.username", "sa");
        emfProps.put("javax.persistence.jdbc.password", "");

        emf = Persistence.createEntityManagerFactory("nonjtaPu", emfProps);
    }

    @Produces
    @RequestScoped
    public EntityManager createEm() {
        return emf.createEntityManager();
    }

    public void cleanupEm(@Disposes EntityManager em) {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    @PreDestroy
    public void shutdownEmf() {
        if (emf != null && emf.isOpen())
        {
            emf.close();
        }
    }
}
