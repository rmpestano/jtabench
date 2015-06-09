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

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:struberg@yahoo.de">Mark Struberg</a>
 */
@WebServlet(urlPatterns = "/customer/*")
public class CustomerServlet extends HttpServlet{
    private static final int COUNT=1000;

    private @Inject CustomerEjb customerEjb;
    private @Inject CustomerCdiService customerCdiService;

    private static AtomicInteger badge = new AtomicInteger(0);
    private static Date dt = new Date();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String action = uri.substring(uri.lastIndexOf('/') + 1);

        resp.setContentType("text/plain");
        resp.setStatus(HttpServletResponse.SC_OK);

        if ("jta".equals(action)) {
            doJta(req, resp);
        }
        else if ("nonjta".equals(action)) {
            doNonJta(req, resp);
        }
        else if ("jtaInfo".equals(action)) {
            long customerCount = customerEjb.getCustomerCount();
            resp.getWriter().append("EJB Customers: " + customerCount);
        }
        else if ("nonjtaInfo".equals(action)) {
            long customerCount = customerCdiService.getCustomerCount();
            resp.getWriter().append("CDI Customers: " + customerCount);
        }
        else if ("reset".equals(action)) {
            doClean(req, resp);
        }
        else {
            resp.getWriter().append("add /jta, /nonjta, /jfaInfo, /nonjtaInfo or /reset to your URI!");
        }
    }


    private void doJta(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        for (int i=0; i<COUNT; i++) {
            customerEjb.createCustomers(dt, badge.incrementAndGet());
        }
        resp.getWriter().append("OK");
    }

    private void doNonJta(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        for (int i=0; i<COUNT; i++) {
            customerCdiService.addCustomer(dt, badge.incrementAndGet());
        }
        resp.getWriter().append("OK");
    }

    private void doClean(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        customerCdiService.cleanupDb();
        customerEjb.cleanupDb();
        resp.getWriter().append("OK");
    }


}
