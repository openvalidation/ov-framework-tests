package functions;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bag.openvalidation.HUMLFramework;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;


public class FirstTests {
    private HUMLFramework huml = new HUMLFramework();

    private class Person
    {
        String name;
        int age;
        boolean married;

        Person(String name, int age, boolean married)
        {
            this.name = name;
            this.age = age;
            this.married = married;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return age == person.age &&
                    married == person.married &&
                    Objects.equals(name, person.name);
        }
    }

    //arrays
    @Test
    void first_with_string_array()
    {
        String[] input = {"Harry", "Hermione", "Ron"};
        String result = huml.FIRST(input);
        Assertions.assertEquals("Harry", result);
    }

    @Test
    void first_with_person_array_and_function()
    {
        Person p1 = new Person("Bob", 24, false);
        Person p2 = new Person("Ross", 42, true);

        Person[] input = {p1, p2};
        Function<Person, Boolean> function = p -> p.married;
        boolean result = huml.FIRST(input, function);
        Assertions.assertFalse(result);
    }

    @Test
    void first_with_person_array_and_amount()
    {
        Person p1 = new Person("Bob", 24, false);
        Person p2 = new Person("Ross", 42, true);
        Person p3 = new Person("Batman", 38, false);

        Person[] input = {p1, p2, p3};
        Person[] result = huml.FIRST(input, 2);
        Assertions.assertEquals(result[0], p1);
        Assertions.assertEquals(result[1], p2);
    }
    
    @Test
    void first_with_person_array_and_function_and_amount()
    {
        Person p1 = new Person("Bob", 24, false);
        Person p2 = new Person("Ross", 42, true);
        Person p3 = new Person("Batman", 38, false);

        Person[] input = {p1, p2, p3};
        Function<Person, Integer> function = p -> p.age;
        Integer[] result = huml.FIRST(input, function, 2);
        Assertions.assertEquals(result[0], 24);
        Assertions.assertEquals(result[1], 42);
    }
    //array edge cases
    @Test
    void first_with_person_array_and_amount_that_is_bigger_than_the_array_size()
    {
        Person p1 = new Person("Bob", 24, false);
        Person p2 = new Person("Ross", 42, true);
        Person p3 = new Person("Batman", 38, false);

        Person[] input = {p1, p2, p3};
        Person[] result = huml.FIRST(input, 4);
        Assertions.assertEquals(3, result.length);
    }


    @Test
    void first_with_person_array_and_amount_that_is_zero()
    {
        Person p1 = new Person("Bob", 24, false);
        Person p2 = new Person("Ross", 42, true);
        Person p3 = new Person("Batman", 38, false);

        Person[] input = {p1, p2, p3};
        Person[] result = huml.FIRST(input, 0);
        Assertions.assertNull(result);
    }

    @Test
    void first_with_person_array_and_amount_that_is_negative()
    {
        Person p1 = new Person("Bob", 24, false);
        Person p2 = new Person("Ross", 42, true);
        Person p3 = new Person("Batman", 38, false);

        Person[] input = {p1, p2, p3};
        Person[] result = huml.FIRST(input, -1);
        Assertions.assertNull(result);
    }

    //lists
    
    @Test
    void first_with_string_list()
    {
        List<String> input = new ArrayList<>(Arrays.asList("Harry", "Hermione", "Ron"));
        String result = huml.FIRST(input);
        Assertions.assertEquals("Harry", result);
    }

    @Test
    void first_with_person_list_and_function()
    {
        Person p1 = new Person("Bob", 24, false);
        Person p2 = new Person("Ross", 42, true);

        List<Person> input = new ArrayList<>(Arrays.asList(p1, p2));
        Function<Person, Boolean> function = p -> p.married;
        boolean result = huml.FIRST(input, function);
        Assertions.assertFalse(result);
    }

