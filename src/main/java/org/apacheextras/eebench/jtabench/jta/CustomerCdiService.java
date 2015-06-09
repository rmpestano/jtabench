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

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;

/**
 * A few notes:
 * 1.) Usually I would not manually manage the transactions but use
 * a CDI interceptor. E.g. Apache DeltaSpike @Transactional
 * (I am even the original author of it). But this is just a sample
 * and I didn't want to pull in dependencies.
 *
 * 2. Although I wrote a producer method for the EM we do NOT use it.
 * Instead we manage the EM manually and close it for each iteration
 * to not have an unfair advantage over EJBs which close the EM after
 * each invocation as well. At the end we like to know the performance
 * overhead of JTA so we try to handle it as similar as possible.
 *
 * @author <a href="mailto:struberg@yahoo.de">Mark Struberg</a>
 */
public class CustomerCdiService {
    private @Inject EntityManagerProducer emP;

    public void addCustomer(Date dt, int badge) {
        EntityManager em = emP.createEm();
        try {
            em.getTransaction().begin();

            Customer c = new Customer();
            c.setFirstName("firstname");
            c.setLastName("" + badge + "_" + "lastname");
            c.setBirthdate(dt);
            c.setChildren(1);
            em.persist(c);

            em.getTransaction().commit();
        }
        catch (Exception e) {
            if (em.getTransaction().isActive()) {
                try {
                    em.getTransaction().rollback();
                }
                catch (Exception ignore) {
                    // ignore
                }
            }
            throw new RuntimeException(e);
        }
        finally {
            emP.cleanupEm(em);
        }
    }

    public void cleanupDb() {
        EntityManager em = emP.createEm();
        try {
            em.getTransaction().begin();

            em.createQuery("DELETE from Customer").executeUpdate();

            em.getTransaction().commit();
        }
        catch (Exception e) {
            if (em.getTransaction().isActive()) {
                try {
                    em.getTransaction().rollback();
                }
                catch (Exception ignore) {
                    // ignore
                }
            }
            throw new RuntimeException(e);
        }
        finally {
            emP.cleanupEm(em);
        }

    }

    public long getCustomerCount() {
        EntityManager em = emP.createEm();
        try {
            em.getTransaction().begin();

            TypedQuery<Long> query = em.createQuery("select count(c) from Customer AS c", Long.class);
            Long count = query.getSingleResult();

            em.getTransaction().commit();

            return count;
        }
        catch (Exception e) {
            if (em.getTransaction().isActive()) {
                try {
                    em.getTransaction().rollback();
                }
                catch (Exception ignore) {
                    // ignore
                }
            }
            throw new RuntimeException(e);
        }
        finally {
            emP.cleanupEm(em);
        }
    }
}
