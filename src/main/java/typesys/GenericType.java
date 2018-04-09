package typesys;

import java.util.ArrayList;


/**
 *
 */
public class GenericType {
    public void main(String arg[]) {
        ArrayList<Human> humans = new ArrayList<Human>();

        humans.add(new Student());

        JFoo foo = new JFoo<Human>();

    }
}
