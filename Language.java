abstract class Language {
    protected String languageName;

    public Language(String languageName) {
        this.languageName = languageName;
    }

    public abstract void learn();

    @Override
    public String toString() {
        return languageName;
    }
}