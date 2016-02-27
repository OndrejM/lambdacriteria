package eu.inginea.lambdaquery.ruleengine;

import java.util.*;
import static java.util.Arrays.asList;
import java.util.function.Function;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

public class RuleEngine {

    private final Deque<Object> expressionBuffer = new LinkedList<>();
    private final Map<List<Class<?>>, Function<List<?>, ?>> rules = new HashMap<>();

    public void addRule(List<Class<?>> literalTypes, Function<List<?>, ?> action) {
        rules.put(literalTypes, action);
    }

    public <LITERAL_TYPE> void addRule(Class<LITERAL_TYPE> literalType, Function<LITERAL_TYPE, ?> action) {
        rules.put(asList(literalType), (Function<List<?>, Object>) (List<?> l) -> {
            return action.apply((LITERAL_TYPE) l.get(0));
        });
    }

    public void addTerm(Object term) {
        expressionBuffer.add(term);
        if (!matchSomeRule()) {
            expressionBuffer.removeLast();
            while (matchSomeRule()) {
            }
            expressionBuffer.add(term);
            matchSomeRule();
        }
    }

    public Object getExpression() {
        while (matchSomeRule()) {
        }
        if (expressionBuffer.size() > 1) {
            throw new IllegalStateException("There should be exactly one expression in buffer");
        }
        return expressionBuffer.getFirst();
    }

    private boolean matchSomeRule() {
        boolean ruleMatches = false;
        List<Class<?>> expTypesList = new LinkedList<>();
        Iterator<Object> itExp = expressionBuffer.descendingIterator();
        while (itExp.hasNext() && !ruleMatches) {
            expTypesList.add(0, itExp.next().getClass());
            Function<List<?>, ?> rule = findFirstMatchingRule(expTypesList);
            if (rule != null) {
                ruleMatches = true;
                List<Object> expList = IntStream.range(0, expTypesList.size())
                        .mapToObj(i -> expressionBuffer.pollLast())
                        .collect(toList());
                Collections.reverse(expList);
                expressionBuffer.add(rule.apply(expList));
            }
        }
        return ruleMatches;
    }

    private Function<List<?>, ?> findFirstMatchingRule(List<Class<?>> expTypesList) {
        Optional<List<Class<?>>> matchingKey = rules.keySet().stream()
                .filter(typeList -> {
                    Iterator<Class<?>> itTypes = expTypesList.iterator();
                    return expTypesList.size() == typeList.size()
                            && typeList.stream().allMatch(t -> t.isAssignableFrom(itTypes.next()));
                })
                .findAny();
        if (matchingKey.isPresent()) {
            return rules.get(matchingKey.get());
        } else {
            return null;
        }
    }

}
