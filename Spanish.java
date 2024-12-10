class Spanish extends Language {
    public Spanish() {
        super("Hiszpański");  // Wywołanie konstruktora klasy nadrzędnej
    }

    @Override
    public void learn() {
        System.out.println("Nauka hiszpańskiego: zapoznaj się z podstawami gramatyki.");
    }
}