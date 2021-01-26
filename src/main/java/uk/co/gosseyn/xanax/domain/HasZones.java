package uk.co.gosseyn.xanax.domain;

import java.util.Collection;

public interface HasZones {
    Collection<Zone> getZones();
    void setZones(Collection<Zone> zones);
}
