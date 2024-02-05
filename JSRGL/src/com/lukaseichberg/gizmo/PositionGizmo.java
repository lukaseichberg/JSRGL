package com.lukaseichberg.gizmo;

import com.lukaseichberg.maths.Mat4f;
import com.lukaseichberg.maths.Vec3f;
import com.lukaseichberg.maths.Vec4f;
import com.lukaseichberg.physics.Ray;
import com.lukaseichberg.renderer.Renderer;
import com.lukaseichberg.structs.Vertex;

public class PositionGizmo {

	private Vertex vX0, vX1, vY0, vY1, vZ0, vZ1;
	private boolean hoverX, hoverY, hoverZ;
	public Vec3f position;
	private boolean pressed;
	private Vec3f xOffset, yOffset, zOffset;
	
	public PositionGizmo(Vec3f position) {
		vX0 = new Vertex(0, 1, 0);
		vX0.pos = new Vec4f(0, 0, 0, 1);
		vX0.vec3[0] = new Vec3f(1, 0, 0);
		
		vX1 = new Vertex(0, 1, 0);
		vX1.pos = new Vec4f(1, 0, 0, 1);
		vX1.vec3[0] = new Vec3f(1, 0, 0);

		vY0 = new Vertex(0, 1, 0);
		vY0.pos = new Vec4f(0, 0, 0, 1);
		vY0.vec3[0] = new Vec3f(0, 1, 0);
		
		vY1 = new Vertex(0, 1, 0);
		vY1.pos = new Vec4f(0, 1, 0, 1);
		vY1.vec3[0] = new Vec3f(0, 1, 0);
		
		vZ0 = new Vertex(0, 1, 0);
		vZ0.pos = new Vec4f(0, 0, 0, 1);
		vZ0.vec3[0] = new Vec3f(0, 0, 1);
		
		vZ1 = new Vertex(0, 1, 0);
		vZ1.pos = new Vec4f(0, 0, 1, 1);
		vZ1.vec3[0] = new Vec3f(0, 0, 1);
		
		this.position = position;
	}
	
	public void render() {
		if (!pressed) {
			vX0.vec3[0] = hoverX ? new Vec3f(1, 1, 1) : new Vec3f(1, 0, 0);
			vY0.vec3[0] = hoverY ? new Vec3f(1, 1, 1) : new Vec3f(0, 1, 0);
			vZ0.vec3[0] = hoverZ ? new Vec3f(1, 1, 1) : new Vec3f(0, 0, 1);
			vX1.vec3[0] = hoverX ? new Vec3f(1, 1, 1) : new Vec3f(1, 0, 0);
			vY1.vec3[0] = hoverY ? new Vec3f(1, 1, 1) : new Vec3f(0, 1, 0);
			vZ1.vec3[0] = hoverZ ? new Vec3f(1, 1, 1) : new Vec3f(0, 0, 1);
			Renderer.renderLine(vX0, vX1);
			Renderer.renderLine(vY0, vY1);
			Renderer.renderLine(vZ0, vZ1);
		} else {
			if (hoverX) {
				Renderer.renderLine(vX0, vX1);
			}
			if (hoverY) {
				Renderer.renderLine(vY0, vY1);
			}
			if (hoverZ) {
				Renderer.renderLine(vZ0, vZ1);
			}
		}
	}
	
	public Mat4f getTransform() {
		return Mat4f.translate(position.x, position.y, position.z);
	}
	
	public void update(Ray ray, int mouseX, int mouseY) {
		Mat4f transform = getTransform();
		Vec3f pX0 = vX0.pos.mul(transform).vec3f();
		Vec3f pY0 = vY0.pos.mul(transform).vec3f();
		Vec3f pZ0 = vZ0.pos.mul(transform).vec3f();
		Vec3f pX1 = vX1.pos.mul(transform).vec3f();
		Vec3f pY1 = vY1.pos.mul(transform).vec3f();
		Vec3f pZ1 = vZ1.pos.mul(transform).vec3f();

		Ray lineX = new Ray(pX0, pX1.sub(pX0).normal());
		Ray lineY = new Ray(pY0, pY1.sub(pY0).normal());
		Ray lineZ = new Ray(pZ0, pZ1.sub(pZ0).normal());

		Vec3f[] pointsX = lineX.closestPoint(ray);
		Vec3f[] pointsY = lineY.closestPoint(ray);
		Vec3f[] pointsZ = lineZ.closestPoint(ray);

		if (!pressed) {
			float distX = Float.POSITIVE_INFINITY;
			float distY = Float.POSITIVE_INFINITY;
			float distZ = Float.POSITIVE_INFINITY;
			
			hoverX = false;
			hoverY = false;
			hoverZ = false;
			
			if (pointsX != null) {
				xOffset = pointsX[0].sub(lineX.origin);
				float onLine = xOffset.dot(lineX.dir);
				if (onLine >= 0 && onLine <= 1) {
					distX = pointsX[0].sub(pointsX[1]).mag();
				}
			}
			
			if (pointsY != null) {
				yOffset = pointsY[0].sub(lineY.origin);
				float onLine = yOffset.dot(lineY.dir);
				if (onLine >= 0 && onLine <= 1) {
					distY = pointsY[0].sub(pointsY[1]).mag();
				}
			}
			
			if (pointsZ != null) {
				zOffset = pointsZ[0].sub(lineZ.origin);
				float onLine = zOffset.dot(lineZ.dir);
				if (onLine >= 0 && onLine <= 1) {
					distZ = pointsZ[0].sub(pointsZ[1]).mag();
				}
			}
			
			float minDist = Math.min(distX, Math.min(distY, distZ));
			
			if (minDist <= 0.05f ) {
				if (minDist == distX) {
					hoverX = true;
				} else if (minDist == distY) {
					hoverY = true;
				} else if (minDist == distZ) {
					hoverZ = true;
				}
			}
		} else {
			if (hoverX) {
				position = pointsX[0].sub(xOffset);
			}

			if (hoverY) {
				position = pointsY[0].sub(yOffset);
			}

			if (hoverZ) {
				position = pointsZ[0].sub(zOffset);
			}
		}
	}
	
	public void press() {
		if (!pressed && (hoverX || hoverY || hoverZ)) {
			pressed = true;
		}
	}
	
	public void release() {
		if (pressed) {
			pressed = false;
		}
	}

}
