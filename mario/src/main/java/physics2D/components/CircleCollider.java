package physics2D.components;

import components.Component;

public class CircleCollider extends Component {
    private float radius = 1.0f;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
