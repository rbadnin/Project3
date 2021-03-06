public interface Action {

    void executeAction(EventScheduler scheduler) throws InterruptedException;

}
