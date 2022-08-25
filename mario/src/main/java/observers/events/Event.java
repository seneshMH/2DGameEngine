package observers.events;

import observers.EventSystem;

public class Event {
    public EventType type;

    public Event(EventType type){
        this.type = type;
    }

    public Event(){
        this.type = EventType.UserEvent;
    }
}
