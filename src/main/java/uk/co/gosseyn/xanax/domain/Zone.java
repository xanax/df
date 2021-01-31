package uk.co.gosseyn.xanax.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level= AccessLevel.PROTECTED)
public abstract class Zone {
    Vector3d location;
    Vector3d extent;
}