    @Test
    void first_with_person_list_and_amount()
    {
        Person p1 = new Person("Bob", 24, false);
        Person p2 = new Person("Ross", 42, true);
        Person p3 = new Person("Batman", 38, false);

        List<Person> input = new ArrayList<>(Arrays.asList(p1, p2, p3));
        Person[] result = huml.FIRST(input, 2);
        Assertions.assertEquals(result[0], p1);
        Assertions.assertEquals(result[1], p2);
    }

    @Test
    void first_with_person_list_and_function_and_amount()
    {
        Person p1 = new Person("Bob", 24, false);
        Person p2 = new Person("Ross", 42, true);
        Person p3 = new Person("Batman", 38, false);

        List<Person> input = new ArrayList<>(Arrays.asList(p1, p2, p3));
        Function<Person, Integer> function = p -> p.age;
        Integer[] result = huml.FIRST(input, function, 2);
        Assertions.assertEquals(result[0], 24);
        Assertions.assertEquals(result[1], 42);
    }
    
    @Test
    void first_with_int_list()
    {
        List<Integer> input = new ArrayList<>();
        input.add(1);
        input.add(2);
        input.add(3);
        
        Integer[] res = huml.FIRST(input, 2);
        
        Assertions.assertEquals(res.length, 2);
        Assertions.assertEquals(res[0], 1);
        Assertions.assertEquals(res[1], 2);
    }

    @Test
    void first_with_int_array()
    {
        Integer[] input = new Integer[]{1,2,3};

        Integer[] res = huml.FIRST(input, 2);

        Assertions.assertEquals(res.length, 2);
        Assertions.assertEquals(res[0], 1);
        Assertions.assertEquals(res[1], 2);
    }
    
