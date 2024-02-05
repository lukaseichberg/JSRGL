package com.lukaseichberg.physics;

import com.lukaseichberg.maths.Vec3f;

public class LineSegment {
	
	public Vec3f start, end;
	
	float coPlanerThreshold = 0.3f; // Some threshold value that is application dependent
	float lengthErrorThreshold = 1e-3f;
	
	public LineSegment(Vec3f start, Vec3f end) {
		this.start = start;
		this.end = end;
	}
	
	public boolean intersection(Ray ray) {
		Vec3f da = ray.dir;
		Vec3f db = end.sub(start);
		Vec3f dc = start.sub(ray.origin);
		
		if (Math.abs(dc.dot(da.cross(db))) >= coPlanerThreshold) // Lines are not coplanar
			return false;
			
		float s = dc.cross(db).dot(da.cross(db)) / da.cross(db).sqLen();

		if (s >= 0.0 && s <= 1.0)	// Means we have an intersection
		{
			Vec3f intersection = ray.origin.add(da.mul(s));
			
			// See if this lies on the segment
			if (intersection.sub(start).sqLen() + intersection.sub(end).sqLen() <= sqLen() + lengthErrorThreshold)
				return true;
		}

		return false;
	}
	
	public float sqLen() {
		Vec3f d = end.sub(start);
		return d.sqLen();
	}

}
