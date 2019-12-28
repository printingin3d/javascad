package eu.printingin3d.javascad.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import eu.printingin3d.javascad.context.ColorHandlingContext;
import eu.printingin3d.javascad.coords.Angles3d;
import eu.printingin3d.javascad.coords.Boundaries3d;
import eu.printingin3d.javascad.coords.Boundaries3dTest;
import eu.printingin3d.javascad.coords.Coords3d;
import eu.printingin3d.javascad.testutils.Test3dModel;

public class Abstract3dModelFindAnnotationsTest {
    private static final Boundaries3d ONE = new Boundaries3d(new Coords3d(-1, -1, -1), new Coords3d(+1, +1, +1));
    private static final Boundaries3d RND = new Boundaries3d(new Coords3d(-1, -2, -3), new Coords3d(+3, +2, +1));

    private static void assertResult(List<Abstract3dModel> annotatedModels,  
            Boundaries3d expectedBoundaries, String expectedScad) {
        assertEquals(1, annotatedModels.size());
        String scad = annotatedModels.iterator().next().toScad(ColorHandlingContext.DEFAULT).getScad();
        assertTrue("The generated SCAD should contain "+expectedScad+", but was "+scad, scad.contains(expectedScad));
        Boundaries3dTest.assertBoundariesEquals(expectedBoundaries, annotatedModels.iterator().next().getBoundaries());
    }
    
    @Test
    public void shouldFindItselfForNull() {
        Abstract3dModel testSubject = new Test3dModel("asd", ONE);
        assertResult(testSubject.findAnnotatedModel(null), ONE, "asd");
    }
    
    @Test
    public void shouldFindItself() {
        Abstract3dModel testSubject = new Test3dModel("asd", ONE).annotate("one");
        assertResult(testSubject.findAnnotatedModel("one"), ONE, "asd");
    }
    
    @Test
    public void shouldFindPartOfUnion() {
        Abstract3dModel testSubject = new Test3dModel("asd", ONE).annotate("one").addModel(new Test3dModel("qwe", ONE));
        assertResult(testSubject.findAnnotatedModel("one"), ONE, "asd");
    }
    
    @Test
    public void shouldMoveBoundaries() {
        Abstract3dModel testSubject = new Test3dModel("asd", ONE).annotate("one").addModel(new Test3dModel("qwe", ONE)).move(new Coords3d(1, 2, 3));
        assertResult(testSubject.findAnnotatedModel("one"), testSubject.getBoundaries(), "asd");
    }
    
    @Test
    public void shouldRotateBoundaries() {
        Abstract3dModel testSubject = new Test3dModel("asd", RND).annotate("one").addModel(new Test3dModel("qwe", RND)).rotate(Angles3d.ROTATE_MINUS_X);
        assertResult(testSubject.findAnnotatedModel("one"), testSubject.getBoundaries(), "asd");
    }
}
