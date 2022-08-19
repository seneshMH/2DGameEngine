package components;

import jade.Component;

public class SpriteRenderer extends Component {

    @Override
    public void start(){
        System.out.println("start");
    }

    @Override
    public void update(float dt) {
        System.out.println("update");
    }
}
