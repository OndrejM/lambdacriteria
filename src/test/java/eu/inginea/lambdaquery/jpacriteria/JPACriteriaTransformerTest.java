package eu.inginea.lambdaquery.jpacriteria;

import eu.inginea.lambdaquery.ruleengine.Parameter;
import eu.inginea.lambdaquery.ruleengine.Term;
import eu.inginea.lambdaquery.ruleengine.Constant;
import eu.inginea.lambdaquery.ruleengine.BinaryOperation;
import eu.inginea.lambdaquery.ruleengine.Path;
import java.util.*;
import javax.persistence.criteria.*;
import integration.jpa.model.Person;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import testbase.JPATestBase;

/**
 *
 * @author ondro
 */
public class JPACriteriaTransformerTest extends JPATestBase {
    private JPACriteriaFilterHandler transformer;
    private List<Term> terms;

    public JPACriteriaTransformerTest() {
    }

    @Test
    public void can_build_criteria_for_simple_term_expression() {
        given(() -> {
            transformer = personTransformer();
            terms = Arrays.asList(new Constant("Ondro:String"),
                    BinaryOperation.EQUAL,
                    new Parameter(0),
                    new Path("name"));
        });
        when(() -> {
            terms.stream().forEach(transformer::handleToken);
        });
        then(() -> {
            Expression<?> criteriaQuery = transformer.getCriteriaQuery();
            assertThat("criteria query", criteriaQuery, is(not(nullValue()))); 
            assertThat("criteria query type", criteriaQuery.getJavaType(), is(equalTo(Boolean.class)));
        });
    }

    @Test
    public void can_build_criteria_for_nested_paths() {
        given(() -> {
            transformer = personTransformer();
            terms = Arrays.asList(
                    new Constant("Nitra"),
                    BinaryOperation.EQUAL,
                    new Parameter(0),
                    new Path("address"),
                    new Path("city")
            );
        });
        when(() -> {
            terms.stream().forEach(transformer::handleToken);
        });
        then(() -> {
            Expression<?> criteriaQuery = transformer.getCriteriaQuery();
            assertThat("criteria query", criteriaQuery, is(not(nullValue()))); 
            assertThat("criteria query type", criteriaQuery.getJavaType(), is(equalTo(Boolean.class)));
        });
    }

    private JPACriteriaFilterHandler personTransformer() {
        CriteriaBuilder cb = getEM().getCriteriaBuilder();
        CriteriaQuery<Object> q = cb.createQuery();
        return new JPACriteriaFilterHandler(q.from(Person.class), cb, q);
    }
}
