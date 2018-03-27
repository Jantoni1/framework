/*
 * Copyright 2000-2018 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.server;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import com.vaadin.server.VaadinPortlet.VaadinGateInRequest;

public class VaadinGateInRequestTest
        extends VaadinHttpAndPortletRequestTestBase<VaadinGateInRequest> {

    @Override
    protected VaadinGateInRequest createSut() {

        VaadinGateInRequest request = new VaadinGateInRequest(portletRequest,
                vaadinPortletService);

        // Although partial mocking can be considered a code smell,
        // here it's actually quite useful to mock reflection calls.
        VaadinGateInRequest spy = spy(request);
        doReturn(servletRequest).when(spy).getServletRequest(portletRequest);

        return spy;
    }
}
