package io.github.dmhacker.rendering.kdtrees;

import java.util.ArrayList;
import java.util.List;

import io.github.dmhacker.rendering.objects.Object3d;
import io.github.dmhacker.rendering.vectors.Vec3d;

public class KDNode {
	private static final int MAX_BUCKET_CAPACITY = 10;
	private static final int MAX_BUCKET_VARIANCE = 3;
	
	private KDNode left;
	private KDNode right;
	private BoundingBox boundingBox;
	private List<Object3d> contained;
	
	public KDNode() {
		this(null, null, new ArrayList<>());
	}
	
	public KDNode(KDNode left, KDNode right, List<Object3d> objects) {
		this.left = left;
		this.right = right;
		this.contained = objects;
		
		if (!objects.isEmpty()) {
			this.boundingBox = BoundingBox.fromObjects(objects);
		}
	}
	
	public KDNode getLeft() {
		return left;
	}
	
	public KDNode getRight() {
		return right;
	}
	
	public void setLeft(KDNode node) {
		this.left = node;
	}
	
	public void setRight(KDNode node) {
		this.right = node;
	}
	
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}
	
	public List<Object3d> getObjects() {
		return contained;
	}
	
	public boolean isLeaf() {
		return left == null && right == null;
	}
	
	public String toString() {
		if (isLeaf()) {
			return contained.size()+"";
		}
		return "["+left+"|"+right+"]";
	}
	
	public static KDNode build(List<Object3d> objects, int depth) {
		KDNode node = new KDNode(null, null, objects);
		
		if (objects.size() < 2) {
			return node;
		}
		
		Vec3d midpoint = new Vec3d(0, 0, 0);
		for (Object3d obj : objects) {
			midpoint = midpoint.add(obj.getCenter());
		}
		midpoint = midpoint.divide(objects.size());
		
		int axis = depth % 3; // node.getBoundingBox().getLongestAxis();
		List<Object3d> leftmost = new ArrayList<Object3d>();
		List<Object3d> rightmost = new ArrayList<Object3d>();
		for (Object3d obj : objects) {
			Vec3d objMidpoint = obj.getCenter();
			if (axis == 0) {
				if (objMidpoint.getX() < midpoint.getX()) {
					leftmost.add(obj);
				}
				else {
					rightmost.add(obj);
				}
			}
			if (axis == 1) {
				if (objMidpoint.getY() < midpoint.getY()) {
					leftmost.add(obj);
				}
				else {
					rightmost.add(obj);
				}
			}
			if (axis == 2) {
				if (objMidpoint.getZ() < midpoint.getZ()) {
					leftmost.add(obj);
				}
				else {
					rightmost.add(obj);
				}
			}
		}
		
		if ((int) Math.abs(leftmost.size() - rightmost.size()) < MAX_BUCKET_VARIANCE && leftmost.size() <= MAX_BUCKET_CAPACITY && rightmost.size() <= MAX_BUCKET_CAPACITY) {
			node.setLeft(new KDNode(null, null, leftmost));
			node.setRight(new KDNode(null, null, rightmost));
		}
		else {
			node.setLeft(build(leftmost, depth + 1));
			node.setRight(build(rightmost, depth + 1));
		}
		
		return node;
	}
}
