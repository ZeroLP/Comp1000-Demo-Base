//This is where we declare our event prototypes.
public interface EventSubscriber
{
     void onAddObject(Objects.GameObject gObj, int currGameTime);
     void onDeleteObject(Objects.GameObject gObj, int currGameTime);
}

//This is where we handle, subscribe and raise our events.
//Take it as like a youtube channel sub and post notification
public class Events
{
    private List<EventSubscriber> subscribers = new ArrayList<EventSubscriber>();

    public void addSubscriber(EventSubscriber sub)
    {
        subscribers.add(sub);
    }

    public void invokeOnAddObject(Objects.GameObject gObj, int currGameTime)
    {
        //foreach(EventSubscriber sub in subscribers)
        for(EventSubscriber sub : subscribers)
            sub.onAddObject(gObj, currGameTime);
    }

    public void invokeOnDeleteObject(Objects.GameObject gObj, int currGameTime)
    {
         //foreach(EventSubscriber sub in subscribers)
        for(EventSubscriber sub : subscribers)
            sub.onDeleteObject(gObj, currGameTime);
    }
}