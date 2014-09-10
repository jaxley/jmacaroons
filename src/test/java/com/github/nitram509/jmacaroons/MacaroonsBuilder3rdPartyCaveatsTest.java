/*
 * Copyright 2014 Martin W. Kirst
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.nitram509.jmacaroons;

import org.testng.annotations.Test;

import static com.github.nitram509.jmacaroons.CaveatPacket.Type;
import static org.fest.assertions.Assertions.assertThat;

public class MacaroonsBuilder3rdPartyCaveatsTest {

  private Macaroon m;

  @Test
  public void add_third_party_caveat() {

    String secret = "this is a different super-secret key; never use the same secret twice";
    String publicIdentifier = "we used our other secret key";
    String location = "http://mybank/";

    String caveat_key = "4; guaranteed random by a fair toss of the dice";
    String predicate = "user = Alice";
    String identifier = "this was how we remind auth of key/pred";
    m = new MacaroonsBuilder(location, secret, publicIdentifier)
        .add_first_party_caveat("account = 3735928559")
        .add_third_party_caveat("http://auth.mybank/", caveat_key, identifier)
        .getMacaroon();

    assertThat(m.identifier).isEqualTo(m.identifier);
    assertThat(m.location).isEqualTo(m.location);
    assertThat(m.caveats).isEqualTo(new CaveatPacket[]{
        new CaveatPacket(Type.cid, "account = 3735928559"),
        new CaveatPacket(Type.cid, identifier),
        new CaveatPacket(Type.vid, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA027FAuBYhtHwJ58FX6UlVNFtFsGxQHS7uD/w/dedwv4Jjw7UorCREw5rXbRqIKhr"),
        new CaveatPacket(Type.cl, "http://auth.mybank/")
    });
    assertThat(m.signature).isEqualTo("6b99edb2ec6d7a4382071d7d41a0bf7dfa27d87d2f9fea86e330d7850ffda2b2");
  }

}