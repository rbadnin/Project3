public class Activity implements Action{

        // variables
        private final ActionEntity entity;
        private final ImageStore imageStore;
        private final WorldModel world;


        // constructor
        public Activity(ActionEntity entity, WorldModel world, ImageStore imageStore)
        {
            this.entity = entity;
            this.world = world;
            this.imageStore = imageStore;
        }


        // methods from Action
        public void executeAction(EventScheduler scheduler) throws InterruptedException {
            entity.executeActivity(world, imageStore, scheduler);
        }

    }

