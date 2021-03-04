final class Event
{
   // variables
   private Action action;
   private long time;
   private Entity entity;


   // constructor
   public Event(Action action, long time, Entity entity)
   {
      this.action = action;
      this.time = time;
      this.entity = entity;
   }


   // accessor methods
   public Action getAction() {
      return action;
   }

   public long getTime() {
      return time;
   }

   public Entity getEntity() {
      return entity;
   }
}
