package classes;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Класс {@code Injector} отвечает за внедрение зависимостей в поля, помеченные аннотацией {@code AutoInjectable}.
 * <p>
 * Он использует рефлексию для анализа полей объекта, определяет те, которые помечены аннотацией {@code AutoInjectable},
 * и затем создает и внедряет экземпляры необходимых зависимостей во время выполнения.
 * </p>
 *
 * @see AutoInjectable
 */
public class Injector {

    /**
     * Внедряет зависимости в поля данного объекта.
     *
     * @param object объект, в который должны быть внедрены зависимости
     * @param <T>    тип объекта
     * @return объект с внедренными зависимостями
     */
    public <T> T inject(T object) {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(AutoInjectable.class)) {
                Class<?> fieldType = field.getType();
                Object instance = createInstance(fieldType);
                field.setAccessible(true);
                try {
                    field.set(object, instance);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return object;
    }

    /**
     * Создает экземпляр указанного класса, используя имя класса, полученное из файла свойств.
     *
     * @param clazz класс, для которого должен быть создан экземпляр
     * @return экземпляр указанного класса
     * @throws RuntimeException если экземпляр не может быть создан
     */
    private Object createInstance(Class<?> clazz) {
        String className = getClassNameFromProperties(clazz.getName());
        try {
            return Class.forName(className).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось создать экземпляр для " + clazz.getName());
        }
    }

    /**
     * Получает имя класса, соответствующее данному имени интерфейса или класса из файла свойств.
     *
     * @param interfaceName имя интерфейса или класса
     * @return полное квалифицированное имя класса
     * @throws RuntimeException если файл свойств не может быть загружен
     */
    private String getClassNameFromProperties(String interfaceName) {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("injector.properties")) {
            properties.load(input);
            return properties.getProperty(interfaceName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось загрузить файл свойств");
        }
    }
}