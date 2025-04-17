package net.swofty.anticheat.world;

import lombok.Getter;

@Getter
public class BoundingBox {
    private float minX, minY, minZ;
    private float maxX, maxY, maxZ;

    public BoundingBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }
}
