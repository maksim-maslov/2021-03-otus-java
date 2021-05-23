package homework.annotations.usage;

import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;

public class AnnotationsUse {

    @Before
    public void setUp() {

    }

    @Test
    public void test() throws Exception {
        throw new Exception();
    }

    @After
    public void tearDown() {

    }
}
