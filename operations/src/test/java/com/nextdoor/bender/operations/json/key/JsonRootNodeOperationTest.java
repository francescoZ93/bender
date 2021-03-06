/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright 2017 Nextdoor.com, Inc
 *
 */

package com.nextdoor.bender.operations.json.key;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.junit.Test;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.nextdoor.bender.InternalEvent;
import com.nextdoor.bender.deserializer.json.GenericJsonEvent;
import com.nextdoor.bender.operation.OperationException;
import com.nextdoor.bender.operation.json.key.JsonRootNodeOperation;
import com.nextdoor.bender.operation.json.key.JsonRootNodeOperationFactory;
import com.nextdoor.bender.operations.json.OperationTest;
import com.nextdoor.bender.testutils.DummyDeserializerHelper.DummpyEvent;

public class JsonRootNodeOperationTest extends OperationTest {
  @Test
  public void testNewRootNode()
      throws JsonSyntaxException, UnsupportedEncodingException, IOException {
    JsonParser parser = new JsonParser();
    JsonElement input = parser.parse(getResourceString("basic_input.json"));

    GenericJsonEvent devent = new GenericJsonEvent(input.getAsJsonObject());

    JsonRootNodeOperationFactory f = new JsonRootNodeOperationFactory();
    JsonRootNodeOperation operation = new JsonRootNodeOperation("$.i.ia");

    InternalEvent ievent = new InternalEvent("", null, 0);
    ievent.setEventObj(devent);
    operation.perform(ievent);

    assertEquals("{\"iaa\":\"bar\"}", devent.getPayload().toString());
  }

  @Test(expected = OperationException.class)
  public void testNotAnObject()
      throws JsonSyntaxException, UnsupportedEncodingException, IOException {
    JsonParser parser = new JsonParser();
    JsonElement input = parser.parse(getResourceString("basic_input.json"));

    DummpyEvent devent = new DummpyEvent();
    devent.payload = input.getAsJsonObject();

    JsonRootNodeOperationFactory f = new JsonRootNodeOperationFactory();
    JsonRootNodeOperation operation = new JsonRootNodeOperation("$.h.ha");

    InternalEvent ievent = new InternalEvent("", null, 0);
    ievent.setEventObj(devent);
    operation.perform(ievent);
  }

  @Test(expected = OperationException.class)
  public void testMissingField()
      throws JsonSyntaxException, UnsupportedEncodingException, IOException {
    JsonParser parser = new JsonParser();
    JsonElement input = parser.parse(getResourceString("basic_input.json"));

    DummpyEvent devent = new DummpyEvent();
    devent.payload = input.getAsJsonObject();

    JsonRootNodeOperationFactory f = new JsonRootNodeOperationFactory();
    JsonRootNodeOperation operation = new JsonRootNodeOperation("$.z");

    InternalEvent ievent = new InternalEvent("", null, 0);
    ievent.setEventObj(devent);
    operation.perform(ievent);
  }
}