    //region Arrays as Object
    @Test
    void first_integer_array_as_object()
    {
        Object input = new Integer[]{1,2,3};

        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Integer);
        Assertions.assertEquals((Integer)res, 1);
    }

    @Test
    void first_boolean_array_as_object()
    {
        Object input = new Boolean[]{true,true,false};

        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Boolean);
        Assertions.assertEquals(res, true);
    }

    @Test
    void first_string_array_as_object()
    {
        Object input = new String[]{"name1", "name2", "name3"};

        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof String);
        Assertions.assertTrue(((String)res).equalsIgnoreCase("name1"));
    }

    @Test
    void first_byte_array_as_object()
    {
        Object input = new Byte[]{1,2,3};

        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Byte);
        Assertions.assertEquals((Byte)res, Byte.valueOf("1"));
    }

    @Test
    void first_short_array_as_object()
    {
        Object input = new Short[]{1,2,3};

        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Short);
        Assertions.assertEquals((Short)res, Short.valueOf("1"));
    }

    @Test
    void first_long_array_as_object()
    {
        Object input = new Long[]{1l,2l,3l};

        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Long);
        Assertions.assertEquals((Long)res, 1l);
    }

    @Test
    void first_float_array_as_object()
    {
        Object input = new Float[]{1f,2f,3f};

        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Float);
        Assertions.assertEquals((Float)res, 1f);
    }

    @Test
    void first_double_array_as_object()
    {
        Object input = new Double[]{1d,2d,3d};

        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Double);
        Assertions.assertEquals((Double)res, 1d);
    }

    @Test
    void first_char_array_as_object()
    {
        Object input = new Character[]{'a', 'b', 'c'};

        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Character);
        Assertions.assertEquals((Character) res, 'a');
    }
    //endregion
    
    //region List as Object
    @Test
    void first_int_list_as_object()
    {
        Object input = new ArrayList<Integer>();
        ((List<Integer>)input).add(1);
        ((List<Integer>)input).add(2);
        ((List<Integer>)input).add(3);

        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Integer);
        Assertions.assertEquals((Integer)res, 1);
    }

    @Test
    void first_byte_list_as_object()
    {
        Object input = new ArrayList<Byte>();
        ((List<Byte>)input).add(Byte.valueOf("1"));
        ((List<Byte>)input).add(Byte.valueOf("2"));
        ((List<Byte>)input).add(Byte.valueOf("3"));

        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Byte);
        Assertions.assertEquals((Byte)res, Byte.valueOf("1"));
    }

    @Test
    void first_short_list_as_object()
    {
        Object input = new ArrayList<Short>();
        ((List<Short>)input).add(Short.valueOf("1"));
        ((List<Short>)input).add(Short.valueOf("2"));
        ((List<Short>)input).add(Short.valueOf("3"));

        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Short);
        Assertions.assertEquals((Short)res, Short.valueOf("1"));
    }

    @Test
    void first_long_list_as_object()
    {
        Object input = new ArrayList<Long>();
        ((List<Long>)input).add(1l);
        ((List<Long>)input).add(2l);
        ((List<Long>)input).add(3l);

        Object res = huml.FIRST(input);
        

        Assertions.assertTrue(res instanceof Long);
        Assertions.assertEquals((Long)res, 1l);
    }

    @Test
    void first_float_list_as_object()
    {
        Object input = new ArrayList<Float>();
        ((List<Float>)input).add(1f);
        ((List<Float>)input).add(2f);
        ((List<Float>)input).add(3f);

        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Float);
        Assertions.assertEquals((Float)res, 1f);
    }

    @Test
    void first_double_list_as_object()
    {
        Object input = new ArrayList<Double>();
        ((List<Double>)input).add(1d);
        ((List<Double>)input).add(2d);
        ((List<Double>)input).add(3d);

        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Double);
        Assertions.assertEquals((Double)res, 1d);
    }

    @Test
    void first_char_list_as_object()
    {
        Object input = new ArrayList<Character>();
        ((List<Character>)input).add('a');
        ((List<Character>)input).add('b');
        ((List<Character>)input).add('c');

        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Character);
        Assertions.assertEquals((Character)res, 'a');
    }

    @Test
    void first_boolean_list_as_object()
    {
        Object input = new ArrayList<Boolean>();
        ((List<Boolean>)input).add(true);
        ((List<Boolean>)input).add(false);
        ((List<Boolean>)input).add(false);

        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Boolean);
        Assertions.assertEquals((Boolean)res, true);
    }
    //endregion
    
    
    //region primitive arrays
    @Test
    void first_primitive_boolean_array_as_object()
    {
        Object input = new boolean[]{true,true,false};
        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Boolean);
        Assertions.assertEquals((Boolean)res, true);
        
        //item.getClass().getName().equalsIgnoreCase("[z")
    }

    @Test
    void first_primitive_byte_array_as_object()
    {
        Object input = new byte[]{1,2,3};
        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Byte);
        Assertions.assertEquals((Byte)res, Byte.valueOf("1"));
    }

    @Test
    void first_primitive_short_array_as_object()
    {
        Object input = new short[]{1,2,3};
        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Short);
        Assertions.assertEquals((Short)res, Short.valueOf("1"));
    }
    
    @Test
    void first_primitive_int_array_as_object()
    {
        Object input = new int[]{1,2,3};
        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Integer);
        Assertions.assertEquals((Integer)res, 1);

        //item.getClass().getName().equalsIgnoreCase("[i")
    }

    @Test
    void first_primitive_long_array_as_object()
    {
        Object input = new long[]{1l,2l,3l};
        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Long);
        Assertions.assertEquals((Long)res, 1l);
    }

    @Test
    void first_primitive_float_array_as_object()
    {
        Object input = new float[]{1l,2l,3l};
        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Float);
        Assertions.assertEquals((Float)res, 1f);
    }

    @Test
    void first_primitive_double_array_as_object()
    {
        Object input = new double[]{1d,2d,3d};
        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Double);
        Assertions.assertEquals((Double)res, 1d);
    }

    @Test
    void first_primitive_char_array_as_object()
    {
        Object input = new char[]{'a','b','c'};
        Object res = huml.FIRST(input);

        Assertions.assertTrue(res instanceof Character);
        Assertions.assertEquals((Character) res, 'a');
    }
    //endregion
    
    //todo enums, structs(?)
    
}
