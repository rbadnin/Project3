public class Animation implements Action{

        // variables
        private final AnimatedEntity entity;
        private final int repeatCount;


        // constructor
        public Animation(AnimatedEntity entity, int repeatCount)
        {
            this.entity = entity;
            this.repeatCount = repeatCount;
        }


        // methods from Action
        public void executeAction(EventScheduler scheduler)
        {
            entity.nextImage("left");

            if (repeatCount != 1)
            {
                scheduler.scheduleEvent(entity,
                        entity.createAnimationAction(Math.max(repeatCount - 1, 0)),
                        entity.getAnimationPeriod());
            }
        }


}


