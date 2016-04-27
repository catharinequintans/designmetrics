package br.edu.ufcg.splab.designmetrics;

import java.util.Set;

import org.designwizard.api.DesignWizard;
import org.designwizard.design.ClassNode;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import br.edu.ufcg.splab.designmetrics.metrics.Coupling;

/**
 * Test class with the verification of design rules recommended by the hibernate for persistent classes.
 * This Unit Test Example use TestNG Framework.
 *
 *  The program ckjm calculates Chidamber and Kemerer object-oriented metrics by processing the
 *  bytecode of compiled Java files. The program calculates for each class the following six metrics,
 *  and displays them on its standard output, following the class's name:
 *
 *  WMC: Weighted methods per class
 *  DIT: Depth of Inheritance Tree
 *  NOC: Number of Children
 *  CBO: Coupling between object classes
 *  RFC: Response for a Class
 *  LCOM: Lack of cohesion in methods
 *  Ca: Afferent coupling (not a C&K metric)
 *  NPM: Number of Public Methods for a class (not a C&K metric)
 *
 * java -jar ~/dev/ckjm-1.9/build/ckjm-1.9.jar *.class
 *
 * br.edu.ufcg.splab.designmetrics.mocks.ClassA   1   1   0   0   2   0   2   1
 * br.edu.ufcg.splab.designmetrics.mocks.ClassB   1   1   0   1   2   0   1   1
 * br.edu.ufcg.splab.designmetrics.mocks.ClassC   1   1   0   2   2   0   2   1
 * br.edu.ufcg.splab.designmetrics.mocks.ClassD   1   1   0   1   2   0   2   1
 * br.edu.ufcg.splab.designmetrics.mocks.ClassE   3   1   0   1   4   1   1   3
 * br.edu.ufcg.splab.designmetrics.mocks.ClassF  13   1   0   2  15  62   2  13
 * br.edu.ufcg.splab.designmetrics.mocks.ClassG   2   1   0   1   4   1   0   2
 * br.edu.ufcg.splab.designmetrics.mocks.ClassH   3   1   0   2   6   1   0   3

 *
 * @author Taciano Morais Silva - tacianosilva@gmail.com
 */
@Test
public class EfferentCouplingTest {

    private SoftAssert softAssert;
    private DesignWizard designWizard;
    private Coupling coupling;

    private ClassNode classA;
    private ClassNode classB;
    private ClassNode classC;
    private ClassNode classD;
    private ClassNode classE;
    private ClassNode classF;
    private ClassNode classG;
    private ClassNode classH;

    @BeforeClass
    public void setUp() throws Exception {
        // Design for all classes in the project.
        // Add here binary code or jar file of the project.
        designWizard = new DesignWizard("target/test-classes/br/edu/ufcg/splab/designmetrics/mocks/");
        coupling = new Coupling(designWizard);

        // Classe Vazia
        classA = designWizard.getClass("br.edu.ufcg.splab.designmetrics.mocks.ClassA");

        // Classe com um atributo da Classe A
        classB = designWizard.getClass("br.edu.ufcg.splab.designmetrics.mocks.ClassB");

        // Classe com dois atributos da Classe A e Classe B
        classC = designWizard.getClass("br.edu.ufcg.splab.designmetrics.mocks.ClassC");

        // Classe com dois atributos da Classe C
        classD = designWizard.getClass("br.edu.ufcg.splab.designmetrics.mocks.ClassD");

        // Classe com um atributo da Classe C e métodos get e set para o atributo
        classE = designWizard.getClass("br.edu.ufcg.splab.designmetrics.mocks.ClassE");

        // Classe com vários atributos da Classe D e Classe E com gets e sets. Além de atributos de tipos primitivos.
        classF = designWizard.getClass("br.edu.ufcg.splab.designmetrics.mocks.ClassF");

        // Classe com um atributo da Classe E e faz chamada a um método de tipo primitivo
        classG = designWizard.getClass("br.edu.ufcg.splab.designmetrics.mocks.ClassG");

        // Classe com um atributo da Classe F e faz chamada a dois método do tipo Classe D
        classH = designWizard.getClass("br.edu.ufcg.splab.designmetrics.mocks.ClassH");
    }

    @BeforeMethod
    public void startTest() {
         softAssert = new SoftAssert();
    }

    @AfterClass
    public void tearDown() throws Exception {
    }

    /**
     */
    public void testCeClassA() {
        Set<ClassNode> directRelatedEntities = coupling.getDirectRelatedEntities(classA);

        softAssert.assertEquals(coupling.efferentCoupling(classA), 0, "\nCe: ");
        softAssert.assertEquals(directRelatedEntities.size(), 0);
        softAssert.assertAll();
    }

    public void testCeClassB() {
        Set<ClassNode> directRelatedEntities = coupling.getDirectRelatedEntities(classB);
        softAssert.assertTrue(directRelatedEntities.contains(classA));
        softAssert.assertEquals(coupling.efferentCoupling(classB), 1, "\nCe: ");
        softAssert.assertAll();
    }

    public void testCeClassC() {
        Set<ClassNode> directRelatedEntities = coupling.getDirectRelatedEntities(classC);
        softAssert.assertTrue(directRelatedEntities.contains(classA));
        softAssert.assertTrue(directRelatedEntities.contains(classB));
        softAssert.assertEquals(coupling.efferentCoupling(classC), 2, "\nCe: ");
        softAssert.assertAll();
    }

    public void testCeClassD() {
        Set<ClassNode> directRelatedEntities = coupling.getDirectRelatedEntities(classD);
        softAssert.assertTrue(directRelatedEntities.contains(classC));
        softAssert.assertEquals(coupling.efferentCoupling(classD), 1, "\nCe: ");
        softAssert.assertAll();
    }

    public void testCeClassE() {
        Set<ClassNode> directRelatedEntities = coupling.getDirectRelatedEntities(classE);
        softAssert.assertTrue(directRelatedEntities.contains(classC));
        softAssert.assertEquals(coupling.efferentCoupling(classE), 1, "\nCe: ");
        softAssert.assertAll();
    }

    public void testCeClassF() {
        Set<ClassNode> directRelatedEntities = coupling.getDirectRelatedEntities(classF);
        softAssert.assertTrue(directRelatedEntities.contains(classD));
        softAssert.assertTrue(directRelatedEntities.contains(classE));
        softAssert.assertEquals(coupling.efferentCoupling(classF), 2, "\nCe: ");
        softAssert.assertAll();
    }

    public void testCeClassG() {
        Set<ClassNode> directRelatedEntities = coupling.getDirectRelatedEntities(classG);
        for (ClassNode classNode : directRelatedEntities) {
            System.out.println("ClassG -> " + classNode.getClassName());
        }
        softAssert.assertTrue(directRelatedEntities.contains(classF));
        softAssert.assertEquals(coupling.efferentCoupling(classG), 1, "\nCe: ");
        softAssert.assertAll();
    }

    public void testCeClassH() {
        Set<ClassNode> directRelatedEntities = coupling.getDirectRelatedEntities(classH);
        softAssert.assertTrue(directRelatedEntities.contains(classF), "\nClassF");
        softAssert.assertTrue(directRelatedEntities.contains(classD), "\nClassD");
        softAssert.assertEquals(coupling.efferentCoupling(classH), 2, "\nCe: ");
        softAssert.assertAll();
    }
}
