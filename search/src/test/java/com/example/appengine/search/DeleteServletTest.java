/**
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.appengine.search;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.google.appengine.api.search.Document;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.stelinno.uddi.search.DeleteController;
import com.stelinno.uddi.search.test.AppConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class DeleteServletTest {
  private final LocalServiceTestHelper helper = new LocalServiceTestHelper();
  
  @Autowired
  private DeleteController servletUnderTest;

  @Before
  public void setUp() throws Exception {
    //MockitoAnnotations.initMocks(this);
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void doGet_successfulyInvoked() throws Exception {
    Document deletedDoc = servletUnderTest.delete();
    assertEquals("My Text", deletedDoc.getOnlyField("MyName").getText());
  }
}