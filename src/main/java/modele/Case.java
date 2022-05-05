package modele;

public class Case {
    private final int value;

    public Case(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Case merge(Case case1, Case case2) {
        return new Case(case1.getValue() + case2.getValue());
    }
}
