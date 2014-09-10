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

import java.io.Serializable;
import java.util.Arrays;

import static com.github.nitram509.jmacaroons.CaveatPacket.Type;
import static com.github.nitram509.jmacaroons.MacaroonsConstants.*;
import static com.github.nitram509.jmacaroons.util.Hex.toHex;

/**
 * <p>
 * Macaroons: Cookies with Contextual Caveats for Decentralized Authorization in the Cloud
 * </p>
 * This is an immutable and serializable object.
 * Use {@link com.github.nitram509.jmacaroons.MacaroonsBuilder} to modify it.
 * Use {@link com.github.nitram509.jmacaroons.MacaroonsVerifier} to verify it.
 *
 * @see <a href="http://research.google.com/pubs/pub41892.html">http://research.google.com/pubs/pub41892.html</a>
 */
public class Macaroon implements Serializable {

  public final String location;
  public final String identifier;
  public final String vid;
  public final String cl;
  public final String signature;
  public final CaveatPacket[] caveatPackets;

  final byte[] signatureBytes;

  Macaroon(String location, String identifier, byte[] signature) {
    this(location, identifier, new CaveatPacket[0], signature);
  }

  Macaroon(String location, String identifier, CaveatPacket[] caveats, byte[] signature) {
    this(location, identifier, caveats, null, null, signature);
  }

  Macaroon(String location, String identifier, CaveatPacket[] caveats, String vid, String cl, byte[] signature) {
    this.location = location;
    this.identifier = identifier;
    this.caveatPackets = caveats;
    this.signature = toHex(signature);
    this.signatureBytes = signature;
    this.vid = vid;
    this.cl = cl;
  }

  public String inspect() {
    return createLocationPacket(location)
        + createIdentifierPacket(identifier)
        + createCaveatsPackets(this.caveatPackets)
        + createVidPacket(vid)
        + createClPacket(vid)
        + createSignaturePacket(signature);
  }

  private String createLocationPacket(String location) {
    return createKeyValuePacket(Type.location, location);
  }

  private String createIdentifierPacket(String identifier) {
    return createKeyValuePacket(Type.identifier, identifier);
  }

  private String createCaveatsPackets(CaveatPacket[] caveats) {
    if (caveats == null) return "";
    StringBuilder sb = new StringBuilder();
    for (CaveatPacket packet : caveats) {
      sb.append(createKeyValuePacket(packet.type, packet.value));
    }
    return sb.toString();
  }

  private String createVidPacket(String value) {
    return createKeyValuePacket(Type.vid, value);
  }

  private String createClPacket(String value) {
    return createKeyValuePacket(Type.cl, value);
  }

  private String createSignaturePacket(String value) {
    return createKeyValuePacket(Type.signature, value);
  }

  private String createKeyValuePacket(Type type, String value) {
    return value != null ? type.name() + KEY_VALUE_SEPARATOR + value + LINE_SEPARATOR : "";
  }

  public String serialize() {
    return MacaroonsSerializer.serialize(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Macaroon macaroon = (Macaroon) o;

    if (!Arrays.equals(caveatPackets, macaroon.caveatPackets)) return false;
    if (cl != null ? !cl.equals(macaroon.cl) : macaroon.cl != null) return false;
    if (identifier != null ? !identifier.equals(macaroon.identifier) : macaroon.identifier != null) return false;
    if (location != null ? !location.equals(macaroon.location) : macaroon.location != null) return false;
    if (signature != null ? !signature.equals(macaroon.signature) : macaroon.signature != null) return false;
    if (vid != null ? !vid.equals(macaroon.vid) : macaroon.vid != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = location != null ? location.hashCode() : 0;
    result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
    result = 31 * result + (vid != null ? vid.hashCode() : 0);
    result = 31 * result + (cl != null ? cl.hashCode() : 0);
    result = 31 * result + (signature != null ? signature.hashCode() : 0);
    result = 31 * result + (caveatPackets != null ? Arrays.hashCode(caveatPackets) : 0);
    return result;
  }
}
