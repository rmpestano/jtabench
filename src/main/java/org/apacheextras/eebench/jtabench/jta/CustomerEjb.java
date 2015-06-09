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

import java.util.Date;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * @author <a href="mailto:struberg@yahoo.de">Mark Struberg</a>
 */
@Stateless
@Named
public class CustomerEjb {

    @PersistenceContext(unitName = "jtaPu")
    private EntityManager em;

    public void createCustomers(Date dt, int badge) {
        Customer c = new Customer();
        c.setFirstName("firstname");
        c.setLastName("" + badge + "_" + "lastname");
        c.setBirthdate(dt);
        c.setChildren(1);
        em.persist(c);
    }

    public void cleanupDb() {
        em.createQuery("DELETE from Customer").executeUpdate();
    }

    public long getCustomerCount() {
        TypedQuery<Long> query = em.createQuery("select count(c) from Customer AS c", Long.class);
        Long count = query.getSingleResult();
        return count;
    }
}
