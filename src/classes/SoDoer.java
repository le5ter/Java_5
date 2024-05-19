package classes;

/**
 * Класс {@code Doer} реализует интерфейс {@code Interface2} и предоставляет
 * функциональность для выполнения определенного действия, связанного с операцией "C".
 */
public class SoDoer implements SomeOtherInterface {

    /**
     * Выполняет операцию "C" и выводит результат.
     */
    @Override
    public void doSomeOther() {
        System.out.println("C");
    }
}