package uk.co.gosseyn.xanax.domain;

import lombok.Data;

@Data
public abstract class Zone {
    private Vector3d location;
    private Vector3d extent;
}
