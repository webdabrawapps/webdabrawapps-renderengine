package io.github.dmhacker.rendering.vectors;

import io.github.dmhacker.rendering.Constants;

// Immutable
public class Vec3d {
	private final double x;
	private final double y;
	private final double z;
	
	public Vec3d() {
		this(0, 0, 0);
	}
	
	public Vec3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3d add(double dx, double dy, double dz) {
		return new Vec3d(x + dx, y + dy, z + dz);
	}
	
	public Vec3d add(Vec3d vec) {
		return new Vec3d(x + vec.x, y + vec.y, z + vec.z);
	}
	
	public Vec3d subtract(Vec3d vec) {
		return new Vec3d(x - vec.x, y - vec.y, z - vec.z);
	}
	
	public Vec3d negative() {
		return new Vec3d(-x, -y, -z);
	}
	
	public Vec3d multiply(double scalar) {
		return new Vec3d(x * scalar, y * scalar, z * scalar);
	}
	
	public Vec3d divide(double scalar) {
		return new Vec3d(x / scalar, y / scalar, z / scalar);
	}
	
	public Vec3d normalize() {
		return divide(distance());
	}
	
	public double dotProduct(Vec3d vec) {
		return x * vec.x + y * vec.y + z * vec.z;
	}
	
	public Vec3d cross(Vec3d vec) {
		return new Vec3d(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x);
	}
	
	public double distance() {
		return Math.sqrt(distanceSquared());
	}
	
	// Using https://en.wikipedia.org/wiki/Rodrigues'_rotation_formula
	public Vec3d rotate(Vec3d axis, double angle) {
		if ((axis.distanceSquared() - 1) > Constants.EPSILON)
			axis = axis.normalize();
		return this.multiply(Math.cos(angle))
				.add(this.cross(axis).multiply(Math.sin(angle)))
				.add(axis.multiply(this.dotProduct(axis) * (1 - Math.cos(angle))));
	}
	
	public Vec3d rotateAroundOrigin(Vec3d origin, Vec3d axis, double angle) {
		Vec3d to = this.subtract(origin);
		return origin.add(to.rotate(axis, angle));
	}
	
	public double distanceSquared() {
		return x * x  + y * y + z * z;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
	
	@Override
	public int hashCode() {
		return new Double(x).hashCode() + new Double(y).hashCode() + new Double(z).hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o instanceof Vec3d) {
			Vec3d v = (Vec3d) o;
			return Math.abs(x - v.x) < Constants.EPSILON && Math.abs(y - v.y) < Constants.EPSILON && Math.abs(z - v.z) < Constants.EPSILON;
		}
		return false;
	}
	
	public String toString() {
		return "<"+x+", "+y+", "+z+">";
	}
}
